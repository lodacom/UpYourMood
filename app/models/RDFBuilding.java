package models;

import java.util.List;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDFS;

public class RDFBuilding {
	private static volatile RDFBuilding instance = null;
	private static final String rdf_file = "public/rdf/upyourmood.rdf";
	private final String prefixe = "http://www.upyourmood.com/";
	private Model m=null;
	Property TitreAlbum =null;
	
	private RDFBuilding(){
		m=FileManager.get().loadModel(rdf_file);
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
	public void rdfUpYourMood(List<String> infoMusic){
		ajouterMusique(infoMusic);
		ajouterUtilisateur();
		ajouterMot_Connotation();
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
		if (music!=null){
			Resource Music = m.createResource(music+infoMusic.get(0));
			Music.addLiteral(m.getProperty("AlbumTitle"), album);
			Music.addLiteral(DC.creator, artiste);
			Music.addLiteral(FOAF.logo, pochette);
			Music.addLiteral(DC.title, titre);
		}else{
			String musicNs=prefixe+"music/";
			m.setNsPrefix("music", musicNs);
			Resource Music = m.createResource(musicNs+infoMusic.get(0));
			TitreAlbum = m.createProperty(musicNs+"AlbumTitle");
			m.add(TitreAlbum, RDFS.subPropertyOf, DC.title);
		}
	}
	
	/**
	 * Dans cette fonction on ajoute:
	 * a minima:
	 * <ul>
	 * 	<li>le pseudo</li>
	 * </ul>
	 */
	private void ajouterUtilisateur(){
		String user=m.getNsPrefixURI("user");
		if (user!=null){

		}else{
			String userNs=prefixe+"user/";
			m.setNsPrefix("user", userNs);
			Resource User = m.createResource(userNs+"User");
		}
	}
	
	private void ajouterMot_Connotation(){
		String mot=m.getNsPrefixURI("wordconnotation");
		if (mot!=null){
			
		}else{
			String motNs=prefixe+"wordconnotation/";
			m.setNsPrefix("wordconnotation", motNs);
			Resource Mot = m.createResource(motNs+"WordConnotation");
		}
	}
}
