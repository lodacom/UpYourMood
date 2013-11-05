package controllers;

import java.util.ArrayList;
import java.util.List;

import models.EndPointQueries;
import models.UpQueries;
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
			UpQueries uq=new UpQueries();
			EndPointQueries epq=uq.userQueriesFromEndPoint(filledForm.field("query").value());
			
			if(filledForm.field("format").value().equals("auto") || 
					filledForm.field("format").value().equals("text/html")){
				return ok(endpoint_response.render(epq));
			}
			if(filledForm.field("format").value().contains("json")){
				
			}
			if(filledForm.field("format").value().contains("rdf+xml")){
				
			}
			return ok(endpoint_sparql.render());
		}
	}
}
