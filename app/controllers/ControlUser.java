package controllers;

import java.sql.SQLException;
import java.util.Map;

import models.User;
import models.UserInformation;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class ControlUser extends Controller {
	
	static Form<User> userForm = Form.form(User.class);
	
	public static Result index() {
		//TODO : il faudra changer le truc ci-dessous
		return ok(inscription.render());
    }
	
	public static Result newUser() {
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		final String nom = values.get("nom")[0];
		final String prenom = values.get("prenom")[0];
		final String email = values.get("email")[0];
		final String pseudo = values.get("pseudo")[0];
		final String mdp = values.get("mdp")[0];
		
		UserInformation ui=new UserInformation();
		try {
			if (!ui.pseudoAlreadyExists(pseudo)){
				User u = new User(pseudo, mdp, nom, prenom, email);
				User.create(u);
			}else{
				return ok("Cet utilisateur exist déjà! ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok("OK");

/*		Form<User> filledForm = userForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			
			 // on regarde si le formulaire a bien été rempli.
			 
			//TODO : il faudra changer le truc ci-dessous
			return badRequest(inscription.render(Application.maSession));
		} else {
			 // quand c'est bon on récupère les champs et 
			 // on les sauvegarde sur la BD H2
			 
			UserInformation ui=new UserInformation();
			try {
				if (!ui.pseudoAlreadyExists(filledForm.field("pseudo").value())){
					User.create(filledForm.get());
				}else{
					return badRequest(inscription.render(Application.maSession));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return redirect(routes.Application.index());  
		}*/
	}
}
