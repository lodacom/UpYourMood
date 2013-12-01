package models;

import javax.persistence.*;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Classe qui permet de gérer les informations des utilisateurs qui s'inscrivent.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
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
	
	/**
	 * Constructeur vide.
	 */
	public User(){
		
	}
	
	/**
	 * Constructeur dans lequel on associe à la variable pseudo le pseudo choisi par l'utilisateur.
	 * @param pseudo Pseudo choisi par l'utilisateur.
	 */
	public User(String pseudo){
		this.pseudo=pseudo;
	}
	
	/**
	 * Constructeur qui instancie :
	 * <ul>
	 * 	<li>Le pseudo de l'utilisateur</li>
	 *  <li>Le mot de passe de l'utilisateur</li>
	 *  <li>Le nom de l'utilisateur</li>
	 *  <li>Le prénom de l'utilisateur</li>
	 *  <li>L'email de l'utilisateur</li>
	 * </ul>
	 * @param pseudo Pseudo choisi par l'utilisateur.
	 * @param mdp Mot de passe choisi par l'utilisateur.
	 * @param nom Nom de l'utilisateur.
	 * @param prenom Prénom de l'utilisateur.
	 * @param email Email de l'utilisateur.
	 */
	public User(String pseudo,String mdp,String nom,String prenom,String email){
		this.pseudo=pseudo;
		this.mdp=mdp;
		this.nom=nom;
		this.prenom=prenom;
		this.email=email;
	}
	
	/**
	 * Méthode qui permet de sauvegarder le nouvel utilisateur dans la BDD.
	 * @param user L'objet User contenant toutes les informations relatives au nouvel utilisateur.
	 * @see User#save()
	 */
	public static void create(User user){
		user.save();
	}
	
	/**
	 * Méthode qui permet de rentrer toutes les informations de l'utilisateur dans la BDD.
	 */
	public void save(){
		ConnectionBase.open();
		ConnectionBase.requete("INSERT INTO \"User\"(pseudo,mdp,nom,prenom,email) VALUES " +
				"("+quoteCharacter+pseudo+quoteCharacter+","+quoteCharacter+mdp+quoteCharacter+
				","+quoteCharacter+nom+quoteCharacter+","+quoteCharacter+prenom+quoteCharacter+","
				+quoteCharacter+email+quoteCharacter+")");
		ConnectionBase.close();
	}
	
	/**
	 * Méthode qui permet de mettre à jour le pseudo d'un utilisateur donné.
	 * @param user L'utilisateur qui souhaite mettre à jour son pseudo.
	 * @param pseudo Le nouveau pseudo choisi par l'utilisateur.
	 * @see User#update(String)
	 */
	public static void update(User user,String pseudo){
		user.update(pseudo);
	}
	
	/**
	 * Mise à jour du pseudo de l'utilisateur.
	 * @param pseudo Nouveau pseudo de l'utilisateur.
	 */
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
