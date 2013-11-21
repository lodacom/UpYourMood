package models;
import java.sql.*;

public class ConnectionBase {

	private static Connection conn;
	
	public static void open(){
		try {
			Class.forName("org.postgresql.Driver");
			//conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UpYourMood_DB", "postgres", "loda");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/UpYourMood_DB", "postgres", "loda");
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
	
	public static ResultSet requete(String req){
		Statement stmt;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			return stmt.executeQuery(req);
		} catch (SQLException e) {
			//System.out.println("Erreur requête : " + e.getMessage()); 
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
