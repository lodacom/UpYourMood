package models;

import play.data.validation.Constraints.Required;

public class ConnectionUtil {
	
	@Required
	public String pseudo;
	@Required 
	public String mdp;
	
}
