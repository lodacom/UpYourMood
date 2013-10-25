package controllers;

import models.OntologyDescription;
import play.mvc.*;
import views.html.*;

public class ControlOntology extends Controller {
	
	public static Result index(){
		OntologyDescription od=new OntologyDescription();
		if (request().accepts("application/rdf+xml")){
			return ok(od.OwlDescription());
		}
		return badRequest();
	}
}
