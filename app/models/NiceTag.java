package models;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * Classe implémentant les informations relatives à l'ontologie NiceTag.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */

public class NiceTag {
	public static final String nicetag="http://ns.inria.fr/nicetag/2010/09/09/voc.html#";
	
	/**
	 * Méthode simple jouant le rôle de "getter".
	 * @return l'uri de nicetag.
	 */
	public static String getURI(){
		return nicetag;
	}
	
	public static Model m = ModelFactory.createDefaultModel();
	
	public static final Property makesMeFeel=m.createProperty(nicetag,"makesMeFeel");
	public static final Property isRelatedTo=m.createProperty(nicetag,"isRelatedTo");
}
