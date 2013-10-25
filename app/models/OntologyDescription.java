package models;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class OntologyDescription {
	public FileOutputStream rdf_file = null;
	private  OntModel m=null;

	private OntologyDescription(){
		
		m = ModelFactory.createOntologyModel ();
	}
	
	public void OwlDescription(){
		
		FileOutputStream fichierSortie = null;

		try {
			fichierSortie = new FileOutputStream (new File ("public/rdf/ontologyDescription.rdf"));
		}
		catch (FileNotFoundException ex) {
			Logger.getLogger (Main.class.getName ()).log (Level.SEVERE, null, ex);
		}
		
		constructionOfOWLOntology();
		m.write (fichierSortie,"RDF/XML-ABBREV");
		
	}
	
	private void constructionOfOWLOntology(){
		
		/****************************************************************************************************************
		 ****************************************************************************************************************
		 **************************************** AJOUT DES CLASSES *****************************************************
		 ****************************************************************************************************************
		 ****************************************************************************************************************/
		
		// Music
		OntClass musicClass = m.createClass(OntologyUpYourMood.getUymMusic());
		musicClass.addLabel("Music Class", "en");
		musicClass.addComment("A Super class of Musics info", "en");
		
		// User
		OntClass userClass = m.createClass(OntologyUpYourMood.getUymUser());
		userClass.addLabel("User Class", "en");
		userClass.addComment("A Super class of user's info", "en");
		
		// WordConnotation
		OntClass wordConnotationClass = m.createClass(OntologyUpYourMood.getUymWordConnotation());
		wordConnotationClass.addLabel("Word-Connotation Class","en");
		wordConnotationClass.addComment("A Super class of Word-Connotation info", "en");
		
		/****************************************************************************************************************
		 ****************************************************************************************************************
		 **************************************** AJOUT DES PROPRIETES **************************************************
		 ****************************************************************************************************************
		 ****************************************************************************************************************/
		
		// hasListen
		ObjectProperty hasListen = m.createObjectProperty(OntologyUpYourMood.getUymUser()+"hasListen");
		hasListen.addRange(userClass);
		hasListen.addLabel("A User has listen a music", "en");
		hasListen.addComment("Link a Music resource to a User", "en");
		
		// isConnoted
		ObjectProperty isConnoted = m.createObjectProperty(OntologyUpYourMood.getUymWordConnotation()+"isConnoted");
		isConnoted.addRange(wordConnotationClass);
		isConnoted.addLabel("A word is connoted positively or negatively ", "en");
		isConnoted.addComment("Link a connotation Literal to a word", "en");
		
		// isAssociatedBy
		ObjectProperty isAssociatedBy = m.createObjectProperty(OntologyUpYourMood.getUymWordConnotation()+"isAssociatedBy");
		isAssociatedBy.addRange(wordConnotationClass);
		isAssociatedBy.addLabel("A word is associate to a Music by a User", "en");
		isAssociatedBy.addComment("??", "en");
		
		// makesMeFeel
		ObjectProperty makesMeFeel = m.createObjectProperty(OntologyUpYourMood.getUymMusic()+"makesMeFeel");
		makesMeFeel.addRange(musicClass);
		makesMeFeel.addLabel("a Music provokes emotions to a User", "en");
		makesMeFeel.addComment("Link a User Resource to a Music", "en");
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
