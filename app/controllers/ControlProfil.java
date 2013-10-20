package controllers;

import java.sql.SQLException;

import models.UserInformation;
import play.mvc.*;
import views.html.*;

public class ControlProfil extends Controller {

	private static UserInformation UI=null;
	
	public static Result index(){
		if (Application.maSession.isConnected()){
			UI=new UserInformation();
			try {
				UI.retrieveInformation(Application.maSession.getPseudo());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ok(profil.render(Application.maSession,UI.getInfoUser()));
		}else{
			return redirect(routes.Application.index());
		}
    }
}
