package app.models;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URLEncoder; 


public class ReadURL {

    String _url;

    public ReadURL(String _url){
        this._url = _url;
        System.out.println(this._url);
    }

    public String getURLContent() {
        String inputLine=new String();
        StringBuilder str = new StringBuilder();
        try {
        	URL mURL = new URL(this._url);
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(mURL.openStream()));
            while ((inputLine = in.readLine()) != null) {
                str.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ReadURL.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
                Logger.getLogger(ReadURL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str.toString();
    }
    
    
    public Integer getURLSize() {
        String inputLine=new String();
        Integer size = 0;
        try {
        	URL mURL = new URL(this._url);
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(mURL.openStream()));
            while ((inputLine = in.readLine()) != null) {
            	size+=inputLine.length();
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
