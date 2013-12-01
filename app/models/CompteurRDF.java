package models;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Classe qui permet de gérer le compteur pour les expériences musicales.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
@SuppressWarnings("serial")
@Entity
public class CompteurRDF extends Model{

	/**
	 * Le compteur, qui permet de distinguer chaque expérience musicale dans upyourmood.rdf.
	 */
	@Id @Required
	public long compteur;
	
	/**
	 * Constructeur vide.
	 */
	public CompteurRDF(){
		
	}
	
	/**
	 * Setter permettant de modifier la valeur du compteur.
	 */
	public CompteurRDF(long compteur){
		this.compteur=compteur;
	}
	
	/**
	 * <p>Méthode qui met à jour le compteur après chaque insertion d'une nouvelle expérience musicale.</p>
	 * @see RDFBuilding#ajouterUtilisateur(UserInformation userInfo, List<String> infoMusic, WordConnotation word, String urlImage)
	 */
	public void update(){
		ConnectionBase.open();
		ConnectionBase.requete("UPDATE \"Compteur\" " +
				"SET compteur=compteur+1");
		ConnectionBase.close();
	}
	
	/**
	 * Getter qui permet de récupérer la valeur du compteur en interrogeant la BDD.
	 * @return la valeur actuelle du compteur.
	 */
	public long select(){
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT * from \"Compteur\"");
		long retour=0;
		try {
			res.first();
			retour=res.getLong("compteur");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionBase.close();
		return retour;
	}
	
}
