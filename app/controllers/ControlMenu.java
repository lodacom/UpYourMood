package controllers;

import java.util.Map;

import controllers.Application;
import models.*;
import play.mvc.*;
import views.html.*;

public class ControlMenu extends Controller {
	
	public static Result showPage(){
		final Map<String, String[]> queryParameters = request().queryString();
		final String f = queryParameters.get("file")[0];
		switch(f.toLowerCase())
			{
			   case "menu_nos_offres" : return ok(menu_nos_offres.render());
			   case "menu_les_fonctionnalites" : return ok(menu_les_fonctionnalites.render());
			   case "menu_a_propos" : return ok(menu_a_propos.render());
               case "menu_developpeurs" : return ok(menu_developpeurs.render());
			   case "menu_votre_musique" : return ok(menu_votre_musique.render());
			   case "menu_aide_et_contact" : return ok(menu_aide_et_contact.render());
			   case "menu_donnees_personnelles" : return ok(menu_donnees_personnelles.render());
			   case "menu_mentions_legales" : return ok(menu_mentions_legales.render());
			   default : return ok(menu_aide_et_contact.render());
			 }
	}
	
}
