package models;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager
{
	public static void getFile(String urlFlickrWrappr,String host)
	{
		
		InputStream input = null;
		FileOutputStream writeFile = null;

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
			e.printStackTrace();
		}
		finally{
			try{
				writeFile.close();
				input.close();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * Fonction permettant de regarder si l'image a déjà été téléchargé via la BD.
	 * Si ce n'est pas le cas rajoute dans la BD et renvoie faux.
	 * @param host url de l'image
	 */
	public void hasBeenAlreadyDownload(String urlFlickrWrappr,String host){
		//requête sur une vue qui rend seulement les hosts
		//s'il n'y a rien mettre dans la BD urlFlickrWrappr|host|nom image
		//renvoyer faux
		//sinon renvoyer vrai
	}
	
	/**
	 * Fonction permettant de savoir si les images de l'url en paramètre
	 * ont déjà été téléchargées. Si ce n'est pas le cas renvoie faux.
	 * @param urlFlickrWrappr
	 */
	public void hasBeenAlreadyTraversed(String urlFlickrWrappr){
		//requête sur une vue qui rend seulement les urlFlickrWrappr
		//s'il n'y a rien renvoyer faux
		//sinon renoyer vrai
	}
}
