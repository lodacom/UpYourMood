package models;

/**
 * Classe qui permet de savoir quels utilisateurs sont connectés.
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
public class SessionValues {

	private boolean connected;
	private String pseudo;
	
	/**
	 * Constructeur.
	 * @param connected
	 */
	public SessionValues(boolean connected){
		this.connected=connected;
	}
	
	/**
	 * Getter sur le pseudo.
	 * @return le pseudo.
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * Setter sur le pseudo.
	 * @param pseudo Pseudo à affecter.
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	/**
	 * Savoir si un utilisateur est connecté.
	 * @return vrai si connecté, faux sinon.
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Setter sur le booléen connected.
	 * @param connected Valeur true ou false à affecter.
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
