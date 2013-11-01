package models;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

public class UpQueries {

	public static final String NL = System.getProperty("line.separator");
	public final String prolog1 = "PREFIX foaf: <"+FOAF.getURI()+">";
	public final String prolog2 = "PREFIX dc: <"+DC.getURI()+">";
	public final String prolog3 = "PREFIX rdf: <"+RDF.getURI()+">";
	public final String prolog4 = "PREFIX music: <http://www.upyourmood.com/music/>";
	public final String prolog5 = "PREFIX user: <http://www.upyourmood.com/user/>";
	public final String prolog6 = "PREFIX wordconnotation: <http://www.upyourmood.com/wordconnotation/>";
	public final String prolog7 = "PREFIX nicetag: <http://ns.inria.fr/nicetag/2010/09/09/voc.html#>";
	private Model m;

	public UpQueries(){
		m = ModelFactory.createOntologyModel();
		String fil_URL = "file:public/rdf/upyourmood.rdf";
		m.read(fil_URL);
	}

	public ResultSet hyperGraph(){
		ResultSet rs=null;
		String req1=prolog5 + NL + prolog7 + NL + prolog6 + NL + prolog1 + NL +
				"SELECT ?mot ?pochette " +
				"WHERE { " +
				"?user user:hasMusicalExperience ?experi . " +
				"?experi nicetag:makesMeFeel ?chose . " +
				"?chose wordconnotation:isAssociatedBy ?mot . " +
				"?experi user:hasListen ?idMusic . " +
				"?idMusic2 foaf:depiction ?pochette ." +
				"FILTER regex(str(?idMusic2) , ?idMusic) "+
				"}";
		Query query = QueryFactory.create(req1);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		try{
			rs = qexec.execSelect() ;
			ResultSetFormatter.out(System.out, rs, query);
		}finally{
			qexec.close();
		}
		return rs;
	}

	public ResultSet hyperGraphOfAUser(String pseudo){
		ResultSet rs=null;
		String req2=prolog5 + NL + prolog7 + NL + prolog6 + NL + prolog1 + NL +
				"SELECT ?mot ?pochette " +
				"WHERE { " +
				"?user user:hasMusicalExperience ?experi . " +
				"?experi nicetag:makesMeFeel ?chose . " +
				"?chose wordconnotation:isAssociatedBy ?mot . " +
				"?experi user:hasListen ?idMusic . " +
				"?idMusic2 foaf:depiction ?pochette ." +
				"FILTER regex(str(?idMusic2) , ?idMusic) . "+
				"FILTER regex(str(?user),"+pseudo+") "+
				"}";
		Query query = QueryFactory.create(req2);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		try{
			rs = qexec.execSelect() ;
		}finally{
			qexec.close();
		}
		return rs;
	}
	
	public void truc(){
		/*
		 * les mots rentrés par tous les utilisateurs 
		 * pour telle musique
		 */
		String req1=prolog5 + NL + prolog7 + NL + prolog6 + NL +
				"SELECT ?user ?mot ?truc " +
				"WHERE { " +
				"?user user:hasMusicalExperience ?experi . " +
				"?experi nicetag:makesMeFeel ?chose . " +
				"?chose wordconnotation:isAssociatedBy ?mot . " +
				"?experi user:hasListen ?truc" +
				"}";

		/*String req3=prolog7 + NL + prolog4 + NL + prolog3 + NL +
				"SELECT ?user " +
				"WHERE { " +
				"?user music:hasMusicalExperience ?music "+
				"}";*/
	}
}
