package controllers;

import play.mvc.*;
import views.html.*;

public class ControlJamendo extends Controller{
	
	public static Result index(){
		//TODO : il faudra changer e truc ci-dessous
        return ok(index.render(Application.maSession,Application.jam.next()));
    }
	
	public static Result nextPrevious(Long decision){
		if (decision==0){
			return ok(index.render(Application.maSession,Application.jam.previous()));
		}else{
			return ok(index.render(Application.maSession,Application.jam.next()));
		}
	}
}
