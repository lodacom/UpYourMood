package models;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class OntologyUpYourMood {
	public static final String uym="http://www.upyourmood.com/";

	public static Model m = ModelFactory.createDefaultModel();
	
	public static final Property IsConnoted = m.createProperty(uym+"IsConnoted");
	public static final Property IsAssociatedBy = m.createProperty(uym+"IsAssociatedBy");

	public static Resource Word = null;
	
	public static String getURI(){
		return uym;
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
