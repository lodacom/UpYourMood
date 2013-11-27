package controllers;

import play.mvc.*;
import views.html.*;
import models.UpQueries;

public class ControlHyperGraph extends Controller{

	//public static HyperGraph hg;
	public static UpQueries uq;
	
	public static Result index(){
		//hg=new HyperGraph();
		//hg.buildHyperGraph();
		uq=new UpQueries();
		uq.hyperGraph();
		return ok(hypergraph.render(Application.maSession));
	}
}
