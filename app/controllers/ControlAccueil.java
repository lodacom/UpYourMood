package controllers;

import play.mvc.*;
import views.html.*;

public class ControlAccueil extends Controller {
	
	public static Result index() {
        return ok(accueil.render());
    }
}
