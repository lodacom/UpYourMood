package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * <p>Classe permettant de récupérer les urls des images correspondant à un mot, en
 * interrogeant via des requêtes SPARQL DBPedia.</p>
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
public class DBpediaQueries {
	public String service="http://dbpedia.org/sparql";
	public String dbprop="PREFIX dbpprop: <http://dbpedia.org/property/>";
	public String rdfs="PREFIX rdfs: <"+RDFS.getURI()+">";
	public final String NL = System.getProperty("line.separator");
	private QueryExecution query;
	private ArrayList<String> urlListe;
	private Optimizer Opti=new Optimizer();
	private ArrayList<String> urlImages;
	private String mot;
	
	/**
	 * Méthode qui permet d'interroger DBPedia pour avoir des urls pointant vers des "fermes" d'images de Flickr.
	 * @param mot Le mot pour lequel on veut récupérer les images.
	 * @param lang La langue appartenant au mot entré. (Pour avoir plus de pertinence)
	 * @see DBpediaQueries#urlFromDBpedia()
	 */
	public void queryImage(String mot,String lang){
		this.mot=mot;
		urlImages=new ArrayList<String>();
		String etape1=dbprop + NL + rdfs + NL +
				"SELECT ?img "+
				"WHERE { "+
				"?res dbpprop:hasPhotoCollection ?img . "+
				"?res rdfs:label ?label "+
				"FILTER (regex(?label, \"^"+mot+".+\",\"i\") && lang(?label)=\""+lang+"\") " + 
				"} " +
				"LIMIT 2 ";
		Query etape2 = QueryFactory.create(etape1);
		query = QueryExecutionFactory.sparqlService(service, etape2.toString());
		urlFromDBpedia();
	}

/**
 * Récupération des informations de la requête effectuée dans la méthode précédente.
 * @see DBpediaQueries#queryImage(String, String)
 */
	private void urlFromDBpedia(){
		urlListe=new ArrayList<String>();
		try {
			ResultSet results = query.execSelect();
			while(results.hasNext()) {
				QuerySolution sol = (QuerySolution) results.next();
				String url=sol.get("?img").toString();
				urlListe.add(url);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			query.close();
		}
		retreiveImages();
	}

	/**
	 * Méthode qui :
	 * <ul>
	 *  <li>Récupère les pages correspondantes à l'url récupérée dans la méthode précédente</li>
	 *  <li>Parse la page afin de trouver les urls des images</li>
	 * </ul>
	 */
	private void retreiveImages(){
		for (int i=0;i<urlListe.size();i++){

			DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
			HttpGet httpget = new HttpGet(urlListe.get(i));
			try{
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();

				InputStream inputStream=entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"), 8);
				String line = null;
				String recup = null;
				String[] images =null;
				String urlImage=null;
				int j=1;
				while ((line = reader.readLine()) != null){
					if (line.matches("<p><a .+</p>")){
						recup=line;
						images=recup.split("<img src=\"");
						while(j<images.length){
							urlImage=images[j].replaceAll("\"/>.+", "");
							//urlImages.add(urlImage);
							Opti.updateAlreadyTraversed( urlImage, this.mot);
							j++;
						}
						j=1;
					}
				}
			}catch(IOException e){

			}
		}
	}

	/**
	 * Getter pour l'ArrayList urlImages.
	 * @return les urls présentes dans l'ArrayList.
	 */
	public ArrayList<String> urlImagesReturn(){
		return urlImages;
	}

}
