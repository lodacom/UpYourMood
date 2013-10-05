package controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import play.mvc.*;
import views.html.*;

public class ControlJamendo extends Controller{
	
	public static Result index() throws ClientProtocolException, IOException{
		//TODO : il faudra changer e truc ci-dessous
		Application.jam.play();
        return ok(accueil.render(Application.maSession,Application.jam.next()));
    }
	
	public static Result next(){
		
		return ok(player.render(Application.jam.next()));
	}
	
	public static Result previous(){
		return ok(player.render(Application.jam.previous()));
	}
}
