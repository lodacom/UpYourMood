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


	public static Model m = ModelFactory.createDefaultModel();
	
	// Property
	
	public static Property IsConnoted =null; //m.createProperty(uym+"IsConnoted");
	public static Property IsAssociatedBy =null; //m.createProperty(uym+"IsAssociatedBy");
	public static Property AlbumTitle=null;
	public static Property HasListen=null;
			
	// Resource
	
	public static Resource Word = null;
	public static Resource Music=null;
	public static Resource User =null;
	public static Resource album=null; 
	public static Resource artiste=null; 
	public static Resource pochette=null; 
	public static Resource titre=null;
	
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
