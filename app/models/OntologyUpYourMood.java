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

}
