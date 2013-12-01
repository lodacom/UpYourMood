package models;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import org.h2.command.ddl.CreateRole;
import org.openjena.riot.RiotException;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import controllers.Application;

/**
 * Classe permettant de construire le fichier upyourmood.rdf.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 */
@SuppressWarnings("deprecation")
public class RDFBuilding {
	private static volatile RDFBuilding instance = null;
	private static final String rdf_file = "public/rdf/upyourmood.rdf";
	private static Model m=null;
	public static long cpt = 0;
	public CompteurRDF cRDF;
	
	/**
	 * Constructeur permettant de charger le fichier upyourmood.rdf afin de l'enrichir par la suite.
	 * Le constructeur récupère également la valeur actuelle du compteur, servant à numéroter une expérience musicale.
	 */
	
	private RDFBuilding(){
		try{
			m=FileManager.get().loadModel(rdf_file);
		}catch(RiotException e){
			m = ModelFactory.createDefaultModel();
		}
		cRDF=new CompteurRDF();
		cpt=cRDF.select();
	}

	/**
	 * <p> Récupérer l'instance de RDFBuilding, ou la créer avec les préfixes nécessaires ainsi
	 * que les notions de sous-propriétés. </p>
	 * @return l'instance de RDFBuilding 
	 */
	public final static RDFBuilding getInstance() {
		if (RDFBuilding.instance == null) {
			synchronized(RDFBuilding.class) {
				if (RDFBuilding.instance == null) {
					RDFBuilding.instance = new RDFBuilding();
					m.setNsPrefix("uym", OntologyUpYourMood.getUym());
					m.setNsPrefix("music", OntologyUpYourMood.getUymMusic());
					m.setNsPrefix("wordconnotation", OntologyUpYourMood.getUymWordConnotation());
					m.setNsPrefix("user", OntologyUpYourMood.getUymUser());
					m.setNsPrefix("rdf", RDF.getURI());
					m.setNsPrefix("rdfs", RDFS.getURI());
					m.setNsPrefix("foaf", FOAF.getURI());
					m.setNsPrefix("dc", DC.getURI());
					m.setNsPrefix("nicetag", NiceTag.getURI());
					m.add(OntologyUpYourMood.albumTitle, RDFS.subPropertyOf, DC.title);
					m.add(NiceTag.makesMeFeel,RDFS.subPropertyOf,NiceTag.isRelatedTo);
					m.add(OntologyUpYourMood.songTitle, RDFS.subPropertyOf, DC.title);
					m.add(OntologyUpYourMood.hasListen,RDFS.subPropertyOf,FOAF.knows);
					m.add(OntologyUpYourMood.makesMeThink,RDFS.subPropertyOf,NiceTag.makesMeFeel);


				}
			}
		}
		return RDFBuilding.instance;
	}

	/**
	 * Fonction principale qu'il faudra appeler dans la classe Application
	 * @param infoMusic information de la musique courante
	 */
	public void rdfUpYourMood(List<String> infoMusic,UserInformation userInfo,WordConnotation word,String urlImage){
		ajouterMusique(infoMusic);
		ajouterUtilisateur(userInfo, infoMusic, word, urlImage);
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream("public/rdf/upyourmood.rdf");
			m.write(outStream, "RDF/XML-ABBREV");
			outStream.close();
		} catch (IOException e) {
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

		OntologyUpYourMood.Music = m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(0));
		OntologyUpYourMood.Album = m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(1));
		OntologyUpYourMood.Artist = m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(2));
		OntologyUpYourMood.AlbumCover = m.createResource(infoMusic.get(3));
		OntologyUpYourMood.Title= m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(4));

		m.add(OntologyUpYourMood.Music,OntologyUpYourMood.albumTitle,OntologyUpYourMood.Album);
		m.add(OntologyUpYourMood.Music,DC.creator,OntologyUpYourMood.Artist);
		m.add(OntologyUpYourMood.Music, FOAF.depiction,OntologyUpYourMood.AlbumCover);
		m.add(OntologyUpYourMood.Music,OntologyUpYourMood.songTitle,OntologyUpYourMood.Title);

	}

	/**
	 * Dans cette fonction on ajoute:
	 * <ul>
	 * 	<li>le pseudo</li>
	 *  <li>le mot associé à la musique</li>
	 *  <li>la connotation du mot</li>
	 *  <li>l'url de l'image associée au mot</li>
	 * </ul>
	 */
	private void ajouterUtilisateur(UserInformation userInfo, List<String> infoMusic, WordConnotation word, String urlImage){

		try {
			userInfo.retrieveInformation(Application.maSession.getPseudo());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		OntologyUpYourMood.MusicalExperience=m.createResource(OntologyUpYourMood.getUymUser()+userInfo.getInfoUser().pseudo+"/"+"musicalExperience"+cpt);
		OntologyUpYourMood.User = m.createResource(OntologyUpYourMood.getUymUser()+userInfo.getInfoUser().pseudo);
        OntologyUpYourMood.connotation=m.createTypedLiteral(word.getConnotation(),XSDDatatype.XSDfloat);
		OntologyUpYourMood.MusicalExperience.addProperty(NiceTag.makesMeFeel,
				m.createResource()
				.addProperty(OntologyUpYourMood.isAssociatedBy, word.getMot())
				.addProperty(OntologyUpYourMood.isConnoted, OntologyUpYourMood.connotation)
				.addProperty(OntologyUpYourMood.makesMeThink, urlImage));
		OntologyUpYourMood.MusicalExperience.addProperty(OntologyUpYourMood.hasListen, infoMusic.get(0));
		m.add(OntologyUpYourMood.User,OntologyUpYourMood.hasMusicalExperience,OntologyUpYourMood.MusicalExperience);
		cpt++;
		cRDF.update();
	}
}
