package models;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

public class NiceTag {
	public static final String nicetag="http://ns.inria.fr/nicetag/2010/09/09/voc.html#";
	
	public static String getURI(){
		return nicetag;
	}
	
	public static Model m = ModelFactory.createDefaultModel();
	
	public static final Property makesMeFeel=m.createProperty(nicetag,"makesMeFeel");
	public static final Property isRelatedTo=m.createProperty(nicetag,"isRelatedTo");
}
