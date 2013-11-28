package models;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class OntologyUpYourMood {
	private static final String uym="http://www.upyourmood.com/";
	private static final String uymMusic=uym+"music/";
	private static final String uymWordConnotation=uym+"wordconnotation/";
	private static final String uymUser=uym+"user/";
	private static final String uymColor=uym+"color/";



	public static Model m = ModelFactory.createDefaultModel();
	
	// Property
	
	public static Property isConnoted =m.createProperty(uymWordConnotation+"isConnoted");
	public static Property isAssociatedBy =m.createProperty(uymWordConnotation+"isAssociatedBy");
	public static Property albumTitle=m.createProperty(uymMusic+"albumTitle");
	public static Property hasListen=m.createProperty(uymUser+"hasListen");
	public static Property hasMusicalExperience=m.createProperty(uymUser+"hasMusicalExperience");
	public static Property songTitle=m.createProperty(uymMusic+"songTitle");
	public static Property makesMeThink=m.createProperty(uymWordConnotation+"makesMeThink");
	
	// Property pour les couleurs
	public static Property isColoredBy =m.createProperty(uymColor+"isColoredBy");
	public static Property givenBy =m.createProperty(uymColor+"givenBy");
	public static Property isSelected =m.createProperty(uymColor+"isSelected");
	public static Property hasValue =m.createProperty(uymColor+"hasValue");

	// Resource
	
	public static Resource Word = null;
	public static Resource Music=null;
	public static Resource User =null;
	public static Resource Album=null; 
	public static Resource Artist=null; 
	public static Resource AlbumCover=null; 
	public static Resource Title=null;
	public static Resource MusicalExperience=null;
	
	// Resource pour les couleurs 
	public static Resource Color =null;
	
	// Litteral
	
	public static Literal connotation=null;
	
	// Prefixes
	
	public static String getUym(){
		return uym;
	}
	
	public static String getUymMusic(){
		return uymMusic;
	}
	
	public static String getUymWordConnotation(){
		return uymWordConnotation;
	}
	
	public static String getUymUser(){
		return uymUser;
	}
	
	public static String getUymColor(){
		return uymColor;
	}
/*
 * PREFIX dbpprop: <http://dbpedia.org/property/>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
SELECT ?img
WHERE {
    ?res dbpprop:hasPhotoCollection ?img .
    ?res rdfs:label ?label
    FILTER regex(?label, "^mot_utilisateur.","i") 
}
Merci de ne pas enlever la requête ci-dessus
 */
}
