package controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.databind.JsonNode;

import models.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

	public static SessionValues maSession=new SessionValues(false);
	static Form<ConnectionUtil> connectionUser = Form.form(ConnectionUtil.class);
	public static Jamendo jam=new Jamendo();
	public static DBpediaQueries DBq=new DBpediaQueries();
	
	public static Result index() throws ClientProtocolException, IOException {
		session("compteur","0");
		String user = session("connected");
		if(user != null) {
			return ok(index.render(maSession, jam.current()));
		} else {
			return unauthorized(index.render(maSession, jam.current()));
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
			return badRequest(index.render(maSession,Application.jam.current()));
		} else {
			UserInformation ui=new UserInformation();
			
			try {
				if (!ui.connectionAllowed(filledForm.field("pseudo").value(), filledForm.field("mdp").value())){
					/*
					 *Mot de passe ou login mauvais 
					 */
					return redirect(routes.Application.index());
				}else{
					session("connected",filledForm.field("pseudo").value());
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
		maSession.setConnected(false);
		maSession.setPseudo(null);
		return redirect(routes.Application.index());
	}

	public static Result checkWord(){
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		final String name = values.get("sentiment")[0];
		final Integer valeur = new Integer(values.get("valeur")[0]);
		if (!name.matches("^[a-zA-ZÀàÂâÆæÇçÉéÈèÊêËëÎîÏïÔôŒœÙùÛûÜüŸÿ]+$")){
			return ok("Symbol incorrect");
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
			
			Optimizer opti=new Optimizer();
			if (size>150){
				//ok bon mot: on peut travailler avec
				if (!opti.wordHasBeenAlreadyViewed(name)){
					//le mot n'a jamais été cherché par un utilisateur
					DBq.queryImage(name,"fr");
				}
				//le mot a déjà été recherché par un utilisateur
				opti.prepareImage(name);
				return ok(Json.toJson(opti.urlImageToPrint()));
				
			}else{
				String _url2 = "http://api.wordreference.com/78289/json/enfr/" + utf_encoded;
				ReadURL ru2 = new ReadURL(_url2);
				Integer size2 = ru2.getURLSize();
				
				if (size2>150){
					if (!opti.wordHasBeenAlreadyViewed(name)){
						DBq.queryImage(name,"en");
					}
					//addToRDF(name, valeur);
					opti.prepareImage(name);
					return ok(Json.toJson(opti.urlImageToPrint()));
				}
				return ok("Mot incorrect");
			}
		}
	}
	
	public static Result addColor(){
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		final String color = values.get("color")[0];
		String pseudo = maSession.getPseudo();
		if(pseudo==null){
			pseudo = "guest";
		}
		RDFBuildingColors.getInstance().rdfUYMAddColor(jam.current(), pseudo, color);
		//System.out.println(RDFBuildingColors.getInstance().getMaxColorMusic(jam.current()));
		//System.out.println(RDFBuildingColors.getInstance().getMaxColorMusicByUser(jam.current(), pseudo));
		return ok("");
	}
	
	private static void addToRDF(String name, Integer valeur, String urlImage){
		RDFBuilding rdf=RDFBuilding.getInstance();
		WordConnotation word=new WordConnotation(name, valeur);
		UserInformation userInf= new UserInformation();
		rdf.rdfUpYourMood(jam.currentInfo(),userInf,word,urlImage);
	}
	
	public static Result validationFormulaire(){
		Map<String,String[]> recup=request().body().asFormUrlEncoded();
		String sentimentR=recup.get("TransSentiment")[0];
		String valeurR=recup.get("TransValeur")[0];
		String urlR=recup.get("TransUrlImage")[0];
		
		addToRDF(sentimentR, Integer.parseInt(valeurR), urlR);
		//System.out.println(sentimentR+" "+valeurR+" "+urlR);
		return ok("");
	}
	
}
