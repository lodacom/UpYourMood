package controllers;

import play.mvc.*;
import views.html.*;

public class ControlAccueil extends Controller {
	
	public static Result index(){
		//TODO : il faudra changer e truc ci-dessous
        return ok(accueil.render(Application.maSession,Application.jam.next()));
    }
}
