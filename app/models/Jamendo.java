package models;

import java.io.IOException;
import java.util.*;

import org.apache.http.client.ClientProtocolException;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class Jamendo {
	static final String TRACKS_URL = "http://api.jamendo.com/v3.0/tracks/?format=json&groupby=artist_id&limit=all";
	static final String CLIENT_ID = "59336032";
	public JsonNode node;
	public List<String> idMusiques;
	private List<String> artistes;
	private List<String> albums;
	private List<String> titres;
	private List<String> pochetteAlbums;
	public int compteur=0;
	/*
	 * artist_name
	 * album_name
	 * name -> correspond au titre de la chanson
	 * album_image
	 * audio
	 */
	
	public void play() throws ClientProtocolException, IOException{
		String result=JSONLoader.loadTracksJSON( TRACKS_URL +"&client_id="+CLIENT_ID);
		node=Json.parse(result);
		listIdMusique();
		listAlbums();
		listArtistes();
		listPochetteAlbum();
		listTitre();
	}
	
	public String next(){
		if (compteur<idMusiques.size()-1){
			compteur++;
			return idMusiques.get(compteur);
		}else{
			try {
				play();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			compteur=0;
			return idMusiques.get(0);
		}
	}
	
	public String previous(){
		if (compteur>0){
			compteur--;
			return idMusiques.get(compteur);
		}else{
			try {
				play();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			compteur=0;
			return idMusiques.get(0);
		}
	}
	
	public String current(){
		return idMusiques.get(compteur);
	}
	
	public List<String> currentInfo(){
		List<String> info=new ArrayList<String>();
		info.add(idMusiques.get(compteur));
		info.add(albums.get(compteur));
		info.add(artistes.get(compteur));
		info.add(pochetteAlbums.get(compteur));
		info.add(titres.get(compteur));
		return info;
	}
	
	private void listArtistes(){
		List<JsonNode> liste=node.findValues("artist_name");
		artistes=new ArrayList<String>();
		for (JsonNode element :liste){
			artistes.add(element.asText());
		}
	}
	
	private void listAlbums(){
		List<JsonNode> liste=node.findValues("album_name");
		albums=new ArrayList<String>();
		for (JsonNode element :liste){
			albums.add(element.asText());
		}
	}
	
	private void listTitre(){
		List<JsonNode> liste=node.findValues("name");
		titres=new ArrayList<String>();
		for (JsonNode element :liste){
			titres.add(element.asText());
		}
	}
	
	private void listPochetteAlbum(){
		List<JsonNode> liste=node.findValues("album_image");
		pochetteAlbums=new ArrayList<String>();
		for (JsonNode element :liste){
			pochetteAlbums.add(element.asText());
		}
	}
	
	/*public String listMusique(){
		List<JsonNode> liste=node.findValues("audio");
		String musiques="";
		for (JsonNode element :liste){
			musiques+=element.asText()+" ";
		}
		return musiques;
	}*/
	
	private void listIdMusique(){
		idMusiques=new ArrayList<String>();
		List<JsonNode> liste=node.findValues("id");
		for (JsonNode element :liste){
			idMusiques.add(element.asText());
		}
	}
}
