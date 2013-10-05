package controllers;

import models.User;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class ControlUser extends Controller {
	
	static Form<User> userForm = Form.form(User.class);
	
	public static Result index() {
		//TODO : il faudra changer le truc ci-dessous
        return ok(inscription.render(Application.maSession));
    }
	
	public static Result newUser() {
		Form<User> filledForm = userForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			/*
			 * on regarde si le formulaire a bien été rempli.
			 */
			//TODO : il faudra changer le truc ci-dessous
			return badRequest(inscription.render(Application.maSession));
		} else {
			/*
			 * quand c'est bon on récupère les champs et 
			 * on les sauvegarde sur la BD H2
			 */
			User.create(filledForm.get());
			return redirect(routes.Application.index());  
		}
	}
}
