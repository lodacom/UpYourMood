package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe qui permet de récupérer les musiques préférées d'un utilisateur.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
public class BDListenAgain {

	public HashMap<Think,Integer> again;
	
	/**
	 * Constructeur qui instancie une HashMap ayant pour clé un objet Think et pour valeur un simple Integer.
	 */
	public BDListenAgain(){
		again=new HashMap<Think,Integer>();
	}
	
	/**
	 * <p>Méthode qui permet de récupérer au plus les 10 musiques préférées pour un utilisateur donné, 
	 * en interrogeant la BDD. Le résultat est stocké dans la HashMap instanciée dans le constructeur.</p>
	 * @param pseudo Le pseudo de l'utilisateur dont on veut récupérer ses musiques préférées.
	 * @throws SQLException Echec de connexion à la BDD.
	 */
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
	
	/**
	 * <p>Méthode qui sert à déterminer si l'identifiant de la musique traité en ce moment est déjà présent
	 * dans la HashMap.</p>
	 * @param idMusique Identifiant de la musique traité actuellement.
	 * @return vrai si l'identifiant de la musique est déjà présent dans la HashMap, faux sinon.
	 */
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
