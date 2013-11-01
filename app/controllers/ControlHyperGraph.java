package controllers;

import play.mvc.*;
import views.html.*;
import models.graphviz.*;

public class ControlHyperGraph extends Controller{

	public static HyperGraph hg;
	
	public static Result index(){
		hg=new HyperGraph();
		hg.buildHyperGraph();
		return ok(hypergraph.render());
	}
}
