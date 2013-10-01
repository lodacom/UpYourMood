package models;

import java.util.*;
import javax.persistence.*;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity /*L'annotation @Entity marque cette classe comme une entité JPA. 
*La classe parente, Model, offre un ensemble d'assistants pour JPA.
* Tous les champs de cette classe seront automatiquement persistés en base.
*/
public class Tweet extends Model{
	@Id
	public Long id;
	@Required 
	public String commentaire;
	@Required
	public String pseudo;
	public Date creationDate;
	
	/*l'annotation Id permet de dire que ce champ occupera beaucoup de place dans la BD
	 * l'annotaion Required de dire que ce champ est requis dans le formulaire
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Finder<Long,Tweet> find = new Finder(Long.class, Tweet.class);
	
	/**
	 * Constructeur vide obligatoire
	 */
	public Tweet(){
		creationDate=new Date();
	}
	
	public static List<Tweet>all(){
		return find.all();//retourne tous les tweets enregistrés
	}
	
	public static void create(Tweet tweet){
		tweet.save();/*on se sert de save présent dans la 
		classe Model (Tweet hérite de Model)
		*/
	}
	
	public static void delete(Long id) {
		  find.ref(id).delete();/*on cherche l'identifiant passer en param
		  dans la BD et on le vire avec delete (provenant comme pour save de
		  Model).
		  */
	}
}
