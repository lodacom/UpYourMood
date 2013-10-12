package models;
import java.sql.*;

public class ConnectionBase {

	private static Connection conn;
	
	public static void open(){
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:h2:mem:play", "loda", "loda");
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ResultSet requete(String req){
		Statement stmt;
		try {
			stmt = conn.createStatement();
			return stmt.executeQuery(req);
		} catch (SQLException e) {
			System.out.println("Erreur requête : " + e.getMessage()); 
			return null;
		}
	}
	
	public static void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
