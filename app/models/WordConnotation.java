package models;

public class WordConnotation {
	
	private String mot;
	private float connotation;
	
	/**
	 * 
	 * @param mot le mot que l'on souhaite connoter
	 * @param connotation une note entre 0 et 10
	 * 0 étant concidéré comme très négatif
	 * 10 étant concidéré comme très positif
	 */
	public WordConnotation(String mot,float connotation){
		this.mot=mot;
		this.connotation=connotation;
	}

	public String getMot() {
		return mot;
	}

	public float getConnotation() {
		return connotation;
	}
}
