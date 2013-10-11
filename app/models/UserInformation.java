package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserInformation {

	private List<String> infoUser;
	
	public UserInformation() throws SQLException{
		infoUser=new ArrayList<String>();
		retrieveInformation();
	}
	
	private void retrieveInformation() throws SQLException{
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT * FROM User");
		while (res.next()){
			infoUser.add(res.getString("pseudo"));
		}
		ConnectionBase.close();
	}

	public List<String> getInfoUser() {
		return infoUser;
	}
	
	
}
