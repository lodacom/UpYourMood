package controllers;

import models.UpQueries;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class ControlHyperGraphPerso extends Controller {

public static UpQueries uq;
	
	public static Result index(){
		//hg=new HyperGraph();
		//hg.buildHyperGraph();
		uq=new UpQueries();
		String pseudo=Application.maSession.getPseudo();
		if (pseudo==null){
			pseudo="guest";
			uq.hyperGraphOfAUser(pseudo);
		}else{
			uq.hyperGraphOfAUser(pseudo);
		}
		
		return ok(hypergraph_perso.render(Application.maSession,pseudo));
	}
}
