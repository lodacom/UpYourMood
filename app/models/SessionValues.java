package models;

public class SessionValues {

	private static volatile SessionValues instance = null;
	private boolean connected;
	private String pseudo;
	
	private SessionValues(boolean connected){
		this.connected=connected;
	}
	
	public final static SessionValues getInstance() {
        if (SessionValues.instance == null) {
           synchronized(SessionValues.class) {
             if (SessionValues.instance == null) {
            	 SessionValues.instance = new SessionValues(false);
             }
           }
        }
        return SessionValues.instance;
    }
	
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
}
