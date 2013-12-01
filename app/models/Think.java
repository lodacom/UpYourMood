package models;

/**
 * <p>Classe permettant de modéliser ce à quoi pense un utilisateur en écoutant une musique.</p>
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
public class Think {
	
	private String mot;
	private String image;
	
	/**
	 * Constructeur qui associe à chaque variable les valeurs correspondantes.
	 * @param mot le mot associé à la musique.
	 * @param image l'image choisie par l'utilisateur qui représente le mieux le mot entré.
	 */
	public Think(String mot,String image){
		this.mot=mot;
		this.image=image;
	}

	/**
	 * Getter pour récupérer le mot associé.
	 * @return le mot associé.
	 */
	public String getMot() {
		return mot;
	}

	/**
	 * Getter pour récupérer l'image associée au mot.
	 * @return l'image associée au mot.
	 */
	public String getImage() {
		return image;
	}
	
	
}
