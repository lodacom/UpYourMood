package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Optimizer
{
	public final String quoteCharacter="'";
	public ArrayList<String> urlImages;
	
	public Optimizer(){
		urlImages=new ArrayList<String>();
	}

	/**
	 * Fonction permettant de regarder si l'image a déjà été téléchargé via la BD.
	 * Si ce n'est pas le cas rajoute dans la BD et renvoie faux.
	 * @param urlFlickrWrappr url du farm
	 * @param host url de l'image
	 */
	public boolean wordHasBeenAlreadyViewed(String mot){
		//requête sur une vue qui rend seulement les hosts
		//s'il n'y a rien mettre dans la BD urlFlickrWrappr|host|nom image
		//renvoyer faux
		//sinon renvoyer vrai
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
	 * Fonction permettant de savoir si les images de l'url en paramètre
	 * ont déjà été téléchargées. Si ce n'est pas le cas renvoie faux.
	 * @param urlFlickrWrappr
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

	public ArrayList<String> urlImageToPrint(){
		return urlImages;
	}
}
