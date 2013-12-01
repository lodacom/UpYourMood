package models;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.iri.impl.Main;
//import org.json.XML;
import org.openjena.riot.RiotException;

import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;

/**
 * Classe permettant de décrire l'ontologie UpYourMood créée par nos soins.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
public class OntologyDescription {
	private  OntModel m=null;

	/**
	 * Constructeur permettant de créer notre modèle pour décrire notre ontologie.
	 */
	public OntologyDescription(){

		m = ModelFactory.createOntologyModel ();
	}

	/**
	 * 
	 * @return le modèle sous le format "RDF/XML-ABBREV", consultable sous le lien "Notre ontologie".
	 */
	public String OwlDescription(){
		constructionOfOWLOntology();
		OutputStream out = new ByteArrayOutputStream();
		m.write (out,"RDF/XML-ABBREV");
		return out.toString();
	}

	/**
	 * Méthode qui ajoute au modèle toutes les informations nécessaires pour décrire notre ontologie.
	 */
	private void constructionOfOWLOntology(){

		/****************************************************************************************************************
		 ****************************************************************************************************************
		 **************************************** AJOUT DES CLASSES *****************************************************
		 ****************************************************************************************************************
		 ****************************************************************************************************************/

		// Music
		OntClass MusicClass = m.createClass(OntologyUpYourMood.getUymMusic());
		MusicClass.addLabel("Music Class", "en");
		MusicClass.addComment("A Super class of Musics info", "en");

		// User
		OntClass UserClass = m.createClass(OntologyUpYourMood.getUymUser());
		UserClass.addLabel("User Class", "en");
		UserClass.addComment("A Super class of user's info", "en");

		// WordConnotation
		OntClass WordConnotationClass = m.createClass(OntologyUpYourMood.getUymWordConnotation());
		WordConnotationClass.addLabel("Word-Connotation Class","en");
		WordConnotationClass.addComment("A Super class of Word-Connotation info", "en");

		// Album
		OntClass AlbumClass = m.createClass(OntologyUpYourMood.getUymMusic()+"album");
		AlbumClass.addLabel("Album Class","en");
		AlbumClass.addComment("Title of the Album that containing the Music", "en");

		// Artist
		OntClass ArtistClass = m.createClass(OntologyUpYourMood.getUymMusic()+"artist");
		ArtistClass.addLabel("Artist Class","en");
		ArtistClass.addComment("Name of the artist who composed the music", "en");

		// AlbumCover
		OntClass AlbumCoverClass = m.createClass(OntologyUpYourMood.getUymMusic()+"albumcover");
		AlbumCoverClass.addLabel("AlbumCover Class","en");
		AlbumCoverClass.addComment("AlbumCover of the Album", "en");

		// Title
		OntClass TitleClass = m.createClass(OntologyUpYourMood.getUymMusic()+"title");
		TitleClass.addLabel("Title Class","en");
		TitleClass.addComment("Title of the Music", "en");

		// MusicaExperience
		OntClass MusicalExperienceClass = m.createClass(OntologyUpYourMood.getUymMusic()+"musicalexperience");
		MusicalExperienceClass.addLabel("MusicalExperience Class","en");
		MusicalExperienceClass.addComment("A MusicalExperience groups the Music that the User is listening and the associated word / connotation ", "en");

		/****************************************************************************************************************
		 ****************************************************************************************************************
		 **************************************** AJOUT DES PROPRIETES **************************************************
		 ****************************************************************************************************************
		 ****************************************************************************************************************/

		// foaf:knows
		ObjectProperty foafKnows = m.createObjectProperty(FOAF.getURI()+"knows");
		
		// hasListen
		ObjectProperty hasListen = m.createObjectProperty(OntologyUpYourMood.getUymUser()+"hasListen");
		hasListen.addRange(MusicClass);
		hasListen.addDomain(UserClass);
		hasListen.addLabel("A User has listen a music", "en");
		hasListen.addComment("Link a Music resource to a User", "en");
		hasListen.addSuperProperty(foafKnows);
		
		// isConnoted
		ObjectProperty isConnoted = m.createObjectProperty(OntologyUpYourMood.getUymWordConnotation()+"isConnoted");
		isConnoted.addRange(WordConnotationClass);
		isConnoted.addDomain(WordConnotationClass);
		isConnoted.addLabel("A word is connoted positively or negatively ", "en");
		isConnoted.addComment("Link a connotation Literal to a word", "en");

		// isAssociatedBy
		ObjectProperty isAssociatedBy = m.createObjectProperty(OntologyUpYourMood.getUymWordConnotation()+"isAssociatedBy");
		isAssociatedBy.addRange(WordConnotationClass);
		isAssociatedBy.addDomain(MusicClass);
		isAssociatedBy.addLabel("A word is associate to a Music by a User", "en");
		isAssociatedBy.addComment("??", "en");
		
		// isRelatedTo
		ObjectProperty isRelatedTo = m.createObjectProperty(NiceTag.getURI()+"isRelatedTo");
		
		// makesMeFeel
		ObjectProperty makesMeFeel = m.createObjectProperty(OntologyUpYourMood.getUymMusic()+"makesMeFeel");
		makesMeFeel.addRange(WordConnotationClass);
		makesMeFeel.addDomain(MusicClass);
		makesMeFeel.addLabel("a Music provokes emotions to a User", "en");
		makesMeFeel.addComment("Link a User Resource to a Music", "en");
		makesMeFeel.addSuperProperty(isRelatedTo);
		
		// dc:title
		ObjectProperty dcTitle = m.createObjectProperty(DC.getURI()+"title");
		
		// albumTitle
		ObjectProperty albumTitle = m.createObjectProperty(OntologyUpYourMood.getUymMusic()+"albumTitle");
		albumTitle.addRange(AlbumClass);
		albumTitle.addDomain(MusicClass);
		albumTitle.addLabel("a Music is in an Album", "en");
		albumTitle.addComment("Link a Album Resource to a Music", "en");
		albumTitle.addSuperProperty(dcTitle);
		
		// hasMusicalExperience
		ObjectProperty hasMusicalExperience = m.createObjectProperty(OntologyUpYourMood.getUymUser()+"hasMusicalExperience");
		hasMusicalExperience.addRange(MusicalExperienceClass);
		hasMusicalExperience.addDomain(UserClass);
		hasMusicalExperience.addLabel("A User has an Musical Experience", "en");
		hasMusicalExperience.addComment("Link a MusicalExperience Resource to a User", "en");

		// songTitle
		ObjectProperty songTitle = m.createObjectProperty(OntologyUpYourMood.getUymMusic()+"songTitle");
		songTitle.addRange(TitleClass);
		songTitle.addDomain(MusicClass);
		songTitle.addLabel("A Music has a Title", "en");
		songTitle.addComment("Link a Title Resource to a Music", "en");
		songTitle.addSuperProperty(dcTitle);
		
		// DC.creator
		ObjectProperty creator = m.createObjectProperty(DC.getURI()+"creator");
		creator.addRange(ArtistClass);
		creator.addDomain(MusicClass);
		creator.addLabel("A Music is made by an Artist ( Use of the Property DC:creator )", "en");
		creator.addComment("Link a Artist Resource to a Music", "en");
		
		// FOAF:depiction
		ObjectProperty foafDepiction = m.createObjectProperty(FOAF.getURI()+"depiction");
		foafDepiction.addRange(AlbumCoverClass);
		foafDepiction.addDomain(MusicClass);
		foafDepiction.addLabel("A Music has an AlbumCover ( Use of the Property FOAF:depiction )", "en");
		foafDepiction.addComment("Link a AlbumCover Resource to a Music", "en");
		
	}


	/*private void ajouterPrefixe(){
		m.setNsPrefix("rdf", RDF.getURI());
		m.setNsPrefix("rdfs", RDFS.getURI());
		m.setNsPrefix("foaf", FOAF.getURI());
		m.setNsPrefix("dc", DC.getURI());
		m.setNsPrefix("uym", OntologyUpYourMood.getUym());
	}*/

	public static void main(String args[]){
		OntologyDescription ds = new OntologyDescription();
		ds.OwlDescription();
	}


}
