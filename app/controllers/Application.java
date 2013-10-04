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
    	String user = session("connected");
    	  if(user != null) {
    		  return ok(index.render("Your new application is ready.",true));
    	  } else {
    	    return unauthorized(index.render("Your new application is ready.",false));
    	  }
    }
    
    public static Result connection(){
    	String user = session("connected");
  	  	if(user != null) {
  		  return deconnection();
  	  	}
    	Form<ConnectionUtil> filledForm = connectionUser.bindFromRequest();
    	if(filledForm.hasErrors()) {
			return badRequest(accueil.render(false));
		} else {
			ConnectionBase.open();
	    	ResultSet res=ConnectionBase.requete("SELECT pseudo,mdp " +
	    			"FROM User " +
	    			"WHERE pseudo='"+filledForm.field("pseudo").value()+"'"+
	    			"AND mdp='"+filledForm.field("mdp").value()+"'");
	    	try {
				if (!res.first()){
					/*
					 *Mot de passe ou login mauvais 
					 */
					ConnectionBase.close();
					return redirect(routes.ControlAccueil.index());
				}else{
					session("connected",filledForm.field("pseudo").value());
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
    
    public static Result deconnection(){
    	session().remove("connected");
    	return redirect(routes.Application.index());
    }
}
