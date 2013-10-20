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
}
