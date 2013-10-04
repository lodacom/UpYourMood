package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import models.ConnectionBase;
import models.ConnectionUtil;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	
	static Form<ConnectionUtil> connectionUser = Form.form(ConnectionUtil.class);
	
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result connection(){
    	Form<ConnectionUtil> filledForm = connectionUser.bindFromRequest();
    	if(filledForm.hasErrors()) {
			return badRequest(accueil.render());
		} else {
			ConnectionBase.open();
	    	ResultSet res=ConnectionBase.requete("SELECT pseudo,mdp " +
	    			"FROM User " +
	    			"WHERE pseudo='"+filledForm.field("pseudo").value()+"'"+
	    			"AND mdp='"+filledForm.field("mdp").value()+"'");
	    	try {
				if (!res.first()){
					ConnectionBase.close();
					return redirect(routes.ControlAccueil.index());
				}else{
					ConnectionBase.close();
					return redirect(routes.Application.index()); 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return redirect(routes.Application.index());  
		}
    }
}
