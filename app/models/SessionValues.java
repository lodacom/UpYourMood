package models;

public class SessionValues {

	private boolean connected;
	
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
