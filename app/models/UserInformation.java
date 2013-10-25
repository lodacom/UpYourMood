package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInformation {

	private User infoUser;
	private static String quoteCharacter="'";

	public UserInformation(){

	}

	public void retrieveInformation(String pseudo) throws SQLException{
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT * FROM \"UserInfo\" " +
				"WHERE pseudo="+quoteCharacter+pseudo+quoteCharacter);
		if (!res.first()){
			this.infoUser=new User("guest");
		}else{
			res.first();
			this.infoUser=new User(
					res.getString("pseudo"),
					res.getString("nom"),
					res.getString("prenom"),
					res.getString("mdp"),
					res.getString("email"));
		}
		ConnectionBase.close();
	}

	public void profil(String pseudo) throws SQLException{
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT * FROM \"UserInfo\" " +
				"WHERE pseudo="+quoteCharacter+pseudo+quoteCharacter);
		res.first();
		this.infoUser=new User(
				res.getString("pseudo"),
				res.getString("mdp"),
				res.getString("nom"),
				res.getString("prenom"),
				res.getString("email"));
		ConnectionBase.close();
	}

	/**
	 * 
	 * @param pseudo
	 * @return vrai s'il y a déjà un pseudo égual au paramètre. Sinon faux
	 * @throws SQLException
	 */
	public boolean pseudoAlreadyExists(String pseudo) throws SQLException{
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT pseudo FROM \"UserInfo\" " +
				"WHERE pseudo="+quoteCharacter+pseudo+quoteCharacter);
		boolean retour=res.first();
		ConnectionBase.close();
		return retour;
	}
	
	/**
	 * 
	 * @param pseudo
	 * @param mdp
	 * @return vrai si l'utilisateur est autorisé à se connecter. Sinon faux
	 * @throws SQLException
	 */
	public boolean connectionAllowed(String pseudo,String mdp) throws SQLException{
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT pseudo,mdp " +
				"FROM \"UserInfo\" " +
				"WHERE pseudo='"+pseudo+"'"+
				"AND mdp='"+mdp+"'");
		boolean retour=res.first();
		ConnectionBase.close();
		return retour;
	}
	
	public User getInfoUser() {
		return infoUser;
	}


}
