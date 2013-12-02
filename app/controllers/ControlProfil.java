package controllers;

import java.sql.SQLException;

import models.User;
import models.UserInformation;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class ControlProfil extends Controller {

	private static UserInformation UI=null;
	static Form<User> userForm = Form.form(User.class);
	
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
	
	public static Result update(){
		Form<User> filledForm = userForm.bindFromRequest();
		UI=new UserInformation();
		if(filledForm.hasErrors()) {
			try {
				UI.retrieveInformation(Application.maSession.getPseudo());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return badRequest(profil.render(Application.maSession,UI.getInfoUser()));
		}else{
			String pseudo=filledForm.field("pseudo").value();
			if (!pseudo.equals(Application.maSession.getPseudo())){
				try {
					if (!UI.pseudoAlreadyExists(pseudo)){
						User.update(filledForm.get(),Application.maSession.getPseudo());
					}else{
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				User.update(filledForm.get(),Application.maSession.getPseudo());
			}
			try {
				UI.retrieveInformation(Application.maSession.getPseudo());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ok(profil.render(Application.maSession,UI.getInfoUser()));
		}
	}
}
