package models;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.openjena.riot.RiotException;

import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;

public class OntologyDescription {
	private static final String rdf_file = "public/rdf/ontologyDescription.rdf";
	private Model m=null;

	private OntologyDescription(){
		try{
			m=FileManager.get().loadModel(rdf_file);
		}catch(RiotException e){
			m = ModelFactory.createDefaultModel();
		}
	}
	
	public void OwlDescription(){
		ajouterPrefixe();
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(rdf_file);
			m.write(outStream, "RDF/XML");
			//m.write(outStream, "N3");
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void ajouterPrefixe(){
		m.setNsPrefix("rdf", RDF.getURI());
		m.setNsPrefix("rdfs", RDFS.getURI());
		m.setNsPrefix("foaf", FOAF.getURI());
		m.setNsPrefix("dc", DC.getURI());
		m.setNsPrefix("uym", OntologyUpYourMood.getUym());
	}
	
	
}
