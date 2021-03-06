package models;

import java.io.IOException;
import java.util.*;
import org.apache.http.client.ClientProtocolException;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;

/**
 * Classe permettant de récupérer les informations des musiques à partir de Jamendo, et donc de les écouter.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 */
public class Jamendo {
	static final String TRACKS_URL = "http://api.jamendo.com/v3.0/tracks/?format=json&groupby=artist_id&limit=all";
	static final String CLIENT_ID = "59336032";
	public JsonNode node;
	public List<String> idMusiques;
	private List<String> artistes;
	private List<String> albums;
	private List<String> titres;
	private List<String> pochetteAlbums;
	
	/**
	 * Constructeur.
	 */
	public Jamendo(){
		 idMusiques=new ArrayList<String>();
		 artistes=new ArrayList<String>();
		 albums=new ArrayList<String>();
		 titres=new ArrayList<String>();
		 pochetteAlbums=new ArrayList<String>();
		 try {
			play();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode qui permet de parser les informations renvoyées par Jamendo et de les stocker.
	 * @throws ClientProtocolException
	 * @throws IOException
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
	
	/**
	 * Méthode qui implémente le comportement du bouton "next", pour changer de musique.
	 * @return l'identifiant de la musique suivante.
	 */
	public String next(){
		String recupCompteur=Controller.session("compteur");
		int compteur=Integer.parseInt(recupCompteur);
		if (compteur<idMusiques.size()-1){
			compteur++;
			Controller.session("compteur",String.valueOf(compteur));
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
			Controller.session("compteur",String.valueOf(compteur));
			return idMusiques.get(0);
		}
	}
	
	/**
	 * Méthode qui implémente le comportement du bouton "previous", pour changer de musique.
	 * @return l'identifiant de la musique précédente.
	 */
	public String previous(){
		String recupCompteur=Controller.session("compteur");
		int compteur=Integer.parseInt(recupCompteur);
		if (compteur>0){
			compteur--;
			Controller.session("compteur",String.valueOf(compteur));
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
			Controller.session("compteur",String.valueOf(compteur));
			return idMusiques.get(0);
		}
	}
	
	/**
	 * Méthode qui permet de récupérer l'identifiant de la musique en écoute.
	 * @return l'identifiant de la musique en écoute.
	 */
	public String current(){
		String recupCompteur=Controller.session("compteur");
		int compteur=Integer.parseInt(recupCompteur);
		return idMusiques.get(compteur);
	}
	
	/**
	 * Méthode qui permet de récupérer toutes les informations de la musique en écoute.
	 * @return toutes les informations de la musique en écoute.
	 */
	public List<String> currentInfo(){
		String recupCompteur=Controller.session("compteur");
		int compteur=Integer.parseInt(recupCompteur);
		List<String> info=new ArrayList<String>();
		info.add(idMusiques.get(compteur));
		info.add(albums.get(compteur));
		info.add(artistes.get(compteur));
		info.add(pochetteAlbums.get(compteur));
		info.add(titres.get(compteur));
		return info;
	}
	
	/**
	 * Informations de l'artiste.
	 */
	private void listArtistes(){
		List<JsonNode> liste=node.findValues("artist_name");
		for (JsonNode element :liste){
			artistes.add(element.asText());
		}
	}
	
	/**
	 * Informations de l'album ( Son titre).
	 */
	private void listAlbums(){
		List<JsonNode> liste=node.findValues("album_name");
		for (JsonNode element :liste){
			albums.add(element.asText());
		}
	}
	
	/**
	 * Titre de la musique.
	 */
	private void listTitre(){
		List<JsonNode> liste=node.findValues("name");
		for (JsonNode element :liste){
			titres.add(element.asText());
		}
	}
	
	/**
	 * L'image de la pochette d'album.
	 */
	private void listPochetteAlbum(){
		List<JsonNode> liste=node.findValues("album_image");
		for (JsonNode element :liste){
			pochetteAlbums.add(element.asText());
		}
	}
	
	/**
	 * L'identifiant de la musique.
	 */
	private void listIdMusique(){
		List<JsonNode> liste=node.findValues("id");
		for (JsonNode element :liste){
			idMusiques.add(element.asText());
		}
	}
}
