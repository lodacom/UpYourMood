package controllers;

import models.User;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class ControlUser extends Controller {
	
	static Form<User> userForm = Form.form(User.class);
	
	public static Result index() {
        return ok(inscription.render());
    }
	
	public static Result newUser() {
		Form<User> filledForm = userForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			/*
			 * on regarde si le formulaire a bien été rempli.
			 */
			return badRequest(inscription.render());
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
