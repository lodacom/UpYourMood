package models;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints.Required;

public class EndPointQueries{
	
	@Required
	public String query;
	public List<ArrayList<String>> response;
	public List<String> headOfArray;
	private int index=0;
	private int mult=1;
	
	public List<String> colonne(){
		List<String> retour=new ArrayList<String>();
		for (int i=index;i<headOfArray.size()*mult;i++){
			retour.add(response.get(0).get(i));
			index=i;
		}
		mult++;
		System.out.println(retour);
		return retour;
	}
	
	public int lignes(){
		return response.get(0).size()%headOfArray.size();
	}
}
