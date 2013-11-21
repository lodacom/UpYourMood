package controllers;

import play.mvc.*;
import views.html.*;

public class ControlJamendo extends Controller{
	
	public static Result index(){
		//TODO : il faudra changer e truc ci-dessous
        return ok(index.render(Application.maSession,Application.jam.next()));
    }
	
	public static Result previous(){
		return ok(player.render(Application.jam.previous()));
	}
	
	public static Result next(){
		return ok(player.render(Application.jam.next()));
	}
}
