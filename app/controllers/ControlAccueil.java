package controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import play.mvc.*;
import views.html.*;

public class ControlAccueil extends Controller {
	
	public static Result index() throws ClientProtocolException, IOException{
		//TODO : il faudra changer e truc ci-dessous
		Application.jam.play();
        return ok(accueil.render(Application.maSession,Application.jam.next()));
    }
}
