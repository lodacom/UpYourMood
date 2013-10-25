package models;

import javax.persistence.*;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@SuppressWarnings("serial")
public class User extends Model {
	@Id @Required
	public String pseudo;
	@Required 
	public String mdp;
	public String nom;
	public String prenom;
	@Required 
	public String email;
	private static String quoteCharacter="'";
	
	public User(){
		
	}
	
	public User(String pseudo){
		this.pseudo=pseudo;
	}
	
	public User(String pseudo,String mdp,String nom,String prenom,String email){
		this.pseudo=pseudo;
		this.mdp=mdp;
		this.nom=nom;
		this.prenom=prenom;
		this.email=email;
	}
	
	public static void create(User user){
		user.save();
	}
	
	public void save(){
		ConnectionBase.open();
		ConnectionBase.requete("INSERT INTO \"User\"(pseudo,mdp,nom,prenom,email) VALUES " +
				"("+quoteCharacter+pseudo+quoteCharacter+","+quoteCharacter+mdp+quoteCharacter+
				","+quoteCharacter+nom+quoteCharacter+","+quoteCharacter+prenom+quoteCharacter+","
				+quoteCharacter+email+quoteCharacter+")");
		ConnectionBase.close();
	}
	
	public static void update(User user,String pseudo){
		user.update(pseudo);
	}
	
	public void update(String pseudo){
		ConnectionBase.open();
		ConnectionBase.requete("UPDATE \"User\" " +
				"SET pseudo="+quoteCharacter+this.pseudo+quoteCharacter+","+
				"mdp="+quoteCharacter+mdp+quoteCharacter+","+
				"nom="+quoteCharacter+nom+quoteCharacter+","+
				"prenom="+quoteCharacter+prenom+quoteCharacter+","+
				"email="+quoteCharacter+email+quoteCharacter+" "+
				"WHERE pseudo="+quoteCharacter+pseudo+quoteCharacter);
		ConnectionBase.close();
	}
}
