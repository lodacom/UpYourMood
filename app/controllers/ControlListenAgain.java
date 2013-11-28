package controllers;

import play.mvc.*;
import views.html.*;

public class ControlListenAgain extends Controller {

	public static Result index(){
		return ok(views.html.listen_again_jamendo.render(Application.maSession, "23962"));
	}
}
