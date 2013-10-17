package models;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.vocabulary.RDFS;

public class DBpediaQueries {
	public String service="http://dbpedia.org/sparql";
	public String dbprop="PREFIX dbpprop: <http://dbpedia.org/property/>";
	public String rdfs="PREFIX rdfs: <"+RDFS.getURI()+">";
	public final String NL = System.getProperty("line.separator");
	QueryExecution query;
	public JsonNode node;
	
	@SuppressWarnings("deprecation")
	public void queryImage(String mot){
		String etape1=dbprop + NL + rdfs + NL +
		"SELECT ?img "+
		"WHERE { "+
		"?res dbpprop:hasPhotoCollection ?img . "+
		"?res rdfs:label ?label "+
		"FILTER regex(?label, \"^"+mot+".\",\"i\") " +
		"LIMIT 10"+ 
		"}";
		System.out.println("Avant requête");
		Query etape2 = QueryFactory.create(etape1);
		etape1=URLEncoder.encode(etape2.toString());
		service+=etape1+"&format=json";
		urlFromDBpedia();
	}
	
	private void urlFromDBpedia(){
		String resultat=null;
		try {
			resultat=JSONLoader.loadTracksJSON(service);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		node=Json.parse(resultat);
		List<JsonNode> liste=node.findValues("img");
		for (JsonNode element :liste){
			System.out.println(element.asText());
		}
	}
	
}
