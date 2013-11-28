package models;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.jena.riot.RiotException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;


public class RDFBuildingColors {
	
	private static RDFBuildingColors instance;
	private static Model m = null;
	private static final String rdf_filecolors = "public/rdf/uymcolors.rdf";
	private static final String color=OntologyUpYourMood.getUym()+"color/";
	
	private static Property isColoredBy =null;
	private static Property givenBy =null;
	private static Property isSelected =null;
	private static Property hasValue =null;
	
	private static String prefixs = null;
	
	private Resource Music=null;
	private Resource User =null;
	private Resource Color =null;
	
	private RDFBuildingColors(){
		try{
			m=FileManager.get().loadModel(rdf_filecolors);
		}catch(RiotException e){
			m = ModelFactory.createDefaultModel();
		}		
	}
	
	public final static RDFBuildingColors getInstance() {
		if (RDFBuildingColors.instance == null) {
			synchronized(RDFBuildingColors.class) {
				if (RDFBuildingColors.instance == null) {
					RDFBuildingColors.instance = new RDFBuildingColors();
					m.setNsPrefix("uym", OntologyUpYourMood.getUym());
					m.setNsPrefix("music", OntologyUpYourMood.getUymMusic());
					m.setNsPrefix("user", OntologyUpYourMood.getUymUser());
					m.setNsPrefix("rdf", RDF.getURI());
					m.setNsPrefix("color", color);
					isColoredBy =m.createProperty(OntologyUpYourMood.getUymMusic()+"isColoredBy");
					givenBy =m.createProperty(OntologyUpYourMood.getUymUser()+"givenBy");
					isSelected =m.createProperty(color+"isSelected");
					hasValue =m.createProperty(color+"hasValue");
					String NL = System.getProperty("line.separator");
					prefixs = "PREFIX uym: <"+OntologyUpYourMood.getUym()+">"+NL+"PREFIX music: <"+OntologyUpYourMood.getUymMusic()+">"+NL+"PREFIX user: <"+OntologyUpYourMood.getUymUser()+">"+NL+"PREFIX color: <"+color+">"+NL;
					
				}
			}
		}
		return RDFBuildingColors.instance;
	}
	
	public void rdfUYMAddColor(String music_number, String pseudo, String color_value){
		if (!this.incrExistColor(music_number, pseudo, color_value)){
			this.addColor(music_number, pseudo, color_value);
		}
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream("public/rdf/uymcolors.rdf");
			m.write(outStream, "RDF/XML-ABBREV");
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private Boolean incrExistColor(String music_number, String pseudo, String color_value){
		Resource Color = m.getResource(color+music_number+"/"+pseudo+"/"+color_value);
		Property prop = m.getProperty(color+"isSelected");
		Statement stmt = m.getProperty(Color, prop);
		
		if (stmt!=null){
			RDFNode object = stmt.getObject();
			stmt.changeLiteralObject(object.asLiteral().getInt()+1);
			return true;
		}
		
		return false;
	}
	
	private void addColor(String music_number, String pseudo, String color_value){
		Music = m.createResource(OntologyUpYourMood.getUymMusic()+music_number);
		User = m.createResource(OntologyUpYourMood.getUymUser()+pseudo);
		Color = m.createResource(color+music_number+"/"+pseudo+"/"+color_value);
		m.add(Music, isColoredBy, Color);
		m.add(Color, givenBy, User);
		m.add(m.createLiteralStatement(Color,  isSelected, 1));
		m.add(m.createLiteralStatement(Color,  hasValue, color_value));
	}
	
	public String getMaxColorMusic(String music_number){
		String maxcolor = "#FFFFFF";
		Integer value = 0;
		String music = "<"+OntologyUpYourMood.getUymMusic()+music_number+">";
		
		String str = prefixs + 
				"SELECT ?value (SUM(?times) AS ?t) WHERE {"+
				music+" music:isColoredBy ?c ."+
				"?c color:hasValue ?value ."+
				"?c color:isSelected ?times ."+
				"} GROUP BY ?value";
		Query query = QueryFactory.create(str);
		QueryExecution execquery = QueryExecutionFactory.create(query, m);
		ResultSet rs = execquery.execSelect() ;
        while (rs.hasNext())
        { 
            	QuerySolution s = rs.nextSolution();
            	Integer v = s.get("?t").asLiteral().getInt();
            	if (v > value){
            		value = v;
            		maxcolor = s.get("?value").asLiteral().getString();
            	}
        }
		execquery.close();
		return maxcolor;
	}
	
	public String getMaxColorMusicByUser(String music_number, String pseudo){
		String maxcolor = "#FFFFFF";
		Integer value = 0;
		String music = "<"+OntologyUpYourMood.getUymMusic()+music_number+">";
		String user = "<"+OntologyUpYourMood.getUymUser()+pseudo+">";
		
		String str = prefixs + 
				"SELECT ?value ?times WHERE {"+
				music+" music:isColoredBy ?c ."+
				"?c color:hasValue ?value ."+
				"?c color:isSelected ?times ."+
				"?c user:givenBy "+ user +" ."+
				"}";
		Query query = QueryFactory.create(str);
		QueryExecution execquery = QueryExecutionFactory.create(query, m);
		ResultSet rs = execquery.execSelect() ;
        while (rs.hasNext())
        { 
            	QuerySolution s = rs.nextSolution();
            	Integer v = s.get("?times").asLiteral().getInt();
            	if (v > value){
            		value = v;
            		maxcolor = s.get("?value").asLiteral().getString();
            	}
        }
		execquery.close();
		return maxcolor;
	}
}