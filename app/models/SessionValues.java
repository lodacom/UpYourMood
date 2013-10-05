package models;

public class SessionValues {

	private boolean connected;
	private String pseudo;
	
	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public SessionValues(boolean connect){
		connected=connect;
	}
}
