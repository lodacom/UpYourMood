package controllers;

import models.Tweet;
import play.data.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

public class ControlTweet extends Controller{
	static Form<Tweet> tweetForm = Form.form(Tweet.class);
	
	/**
	 * Ici avec cette méthode on fait de la négociation de contenu.
	 * Car on propose pour une même URI la possibilité d'avoir différentes
	 * versions d'un même document (ici soit un doc HTML, JSON, RDF) pour un 
	 * agent cad ici le navigateur.
	 * L'agent va choisir la version la plus adapté à ses moyens ici HTML. 
	 * @return
	 */
	public static Result listTweet(){
    	if (request().accepts("text/html")){
    		/*on envoie la liste des tweets via la méthode GET 
    		 * avec Tweet.all on envoie également la variable tweetForm
    		 * qui permetra de générer le formulaire
    		 */
    		return ok(essai.render(Tweet.all(),tweetForm));
    	}else{
    		if (request().accepts("application/json")){
    			return ok(Json.toJson(Tweet.all()));
    		}else {
    			if (request().accepts("application/rdf+xml")){
    				return ok("This will be RDF XML");
    			}
    		}
    		return badRequest();
    	}
    }
    
    public static Result newTweet(){
    	Form<Tweet> filledForm = tweetForm.bindFromRequest();
    	  if(filledForm.hasErrors()) {
    		  /*
    		   * on regarde si le formulaire a bien été rempli.
    		   */
    	    return badRequest(
    	      essai.render(Tweet.all(), filledForm)
    	    );
    	  } else {
    		  /*
    		   * quand c'est bon on récupère les champs et 
    		   * on les sauvegarde sur la BD H2
    		   */
    	    Tweet.create(filledForm.get());
    	    return redirect(routes.ControlTweet.listTweet());  
    	  }
    }
    
    public static Result deleteTweet(Long id){
    	Tweet.delete(id);
    	return redirect(routes.ControlTweet.listTweet());
    }

}
