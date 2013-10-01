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
	
	public User(){
		
	}
	
	public static void create(User user){
		user.save();
	}
}
