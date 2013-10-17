package models;

import java.io.*;
import java.util.List;
import org.openjena.riot.RiotException;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

@SuppressWarnings("deprecation")
public class RDFBuilding {
	private static volatile RDFBuilding instance = null;
	private static final String rdf_file = "public/rdf/upyourmood.rdf";
	private final String prefixe = "http://www.upyourmood.com/";
	private Model m=null;
	Resource Music=null;
	Resource User =null;
	
	private RDFBuilding(){
		try{
			m=FileManager.get().loadModel(rdf_file);
		}catch(RiotException e){
			m = ModelFactory.createDefaultModel();
		}
	}
	
	public final static RDFBuilding getInstance() {
        if (RDFBuilding.instance == null) {
           synchronized(RDFBuilding.class) {
             if (RDFBuilding.instance == null) {
            	 RDFBuilding.instance = new RDFBuilding();
             }
           }
        }
        return RDFBuilding.instance;
    }
	
	/**
	 * fonction principale qu'il faudra appeler dans la classe Application
	 * @param List<String> infoMusic information de la musique courante
	 */
	public void rdfUpYourMood(List<String> infoMusic,UserInformation userInfo,WordConnotation word){
		ajouterMusique(infoMusic);
		ajouterUtilisateur(userInfo);
		ajouterMot_Connotation(word);
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream("public/rdf/upyourmood.rdf");
			m.write(outStream, "RDF/XML");
			//m.write(outStream, "N3");
	        outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	/**
	 * Dans cette fonction on ajoute:
	 * <ul>
	 * 	<li>l'identifiant de la musique</li>
	 * 	<li>titre de la musique</li>
	 * 	<li>titre de l'album</li>
	 * 	<li>nom de l'artiste</li>
	 * 	<li>url de la photo de l'album</li>
	 * </ul>
	 */
	private void ajouterMusique(List<String> infoMusic){
		String music=m.getNsPrefixURI("music");
		Literal album=m.createTypedLiteral(infoMusic.get(1),XSDDatatype.XSDstring);
		Literal artiste=m.createTypedLiteral(infoMusic.get(2),XSDDatatype.XSDstring);
		Literal pochette=m.createTypedLiteral(infoMusic.get(3),XSDDatatype.XSDanyURI);
		Literal titre=m.createTypedLiteral(infoMusic.get(4),XSDDatatype.XSDstring);
		
		Music = m.createResource(music+infoMusic.get(0));
		if (music!=null){
			Property TitreAlbum = m.createProperty(music+"AlbumTitle");
			
			Music.addLiteral(TitreAlbum, album);
			Music.addLiteral(DC.creator, artiste);
			Music.addLiteral(FOAF.depiction, pochette);
			Music.addLiteral(DC.title, titre);

			m.add(Music,RDF.type,Music);
		}else{
			String musicNs=prefixe+"music/";
			m.setNsPrefix("music", musicNs);
			
			Music = m.createResource(musicNs+infoMusic.get(0));
			
			Property TitreAlbum = m.createProperty(musicNs+"AlbumTitle");
			m.add(TitreAlbum, RDFS.subPropertyOf, DC.title);
			
			Music.addLiteral(TitreAlbum, album);
			Music.addLiteral(DC.creator, artiste);
			Music.addLiteral(FOAF.depiction, pochette);
			Music.addLiteral(DC.title, titre);

			m.add(Music,RDF.type,Music);
		}
	}
	
	/**
	 * Dans cette fonction on ajoute:
	 * a minima:
	 * <ul>
	 * 	<li>le pseudo</li>
	 * </ul>
	 */
	private void ajouterUtilisateur(UserInformation userInfo){
		String user=m.getNsPrefixURI("user");
		User = m.createResource(user+userInfo.getInfoUser().get(0));
		if (user!=null){
			Property AEcoute=m.createProperty(user+"HasListen");
			
			m.add(AEcoute,RDFS.subPropertyOf,FOAF.knows);
			m.add(User,RDF.type,User);
			m.add(User,AEcoute,Music);
		}else{
			String userNs=prefixe+"user/";
			m.setNsPrefix("user", userNs);
			User = m.createResource(userNs+userInfo.getInfoUser().get(0));
			
			Property AEcoute = m.createProperty(userNs+"HasListen");
			m.add(AEcoute,RDFS.subPropertyOf,FOAF.knows);
			
			m.add(User,RDF.type,User);
			m.add(User,AEcoute,Music);
		}
	}
	
	private void ajouterMot_Connotation(WordConnotation word){
		String mot=m.getNsPrefixURI("wordconnotation");
		
		Literal connotation=m.createTypedLiteral(word.getConnotation(),XSDDatatype.XSDfloat);
		Literal mon_mot=m.createTypedLiteral(word.getMot(),XSDDatatype.XSDstring);
		
		Resource Mot = m.createResource(mot+word.getMot()+"/"+word.getConnotation());
		if (mot!=null){
			Property connot = m.createProperty(mot+"APourConnotation");
			Property Tag = m.createProperty(mot+"Tag");
			Property AssociePar = m.createProperty(mot+"IsAssociatedBy");
			
			Mot.addLiteral(connot, mon_mot);			
			Mot.addLiteral(connot, connotation);
			
			m.add(Mot,RDF.type,Mot);
			m.add(Mot,RDF.type,connot);	
			m.add(Mot,Tag,Music);
			m.add(Mot,AssociePar,User);
		}else{
			String motNs=prefixe+"wordconnotation/";
			m.setNsPrefix("wordconnotation", motNs);
			Mot = m.createResource(motNs+word.getMot()+"/"+word.getConnotation());
			
			Property APourConnotation = m.createProperty(motNs+"APourConnotation");
			Property Tag = m.createProperty(motNs+"Tag");
			Property AssociePar = m.createProperty(motNs+"IsAssociatedBy");
			
			Mot.addLiteral(APourConnotation, mon_mot);			
			Mot.addLiteral(APourConnotation, connotation);
			
			m.add(Mot,RDF.type,Mot);
			m.add(Mot,RDF.type,APourConnotation);
			m.add(Mot,Tag,Music);
			m.add(Mot,AssociePar,User);
		}
	}
}
