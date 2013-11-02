package controllers;

import models.EndPointQueries;
import play.mvc.*;
import views.html.*;
import play.data.Form;

public class ControlEndPointSparql extends Controller{

	static Form<EndPointQueries> edqForm= Form.form(EndPointQueries.class);
	
	public static Result index(){
		return ok(endpoint_sparql.render());
	}
	
	public static Result query(){
		Form<EndPointQueries> filledForm = edqForm.bindFromRequest();
		if (filledForm.hasErrors()){
			return ok(endpoint_sparql.render());
		}else{
			
			return ok(endpoint_sparql.render());
		}
	}
}
