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

@SuppressWarnings("deprecation")
public class RDFBuilding {
	private static volatile RDFBuilding instance = null;
	private static final String rdf_file = "public/rdf/upyourmood.rdf";
	private static Model m=null;
	public static int cpt = 0;

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
					m.setNsPrefix("uym", OntologyUpYourMood.getUym());
					m.setNsPrefix("music", OntologyUpYourMood.getUymMusic());
					m.setNsPrefix("wordconnotation", OntologyUpYourMood.getUymWordConnotation());
					m.setNsPrefix("user", OntologyUpYourMood.getUymUser());
					m.setNsPrefix("rdf", RDF.getURI());
					m.setNsPrefix("rdfs", RDFS.getURI());
					m.setNsPrefix("foaf", FOAF.getURI());
					m.setNsPrefix("dc", DC.getURI());
					m.setNsPrefix("nicetag", NiceTag.getURI());
					m.add(OntologyUpYourMood.AlbumTitle, RDFS.subPropertyOf, DC.title);
					m.add(NiceTag.makesMeFeel,RDFS.subPropertyOf,NiceTag.isRelatedTo);
					m.add(OntologyUpYourMood.SongTitle, RDFS.subPropertyOf, DC.title);
					m.add(OntologyUpYourMood.HasListen,RDFS.subPropertyOf,FOAF.knows);

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
		ajouterUtilisateur(userInfo, infoMusic, word);
		//ajouterMot_Connotation(word);

		//............................................................................................
		
		//............................................................................................
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
		OntologyUpYourMood.album = m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(1));
		OntologyUpYourMood.artist = m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(2));
		OntologyUpYourMood.albumCover = m.createResource(infoMusic.get(3));
		OntologyUpYourMood.title= m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(4));

		m.add(OntologyUpYourMood.Music,OntologyUpYourMood.AlbumTitle,OntologyUpYourMood.album);
		m.add(OntologyUpYourMood.Music,DC.creator,OntologyUpYourMood.artist);
		m.add(OntologyUpYourMood.Music, FOAF.depiction,OntologyUpYourMood.albumCover);
		m.add(OntologyUpYourMood.Music,OntologyUpYourMood.SongTitle,OntologyUpYourMood.title);

	}

	/**
	 * Dans cette fonction on ajoute:
	 * a minima:
	 * <ul>
	 * 	<li>le pseudo</li>
	 * </ul>
	 */
	private void ajouterUtilisateur(UserInformation userInfo, List<String> infoMusic, WordConnotation word ){

		try {
			userInfo.retrieveInformation(Application.maSession.getPseudo());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		OntologyUpYourMood.musicalExperience=m.createResource(OntologyUpYourMood.getUymUser()+userInfo.getInfoUser().pseudo+"/"+"musicalExperience"+cpt);
		OntologyUpYourMood.User = m.createResource(OntologyUpYourMood.getUymUser()+userInfo.getInfoUser().pseudo);
		OntologyUpYourMood.musicalExperience.addProperty(NiceTag.makesMeFeel,
				m.createResource()
				.addProperty(OntologyUpYourMood.IsAssociatedBy, word.getMot())
				.addProperty(OntologyUpYourMood.IsConnoted, String.valueOf(word.getConnotation())));
		OntologyUpYourMood.musicalExperience.addProperty(OntologyUpYourMood.HasListen, infoMusic.get(0));
		m.add(OntologyUpYourMood.User,OntologyUpYourMood.HasMusicalExperience,OntologyUpYourMood.musicalExperience);
		cpt++;

	}

	/*private void ajouterMot_Connotation(WordConnotation word){

		OntologyUpYourMood.Word = m.createResource(OntologyUpYourMood.getUymWordConnotation()+word.getMot());
		OntologyUpYourMood.connotation=m.createTypedLiteral(word.getConnotation(),XSDDatatype.XSDfloat);

		OntologyUpYourMood.IsConnoted = m.createProperty(OntologyUpYourMood.getUymWordConnotation()+"IsConnoted");
		OntologyUpYourMood.IsAssociatedBy = m.createProperty(OntologyUpYourMood.getUymWordConnotation()+"IsAssociatedBy");

		OntologyUpYourMood.Word.addLiteral(OntologyUpYourMood.IsConnoted, OntologyUpYourMood.connotation);

		m.add(OntologyUpYourMood.Word,RDF.type,OntologyUpYourMood.Word);

		m.add(OntologyUpYourMood.Word,RDF.type,OntologyUpYourMood.IsConnoted);	
		OntologyUpYourMood.User.addProperty(OntologyUpYourMood.IsAssociatedBy, OntologyUpYourMood.Word);
		//m.add(OntologyUpYourMood.User,OntologyUpYourMood.IsAssociatedBy,OntologyUpYourMood.Word);

	}*/
}
