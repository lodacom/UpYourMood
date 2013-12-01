package models;

/**
 * <p>Classe permettant de modéliser une émotion ressentie par un utilisateur, 
 * incluant la pochette de la musique écoutée, le mot associé ainsi que la couleur.</p>
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
public class Emotion {

	private String pochette;
	private String mot;
	private String couleur;
	
	/**
	 * Constructeur qui à chaque variable les valeurs correspondantes.
	 * @param pochette La pochette de l'ablum auquel appartient la musique écoutée.
	 * @param mot Le mot associé à la musique.
	 * @param couleur La couleur associée à la musique.
	 */
	public Emotion(String pochette,String mot,String couleur){
		this.pochette=pochette;
		this.mot=mot;
		this.couleur=couleur;
	}

	/**
	 * Getter pour récupérer l'url de la pochette d'album.
	 * @return l'url de la pochette d'album.
	 */
	public String getPochette() {
		return pochette;
	}

	/**
	 * Getter pour récupérer le mot associé à la musique.
	 * @return le mot associé à la musique.
	 */
	public String getMot() {
		return mot;
	}

	/**
	 * Getter pour récupérer la couleur associée à la musique.
	 * @return la couleur associée à la musique.
	 */
	public String getCouleur() {
		return couleur;
	}
}
