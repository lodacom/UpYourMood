package models;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints.Required;

public class EndPointQueries{
	
	@Required
	public String query;
	public List<ArrayList<String>> response;
	public List<String> headOfArray;
	private int index=-1;
	
	public List<String> colonne(){
		if (index<response.size()){
			index++;
			return response.get(index);
		}else{
			return null;
		}
	}
}
