package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe permettant de construire le fichier upyourmood.rdf.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 */
public class Optimizer
{
	public final String quoteCharacter="'";
	/**
	 * Contiendra l'ensemble des urls des images en rapport avec un mot précis.
	 */
	public ArrayList<String> urlImages;
	
	/**
	 * Constructeur qui instancie une ArrayList, qui contiendra les urls  des images.
	 */
	public Optimizer(){
		urlImages=new ArrayList<String>();
	}

	/**
	 * <p>Fonction permettant de vérifier si le mot entré par l'utilisateur a déjà été rentré 
	 * par quelqu'un d'autre. Cela permet d'éviter d'aller interroger Flickr inutilement, car si le mot
	 * a déjà été rentré, alors les urls associées sont déjà stockées dans la BDD.</p>
	 * @param mot Le mot à vérifier dans la BDD.
	 * @return vrai si le mot est présent dans la BDD, faux sinon.
	 */
	public boolean wordHasBeenAlreadyViewed(String mot){
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT * FROM \"Image\" WHERE \"motAssoc\"="+quoteCharacter+mot+quoteCharacter);
		try {
			if (!res.first()){
				//le mot n'a jamais été rentré par un utilisateur
				ConnectionBase.close();
				return false;
			}else{

				ConnectionBase.close();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ConnectionBase.close();
			return false;
		}
	}

	/**
	 * Fonction permettant d'insérer dans la BDD les urls des images récupérées.
	 * @param host Url de l'image
	 * @param motAssoc Mot associé à l'url
	 */
	public void updateAlreadyTraversed(String host,String motAssoc){
		//requête sur une vue qui rend seulement les urlFlickrWrappr
		//s'il n'y a rien renvoyer faux
		//sinon renoyer vrai
		ConnectionBase.open();
		ConnectionBase.requete("INSERT INTO \"Image\" (\"urlImage\",\"motAssoc\") VALUES " +
				"("+quoteCharacter+host+quoteCharacter+","+
				quoteCharacter+motAssoc+quoteCharacter+")");
		ConnectionBase.close();
	}

	/**
	 * Méthode qui récupère via la BDD les urls des images, pour ensuite les proposer à l'utilisateur.
	 * @param mot Mot rentré par l'utilisateur.
	 * @throws SQLException Echec de connexion à la BDD.
	 */
	public void prepareImage(String mot){
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT \"urlImage\" FROM \"Image\" WHERE \"motAssoc\"="+quoteCharacter+mot+quoteCharacter);
		try {
			while(res.next()){
				urlImages.add(res.getString("urlImage"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionBase.close();
	}

	/**
	 * Getter qui renvoie l'ArrayList contenant les urls des images.
	 * @return l'ArrayList urlImages.
	 */
	public ArrayList<String> urlImageToPrint(){
		return urlImages;
	}
}
