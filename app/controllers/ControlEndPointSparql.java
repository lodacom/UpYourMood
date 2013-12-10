package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;

import models.EndPointQueries;
import models.UpQueries;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import play.data.Form;
import sun.java2d.pipe.RenderBuffer;

public class ControlEndPointSparql extends Controller{

	static Form<EndPointQueries> edqForm= Form.form(EndPointQueries.class);
	
	private final static String cfgProp = "app/controllers/fuseki.properties";
	private final static Properties configFile = new Properties() {
		private final static long serialVersionUID = 1L; {
			try {
				load(new FileInputStream(cfgProp));
			} catch (Exception e) {}
		}
	};
	private static String FUSEKI = configFile.getProperty("fusekiFor");
	
	public static Result index(){
		return ok(endpoint_sparql.render(Application.maSession));
	}
	
	public static Result fuseki(){
		Runtime rt = Runtime.getRuntime();
		String[] clean={FUSEKI+"s-update", "--service" ,"http://localhost:3030/ds/update", "'CLEAR DEFAULT'"};
		String path="public/rdf/upyourmood.rdf";
		String path2="public/rdf/uymcolors.rdf";
		String[] put={FUSEKI+"s-put" ,"http://localhost:3030/ds/data","default",path};
		String[] put2={FUSEKI+"s-put" ,"http://localhost:3030/ds/data","default",path2};
		try {
			rt.exec(clean);
			rt.exec(put);
			rt.exec(put2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return redirect("http://localhost:3030/sparql.tpl");
	}
	
	public static Result query(String query,String format){
			UpQueries uq=new UpQueries();			
			if(format.equals("auto") || format.equals("text/html")){
				EndPointQueries epq=uq.userQueriesFromEndPoint(query);
				return ok(endpoint_response.render(epq));
			}
			if(format.contains("json")){
				EndPointQueries epq=uq.userQueriesFromEndPoint(query);
				return ok(Json.toJson(epq.response));
			}
			if(format.contains("rdf+xml")){
				String rdf_response=uq.userQueriesResponseFromRDF(query);
				return ok(rdf_response);
			}
			return ok(endpoint_sparql.render(Application.maSession));
	}
}
