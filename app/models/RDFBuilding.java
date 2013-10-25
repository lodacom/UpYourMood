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
	//private final String prefixe = "http://www.upyourmood.com/";
	private static Model m=null;


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
		ajouterUtilisateur(userInfo, infoMusic);
		//ajouterMot_Connotation(word);
		
		//............................................................................................
		m.add(NiceTag.makesMeFeel,RDFS.subPropertyOf,NiceTag.isRelatedTo);
		//............................................................................................
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream("public/rdf/upyourmood.rdf");
			m.write(outStream, "RDF/XML-ABBREV");
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

		OntologyUpYourMood.Music = m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(0));
		OntologyUpYourMood.album = m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(1));
		OntologyUpYourMood.artiste = m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(2));
		OntologyUpYourMood.pochette = m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(3));
		OntologyUpYourMood.titre= m.createResource(OntologyUpYourMood.getUymMusic()+infoMusic.get(4));
		//OntologyUpYourMood.AlbumTitle = m.createProperty(OntologyUpYourMood.getUymMusic()+"AlbumTitle");

		m.add(OntologyUpYourMood.AlbumTitle, RDFS.subPropertyOf, DC.title);
		m.add(OntologyUpYourMood.Music,OntologyUpYourMood.AlbumTitle,OntologyUpYourMood.album);
		m.add(OntologyUpYourMood.Music,DC.creator,OntologyUpYourMood.artiste);
		m.add(OntologyUpYourMood.Music, FOAF.depiction,OntologyUpYourMood.pochette);
		m.add(OntologyUpYourMood.Music,DC.title,OntologyUpYourMood.titre);
		//m.add(OntologyUpYourMood.Music,RDF.type,OntologyUpYourMood.Music);

	}

	/**
	 * Dans cette fonction on ajoute:
	 * a minima:
	 * <ul>
	 * 	<li>le pseudo</li>
	 * </ul>
	 */
	private void ajouterUtilisateur(UserInformation userInfo, List<String> infoMusic ){

		try {
			userInfo.retrieveInformation(Application.maSession.getPseudo());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//OntologyUpYourMood.HasListen=m.createProperty(OntologyUpYourMood.getUymUser()+"HasListen");
		m.add(OntologyUpYourMood.HasListen,RDFS.subPropertyOf,FOAF.knows);
		OntologyUpYourMood.User = m.createResource(OntologyUpYourMood.getUymUser()+userInfo.getInfoUser().get(0))
								.addProperty(OntologyUpYourMood.HasListen, OntologyUpYourMood.getUymMusic()+infoMusic.get(0));
		OntologyUpYourMood.User.addProperty(NiceTag.makesMeFeel,
							    m.createResource()
							    .addProperty(OntologyUpYourMood.IsAssociatedBy, "test")
							    .addProperty(OntologyUpYourMood.IsConnoted, "test2"));
        		
        /*m.createResource()
        .addProperty(OntologyUpYourMood.IsAssociatedBy, OntologyUpYourMood.Word)
        .addProperty(OntologyUpYourMood.IsConnoted, OntologyUpYourMood.connotation));
		OntologyUpYourMood.User = m.createResource(OntologyUpYourMood.getUymUser()+userInfo.getInfoUser().get(0));

		m.add(OntologyUpYourMood.User,RDF.type,OntologyUpYourMood.User);
		m.add(OntologyUpYourMood.User,OntologyUpYourMood.HasListen,OntologyUpYourMood.Music);*/

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
