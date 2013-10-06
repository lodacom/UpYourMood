package controllers;

import play.mvc.*;
import views.html.*;

public class ControlProfil extends Controller {

	public static Result index(){
		if (Application.maSession.isConnected()){
			return ok(profil.render(Application.maSession));
		}else{
			return redirect(routes.Application.index());
		}
    }
}
