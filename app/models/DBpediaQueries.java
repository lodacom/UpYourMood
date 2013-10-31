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

public class DBpediaQueries {
	public String service="http://dbpedia.org/sparql";
	public String dbprop="PREFIX dbpprop: <http://dbpedia.org/property/>";
	public String rdfs="PREFIX rdfs: <"+RDFS.getURI()+">";
	public final String NL = System.getProperty("line.separator");
	private QueryExecution query;
	private ArrayList<String> urlListe;
	public ArrayList<String> urlTraversed;
	private DownloadManager DM=new DownloadManager();
	
	public void queryImage(String mot,String lang){
		String etape1=dbprop + NL + rdfs + NL +
				"SELECT ?img "+
				"WHERE { "+
				"?res dbpprop:hasPhotoCollection ?img . "+
				"?res rdfs:label ?label "+
				"FILTER (regex(?label, \"^"+mot+".+\",\"i\") && lang(?label)=\""+lang+"\") " + 
				"}" +
				"LIMIT 2";
		Query etape2 = QueryFactory.create(etape1);
		query = QueryExecutionFactory.sparqlService(service, etape2.toString());
		urlFromDBpedia();
	}

	private void urlFromDBpedia(){
		urlListe=new ArrayList<String>();
		urlTraversed=new ArrayList<String>();
		try {
			ResultSet results = query.execSelect();
			while(results.hasNext()) {
				QuerySolution sol = (QuerySolution) results.next();
				String url=sol.get("?img").toString();
				if (!DM.hasBeenAlreadyTraversed(url)){
					urlListe.add(url);
				}else{
					urlTraversed.add(url);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			query.close();
		}
		if (urlListe.isEmpty()){
			DM.prepareImage(urlTraversed);
		}else{
			retreiveImages();
		}
	}

	private void retreiveImages(){
		for (int i=0;i<urlListe.size();i++){

			DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
			HttpGet httpget = new HttpGet(urlListe.get(i));
			try{
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();

				InputStream inputStream=entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"), 8);
				//StringBuilder sb = new StringBuilder();
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
							DM.getFile(urlListe.get(i),urlImage);
							j++;
						}
						j=1;
					}
				}
			}catch(IOException e){

			}
		}
	}



}
