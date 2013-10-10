package controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import models.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	
	public static SessionValues maSession=SessionValues.getInstance();
	static Form<ConnectionUtil> connectionUser = Form.form(ConnectionUtil.class);
	public static Jamendo jam=new Jamendo();
	
    public static Result index() throws ClientProtocolException, IOException {
    	String user = session("connected");
    	jam.play();
    	  if(user != null) {
    		  //return ok(index.render("Your new application is ready.",maSession));
    		  return ok(accueil.render(maSession, jam.next()));
    	  } else {
    	    //return unauthorized(index.render("Your new application is ready.",maSession));
    		  return unauthorized(accueil.render(maSession, jam.next()));
    	  }
    }
    
    public static Result connection(){
    	String user = session("connected");
    	if(user != null) {
    		maSession.setConnected(false);
    		return deconnection();
    	}
    	Form<ConnectionUtil> filledForm = connectionUser.bindFromRequest();
    	if(filledForm.hasErrors()) {
    		return badRequest(accueil.render(maSession,Application.jam.current()));
    	} else {
    		ConnectionBase.open();
    		ResultSet res=ConnectionBase.requete("SELECT pseudo,mdp " +
    				"FROM User " +
    				"WHERE pseudo='"+filledForm.field("pseudo").value()+"'"+
    				"AND mdp='"+filledForm.field("mdp").value()+"'");
    		try {
    			if (!res.first()){
    				/*
    				 *Mot de passe ou login mauvais 
    				 */
    				ConnectionBase.close();
    				return redirect(routes.ControlAccueil.index());
    			}else{
    				session("connected",filledForm.field("pseudo").value());
    				ConnectionBase.close();
    				maSession.setConnected(true);
    				maSession.setPseudo(filledForm.field("pseudo").value());
    				return redirect(routes.Application.index()); 
    			}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return redirect(routes.Application.index());  
    	}
    }
    
    public static Result deconnection(){
    	session().remove("connected");
    	return redirect(routes.Application.index());
    }
    
    public static Result CheckWord(){
    	final Map<String, String[]> values = request().body().asFormUrlEncoded();
    	final String name = values.get("sentiment")[0];
    	if (!name.matches("^[a-zA-ZÀàÂâÆæÇçÉéÈèÊêËëÎîÏïÔôŒœÙùÛûÜüŸÿ]+$")){
    		return redirect(routes.Application.index());
    	}else{
    		String utf_encoded="";
    		try {
				utf_encoded = URLEncoder.encode(name,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    		String _url = "http://api.wordreference.com/78289/json/fren/" + utf_encoded;
        	ReadURL ru = new ReadURL(_url);
            Integer size = ru.getURLSize();
            String result;
            if (size>150){
            	//on bon mot: on peut travailler avec
            	
            }
            return redirect(routes.Application.index());
    	}
    	
    	
    }
}
