package controllers;

import java.util.Map.Entry;
import java.util.*;

import models.BDListenAgain;
import models.Think;
import models.UpQueries;
import play.mvc.*;
import views.html.*;

public class ControlListenAgain extends Controller {

	public static UpQueries uq;
	public static BDListenAgain bdLA;
	public static String idMusique;
	public static List<String> urlImages;
	public static int nbreImages=0;
	
	public static Result index(){
		uq=new UpQueries();
		bdLA=new BDListenAgain();
		session("compteur_again","1");
		
		String pseudo;
		if (Application.maSession.getPseudo()!=null){
			pseudo=Application.maSession.getPseudo();
			uq.listenAgainJamendo(pseudo);
		}else{
			pseudo="guest";
			uq.listenAgainJamendo(pseudo);
		}
		bdLA.retreiveMusics(pseudo);
		
		images(Integer.parseInt(session("compteur_again")));
		
		return ok(views.html.listen_again_jamendo.render(Application.maSession, idMusique,urlImages));
	}
	
	public static Result next(){
		String pseudo;
		if (Application.maSession.getPseudo()!=null){
			pseudo=Application.maSession.getPseudo();
			uq.listenAgainJamendo(pseudo);
		}else{
			pseudo="guest";
			uq.listenAgainJamendo(pseudo);
		}
		bdLA.retreiveMusics(pseudo);
		
		int recup=Integer.parseInt(session("compteur_again"));
		if (recup==nbreImages){
			session("compteur_again","1");
		}else{
			recup++;
			session("compteur_again",String.valueOf(recup));
		}
		images(Integer.parseInt(session("compteur_again")));
		
		return ok(views.html.listen_again_jamendo.render(Application.maSession, idMusique,urlImages));
	}
	
	public static Result previous(){
		String pseudo;
		if (Application.maSession.getPseudo()!=null){
			pseudo=Application.maSession.getPseudo();
			uq.listenAgainJamendo(pseudo);
		}else{
			pseudo="guest";
			uq.listenAgainJamendo(pseudo);
		}
		bdLA.retreiveMusics(pseudo);
		
		int recup=Integer.parseInt(session("compteur_again"));
		if (recup==1){
			session("compteur_again",String.valueOf(nbreImages));
		}else{
			recup--;
			session("compteur_again",String.valueOf(recup));
		}
		images(Integer.parseInt(session("compteur_again")));
		
		return ok(views.html.listen_again_jamendo.render(Application.maSession, idMusique,urlImages));
	}
	
	private static void images(int compteur){
		urlImages=new ArrayList<String>();
		
		Set<Entry<Think,Integer>> recup=bdLA.again.entrySet();
		Iterator<Entry<Think,Integer>> essai=recup.iterator();
		while (essai.hasNext()){
			Entry<Think,Integer> bis=essai.next();
			if (bis.getValue()==compteur){
				idMusique=bis.getKey().getMot();
				urlImages.add(bis.getKey().getImage());
			}
			if (bis.getValue()>nbreImages){
				nbreImages=bis.getValue();
			}
		}
	}
}
