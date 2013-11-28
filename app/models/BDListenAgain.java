package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BDListenAgain {

	public HashMap<Think,Integer> again;
	
	public BDListenAgain(){
		again=new HashMap<Think,Integer>();
	}
	
	public void retreiveMusics(String pseudo){
		Think t;
		ConnectionBase.open();
		ResultSet res=ConnectionBase.requete("SELECT * FROM "+pseudo);
		int i=0;
		try {
			while (res.next()){
				String rIdMusique=res.getString("idmusique").toString();
				String rImage=res.getString("image").toString();
				t=new Think(rIdMusique, rImage);
				if (!test(rIdMusique)){
					i++;
					again.put(t, i);
				}else{
					again.put(t, i);
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionBase.close();
	}
	
	private boolean test(String idMusique){
		Set<Think> recup=again.keySet();
		Iterator<Think> iter=recup.iterator();
		boolean trouve=false;
		while(iter.hasNext()){
			if (iter.next().getMot().equals(idMusique)){
				trouve=true;
				break;
			}
		}
		return trouve;
	}
}
