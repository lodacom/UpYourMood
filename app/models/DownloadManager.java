package models;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DownloadManager
{
	public final String quoteCharacter="'";

	public void getFile(String urlFlickrWrappr,String host)
	{

		InputStream input = null;
		FileOutputStream writeFile = null;

		if (!hasBeenAlreadyDownload(urlFlickrWrappr, host)){
			try
			{
				URL url = new URL(host);
				URLConnection connection = url.openConnection();
				int fileLength = connection.getContentLength();

				if (fileLength == -1){
					System.out.println("Invalid URL or file.");
				}

				input = connection.getInputStream();
				String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
				writeFile = new FileOutputStream("public/downloadImages/"+fileName);
				byte[] buffer = new byte[1024];
				int read;

				while ((read = input.read(buffer)) > 0)
					writeFile.write(buffer, 0, read);
				writeFile.flush();
			}
			catch (IOException e){
				System.out.println("Error while trying to download the file.");
			}
			finally{
				try{
					writeFile.close();
					input.close();
				}
				catch (IOException e){

				}
			}
		}
	}

	/**
	 * Fonction permettant de regarder si l'image a déjà été téléchargé via la BD.
	 * Si ce n'est pas le cas rajoute dans la BD et renvoie faux.
	 * @param urlFlickrWrappr url du farm
	 * @param host url de l'image
	 */
	public boolean hasBeenAlreadyDownload(String urlFlickrWrappr,String host){
		//requête sur une vue qui rend seulement les hosts
		//s'il n'y a rien mettre dans la BD urlFlickrWrappr|host|nom image
		//renvoyer faux
		//sinon renvoyer vrai
		String fileName = null;
		ConnectionBase.open();
		try {
			URL url = new URL(host);
			fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
		}

		if (fileName==null){
			fileName="";
		}
		ResultSet res=ConnectionBase.requete("SELECT * FROM \"UrlImage\" WHERE \"urlImage\"="+quoteCharacter+host+quoteCharacter);
		try {
			if (!res.first()){
				//l'image n'a pas encore été dl on insert dans la table
				ConnectionBase.requete("INSERT INTO \"Image\" (\"urlFlickr\",\"urlImage\",\"nomImage\") VALUES " +
						"("+quoteCharacter+urlFlickrWrappr+quoteCharacter+","+
						quoteCharacter+host+quoteCharacter+","+
						quoteCharacter+fileName+quoteCharacter+")");
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
	public boolean hasBeenAlreadyTraversed(String urlFlickrWrappr){
		//requête sur une vue qui rend seulement les urlFlickrWrappr
		//s'il n'y a rien renvoyer faux
		//sinon renoyer vrai
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT * FROM \"UrlFlickr\" WHERE \"urlFlickr\"="+quoteCharacter+urlFlickrWrappr+quoteCharacter);
		try {
			if (!res.first()){
				//on a pas encore dl toutes les images de cette url
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
}
