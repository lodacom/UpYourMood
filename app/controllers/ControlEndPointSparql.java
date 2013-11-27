package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.EndPointQueries;
import models.UpQueries;
import play.mvc.*;
import views.html.*;
import play.data.Form;

public class ControlEndPointSparql extends Controller{

	static Form<EndPointQueries> edqForm= Form.form(EndPointQueries.class);
	
	public static Result index(){
		return ok(endpoint_sparql.render(Application.maSession));
	}
	
	public static Result query(String query,String format){
		/*final Map<String, String[]> values = request().body().asFormUrlEncoded();
		String query=values.get("query")[0];
		String format=values.get("format")[0];*/
		/*Form<EndPointQueries> filledForm = edqForm.bindFromRequest();
		if (filledForm.hasErrors()){
			return ok(endpoint_sparql.render());
		}else{*/
			UpQueries uq=new UpQueries();
			//String query=filledForm.field("query").value();
			//String format=filledForm.field("format").value();
			//System.out.println(query);
			EndPointQueries epq=uq.userQueriesFromEndPoint(query);
			
			if(format.equals("auto") || format.equals("text/html")){
				return ok(endpoint_response.render(epq));
			}
			if(format.contains("json")){
				
			}
			if(format.contains("rdf+xml")){
				
			}
			return ok(endpoint_sparql.render(Application.maSession));
		//}
	}
}
