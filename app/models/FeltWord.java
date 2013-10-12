package models;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
public class FeltWord extends Model{

	@Required
	public String mot;
	
	public FeltWord(){
		
	}
}
