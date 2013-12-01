package models;

import play.data.validation.Constraints.Required;

/**
 * Classe permettant de modéliser la connection d'un utilisateur.
 * Seulement utiliser pour la vérifcation du formulaire.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
public class ConnectionUtil {
	
	@Required
	public String pseudo;
	@Required 
	public String mdp;
	
}
