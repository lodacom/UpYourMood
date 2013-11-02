package controllers;

import java.sql.SQLException;
import models.User;
import models.UserInformation;
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
		}
	}
}
