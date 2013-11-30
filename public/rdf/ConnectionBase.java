package TP3;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMConstants;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMSource;

public class ConnectionBase {

	private static Connection conn;
	private static String quoteCharacter="'";
	
	public static void open(){
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UpYourMood_DB", "postgres", "loda");
			//conn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/UpYourMood_DB", "postgres", "loda");
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
			//System.out.println("Erreur requ??te : " + e.getMessage()); 
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
	
	public static void main(String[] args){
		ConnectionBase.open();
		ConnectionBase.requete("CREATE TABLE colors (hexadecimal character varying(255),"
				+ "color character varying(255))");
		SAXBuilder sxb=new SAXBuilder();
		try {
			Document document=sxb.build(new File("colors.xml"));
			Element racine = document.getRootElement();
			List<Element> list=racine.getChildren();
			Iterator<Element> iter=list.iterator();
			while (iter.hasNext()){
				Element chose=iter.next();
                String hex=chose.getAttributeValue("hexadecimal");
                String color=chose.getAttributeValue("string");
                ConnectionBase.requete("INSERT INTO colors (hexadecimal,color) VALUES "
                		+ "("+quoteCharacter+hex+quoteCharacter+","+quoteCharacter+color+quoteCharacter+")");
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionBase.close();
	}
}
