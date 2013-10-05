package models;

import java.io.IOException;
import java.util.*;

import org.apache.http.client.ClientProtocolException;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class Jamendo {
	static final String TRACKS_URL = "http://api.jamendo.com/v3.0/tracks/?format=json&groupby=artist_id";
	static final String CLIENT_ID = "b6747d04";
	public JsonNode node;
	public List<String> idMusiques;
	public List<String> pochettesAlbums;
	public int compteur=-1;
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
	}
	
	public String next(){
		compteur++;
		return idMusiques.get(compteur);
	}
	
	public String previous(){
		if (compteur!=0){
			compteur--;
			return idMusiques.get(compteur);
		}else{
			return idMusiques.get(idMusiques.size()-1);
		}
	}
	
	public String listArtistes(){
		List<JsonNode> liste=node.findValues("artist_name");
		String artistes="";
		for (JsonNode element :liste){
			artistes+=element.asText()+" ";
		}
		return artistes;
	}
	
	public String listAlbums(){
		List<JsonNode> liste=node.findValues("album_name");
		String albums="";
		for (JsonNode element :liste){
			albums+=element.asText()+" ";
		}
		return albums;
	}
	
	public String listTitre(){
		List<JsonNode> liste=node.findValues("name");
		String titres="";
		for (JsonNode element :liste){
			titres+=element.asText()+" ";
		}
		return titres;
	}
	
	public List<String> listPochetteAlbum(){
		List<JsonNode> liste=node.findValues("album_image");
		pochettesAlbums=new ArrayList<String>();
		for (JsonNode element :liste){
			pochettesAlbums.add(element.asText());
		}
		return pochettesAlbums;
	}
	
	public String listMusique(){
		List<JsonNode> liste=node.findValues("audio");
		String musiques="";
		for (JsonNode element :liste){
			musiques+=element.asText()+" ";
		}
		return musiques;
	}
	
	public void listIdMusique(){
		idMusiques=new ArrayList<String>();
		List<JsonNode> liste=node.findValues("id");
		for (JsonNode element :liste){
			idMusiques.add(element.asText());
		}
	}
}
