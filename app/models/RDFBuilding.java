package models;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import org.openjena.riot.RiotException;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import controllers.Application;

@SuppressWarnings("deprecation")
public class RDFBuilding {
	private static volatile RDFBuilding instance = null;
	private static final String rdf_file = "public/rdf/upyourmood.rdf";
	//private final String prefixe = "http://www.upyourmood.com/";
	private Model m=null;


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
		//............................................................................................
		m.add(NiceTag.makesMeFeel,RDFS.subPropertyOf,NiceTag.isRelatedTo);
		//............................................................................................
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

		OntologyUpYourMood.album = m.createResource(music+infoMusic.get(1));
		OntologyUpYourMood.artiste = m.createResource(music+infoMusic.get(2));
		OntologyUpYourMood.pochette = m.createResource(music+infoMusic.get(3));
		OntologyUpYourMood.titre= m.createResource(music+infoMusic.get(4));

		OntologyUpYourMood.Music = m.createResource(music+infoMusic.get(0));
		if (music!=null){
			OntologyUpYourMood.AlbumTitle = m.createProperty(music+"AlbumTitle");

			m.add(OntologyUpYourMood.Music,OntologyUpYourMood.AlbumTitle,OntologyUpYourMood.album);
			m.add(OntologyUpYourMood.Music,DC.creator,OntologyUpYourMood.artiste);
			m.add(OntologyUpYourMood.Music, FOAF.depiction,OntologyUpYourMood.pochette);
			m.add(OntologyUpYourMood.Music,DC.title,OntologyUpYourMood.titre);
			OntologyUpYourMood.Music.addLiteral(DC.title, OntologyUpYourMood.titre);

			m.add(OntologyUpYourMood.Music,RDF.type,OntologyUpYourMood.Music);
		}else{
			String musicNs=OntologyUpYourMood.getURI()+"music/";
			m.setNsPrefix("music", musicNs);

			OntologyUpYourMood.Music = m.createResource(musicNs+infoMusic.get(0));
			OntologyUpYourMood.album = m.createResource(musicNs+infoMusic.get(1));
			OntologyUpYourMood.artiste = m.createResource(musicNs+infoMusic.get(2));
			OntologyUpYourMood.pochette = m.createResource(musicNs+infoMusic.get(3));
			OntologyUpYourMood.titre= m.createResource(musicNs+infoMusic.get(4));

			OntologyUpYourMood.AlbumTitle = m.createProperty(musicNs+"AlbumTitle");
			m.add(OntologyUpYourMood.AlbumTitle, RDFS.subPropertyOf, DC.title);

			m.add(OntologyUpYourMood.Music,OntologyUpYourMood.AlbumTitle,OntologyUpYourMood.album);
			m.add(OntologyUpYourMood.Music,DC.creator,OntologyUpYourMood.artiste);
			m.add(OntologyUpYourMood.Music, FOAF.depiction,OntologyUpYourMood.pochette);
			m.add(OntologyUpYourMood.Music,DC.title,OntologyUpYourMood.titre);

			m.add(OntologyUpYourMood.Music,RDF.type,OntologyUpYourMood.Music);
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
		try {
			userInfo.retrieveInformation(Application.maSession.getPseudo());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OntologyUpYourMood.User = m.createResource(user+userInfo.getInfoUser().get(0));
		if (user!=null){
			OntologyUpYourMood.HasListen=m.createProperty(user+"HasListen");

			m.add(OntologyUpYourMood.HasListen,RDFS.subPropertyOf,FOAF.knows);
			m.add(OntologyUpYourMood.User,RDF.type,OntologyUpYourMood.User);
			m.add(OntologyUpYourMood.User,OntologyUpYourMood.HasListen,OntologyUpYourMood.Music);
		}else{
			String userNs=OntologyUpYourMood.getURI()+"user/";
			m.setNsPrefix("user", userNs);
			OntologyUpYourMood.User = m.createResource(userNs+userInfo.getInfoUser().get(0));

			OntologyUpYourMood.HasListen = m.createProperty(userNs+"HasListen");
			m.add(OntologyUpYourMood.HasListen,RDFS.subPropertyOf,FOAF.knows);

			m.add(OntologyUpYourMood.User,RDF.type,OntologyUpYourMood.User);
			m.add(OntologyUpYourMood.User,OntologyUpYourMood.HasListen,OntologyUpYourMood.Music);
		}
	}

	private void ajouterMot_Connotation(WordConnotation word){
		String mot=m.getNsPrefixURI("wordconnotation");

		OntologyUpYourMood.connotation=m.createTypedLiteral(word.getConnotation(),XSDDatatype.XSDfloat);
		OntologyUpYourMood.userWord=m.createTypedLiteral(word.getMot(),XSDDatatype.XSDstring);

		OntologyUpYourMood.Word = m.createResource(mot+word.getMot()+"/"+word.getConnotation());
		if (mot!=null){
			OntologyUpYourMood.IsConnoted = m.createProperty(mot+"IsConnoted");
			OntologyUpYourMood.IsAssociatedBy = m.createProperty(mot+"IsAssociatedBy");

			OntologyUpYourMood.Word.addLiteral(OntologyUpYourMood.IsConnoted, OntologyUpYourMood.userWord);			
			OntologyUpYourMood.Word.addLiteral(OntologyUpYourMood.IsConnoted, OntologyUpYourMood.connotation);

			m.add(OntologyUpYourMood.Word,RDF.type,OntologyUpYourMood.Word);

			m.add(OntologyUpYourMood.Word,RDF.type,OntologyUpYourMood.IsConnoted);	
			m.add(OntologyUpYourMood.Word,NiceTag.makesMeFeel,OntologyUpYourMood.Music);
			m.add(OntologyUpYourMood.Word,OntologyUpYourMood.IsAssociatedBy,OntologyUpYourMood.User);
		}else{
			String motNs=OntologyUpYourMood.getURI()+"wordconnotation/";
			m.setNsPrefix("wordconnotation", motNs);
			OntologyUpYourMood.Word = m.createResource(motNs+word.getMot()+"/"+word.getConnotation());

			OntologyUpYourMood.Word.addLiteral(OntologyUpYourMood.IsConnoted, OntologyUpYourMood.userWord);			
			OntologyUpYourMood.Word.addLiteral(OntologyUpYourMood.IsConnoted, OntologyUpYourMood.connotation);

			m.add(OntologyUpYourMood.Word,RDF.type,OntologyUpYourMood.Word);
			m.add(OntologyUpYourMood.Word,RDF.type,OntologyUpYourMood.IsConnoted);

			m.add(OntologyUpYourMood.Word,NiceTag.makesMeFeel,OntologyUpYourMood.Music);
			m.add(OntologyUpYourMood.Word,OntologyUpYourMood.IsAssociatedBy,OntologyUpYourMood.User);
		}
	}
}
