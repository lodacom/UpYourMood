package controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import models.Jamendo;
import play.mvc.*;
import views.html.player;

public class ControlJamendo extends Controller{
	
	public static Jamendo jam=new Jamendo();
	
	public static Result index() throws ClientProtocolException, IOException{
		//TODO : il faudra changer e truc ci-dessous
		jam.play();
        return ok(player.render(Application.maSession,jam.next()));
    }
	
	public static Result next(){
		return ok(player.render(Application.maSession,jam.next()));
	}
	
	public static Result previous(){
		return ok(player.render(Application.maSession,jam.previous()));
	}
}
