package models;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URLEncoder; 

/*
 * Classe utilisée pour charger les données sur la web-page (dans notre cas API: http://api.wordreference.com).
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA BEL Katsiaryna, SEGUIN Tristan
 */
public class ReadURL {
	//_url : attribut correspondent à la page URL de chargement
    String _url;

    public ReadURL(String _url){
        this._url = _url;
    }
/*
 * Méthode permettant de récupérer les informations de type text sur la web-page.
 * @return les informations sous format String de la web-page.
 * @throws MalformedURLException
 * @throws IOException
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA BEL Katsiaryna, SEGUIN Tristan
 */
    public String getURLContent() {
        String inputLine=new String();
        StringBuilder str = new StringBuilder(); //variable a remplir avec les donnes de la page
        try {
        	URL mURL = new URL(this._url); //initialisation d'objet de type URL pour se connecter vers la page web
            BufferedReader in;  //objet de lecture de donnees de type text de la text-input flux
            in = new BufferedReader(new InputStreamReader(mURL.openStream()));//ouverture de web-flux
            while ((inputLine = in.readLine()) != null) {//des q'il y a les données sur la ligne de flux
                str.append(inputLine);//on les ajoute sur le format text
            }
            in.close();//fermeture de flux
        } catch (MalformedURLException ex) {
            Logger.getLogger(ReadURL.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
                Logger.getLogger(ReadURL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str.toString();
    }
    
/*
 * Méthode permettant de récupérer la taille de l'information de type text sur la web-page.
 * @return la taille sous format Integer de la web-page.
 * @throws MalformedURLException
 * @throws IOException
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA BEL Katsiaryna, SEGUIN Tristan
*/    
    public Integer getURLSize() {
        String inputLine=new String();
        Integer size = 0;
        try {
        	URL mURL = new URL(this._url);
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(mURL.openStream()));
            while ((inputLine = in.readLine()) != null) {
            	size+=inputLine.length();//on ajoute le taille du text
            }
            in.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ReadURL.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
                Logger.getLogger(ReadURL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return size;
    }

}
