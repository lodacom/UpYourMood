package models;

public class Emotion {

	private String pochette;
	private String mot;
	private String couleur;
	
	public Emotion(String pochette,String mot,String couleur){
		this.pochette=pochette;
		this.mot=mot;
		this.couleur=couleur;
	}

	public String getPochette() {
		return pochette;
	}

	public String getMot() {
		return mot;
	}

	public String getCouleur() {
		return couleur;
	}
}
