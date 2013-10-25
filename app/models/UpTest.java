package models;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class UpTest {
	
public static final String NL = System.getProperty("line.separator");
	
	public static void main(String[] args){
		 Model m = ModelFactory.createOntologyModel();
		 String fil_URL = "file:upyourmood.rdf";
		 m.read(fil_URL);
		 /*
		  * les mots rentrés par tous les utilisateurs 
		  * pour telle musique
		  */
		 String prolog4 = "PREFIX music: <http://www.upyourmood.com/music/>";
		 String prolog7 = "PREFIX user: <http://www.upyourmood.com/user/>";
		 String prolog8 = "PREFIX wordconnotation: <http://www.upyourmood.com/wordconnotation/>";
		 String prolog1 = "PREFIX nicetag: <http://ns.inria.fr/nicetag/2010/09/09/voc.html#>";
		 String req1=prolog1 + NL + prolog7 + NL + prolog8 + NL +
		"SELECT ?user ?mot " +
		 "WHERE { " +
		 "?user user:HasMusicalExperience ?experi . " +
		 "?experi nicetag:makesMeFeel ?chose . " +
		 "?chose wordconnotation:IsAssociatedBy ?mot" +
		 "}";
		 Query query = QueryFactory.create(req1);
		 QueryExecution qexec = QueryExecutionFactory.create(query, m);
		 try{
			 ResultSet rs = qexec.execSelect() ;
			 ResultSetFormatter.out(System.out, rs, query);
		 }finally{
			 qexec.close();
		 }
		 
	}
}
