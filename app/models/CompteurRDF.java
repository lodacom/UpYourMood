package models;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Entity;
import javax.persistence.Id;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class CompteurRDF extends Model{

	@Id @Required
	public long compteur;
	//private static String quoteCharacter="'";
	
	public CompteurRDF(){
		
	}
	
	public CompteurRDF(long compteur){
		this.compteur=compteur;
	}
	
	public void update(){
		ConnectionBase.open();
		ConnectionBase.requete("UPDATE \"Compteur\" " +
				"SET compteur=compteur+1");
		ConnectionBase.close();
	}
	
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
