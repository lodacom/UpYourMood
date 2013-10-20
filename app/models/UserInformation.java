package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserInformation {

	private List<String> infoUser;
	private static String quoteCharacter="'";
	
	public UserInformation(){
		infoUser=new ArrayList<String>();
	}
	
	public void retrieveInformation(String pseudo) throws SQLException{
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT * FROM \"UserInfo\" " +
		"WHERE pseudo="+quoteCharacter+pseudo+quoteCharacter);
		if (!res.first()){
			infoUser.add("guest");
		}else{
			res.first();
			infoUser.add(res.getString("pseudo"));
			infoUser.add(res.getString("nom"));
			infoUser.add(res.getString("prenom"));
			infoUser.add(res.getString("mdp"));
			infoUser.add(res.getString("email"));
		}
		ConnectionBase.close();
	}

	public List<String> getInfoUser() {
		return infoUser;
	}
	
	
}
