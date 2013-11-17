package models;


import java.util.ArrayList;
import java.util.List;

import models.graphviz.HyperGraph;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.function.library.e;
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
	private HyperGraph hg;
	
	public UpQueries(){
		m = ModelFactory.createOntologyModel();
		String fil_URL = "file:public/rdf/upyourmood.rdf";
		m.read(fil_URL);
		hg=new HyperGraph();
	}

	/**
	 * Fonction permettant de construire l'hyper-graphe de toutes les 
	 * expériences musicales de tous les utilisateurs
	 */
	public void hyperGraph(){
		ResultSet rs=null;
		String req1=prolog5 + NL + prolog7 + NL + prolog6 + NL + prolog1 + NL +
				"SELECT ?mot ?pochette ?image " +
				"WHERE { " +
				"?user user:hasMusicalExperience ?experi . " +
				"?experi nicetag:makesMeFeel ?chose . " +
				"?chose wordconnotation:isAssociatedBy ?mot . " +
				"?chose wordconnotation:makesMeThink ?image . " +
				"?experi user:hasListen ?idMusic . " +
				"?idMusic2 foaf:depiction ?pochette ." +
				"FILTER regex(str(?idMusic2) , ?idMusic) "+
				"}";
		Query query = QueryFactory.create(req1);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		try{
			rs = qexec.execSelect() ;
			
			hg.startGraph();
			while(rs.hasNext()) {
				QuerySolution sol = (QuerySolution) rs.next();
				String pochette=sol.get("?pochette").toString();
				String mot=sol.get("?mot").toString();
				String image=sol.get("?image").toString();
				//System.out.println(pochette+"->"+mot);
				hg.ajouterPochetteMotRelation(pochette, mot);
				hg.ajouterMotImageRelation(mot, image);
			}
			hg.endGraph();
		}finally{
			qexec.close();
		}
	}
	
	/**
	 * Fonction permettant de construire l'hyper-graphe de toutes les 
	 * expériences musicales de l'utilisateur en param
	 * @param pseudo l'utilisateur courant
	 */
	public void hyperGraphOfAUser(String pseudo){
		ResultSet rs=null;
		String req2=prolog5 + NL + prolog7 + NL + prolog6 + NL + prolog1 + NL +
				"SELECT ?mot ?pochette " +
				"WHERE { " +
				"?user user:hasMusicalExperience ?experi . " +
				"?experi nicetag:makesMeFeel ?chose . " +
				"?chose wordconnotation:isAssociatedBy ?mot . " +
				"?chose wordconnotation:makesMeThink ?image . " +
				"?experi user:hasListen ?idMusic . " +
				"?idMusic2 foaf:depiction ?pochette ." +
				"FILTER regex(str(?idMusic2) , ?idMusic) . "+
				"FILTER regex(str(?user),"+pseudo+") "+
				"}";
		Query query = QueryFactory.create(req2);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		try{
			rs = qexec.execSelect() ;
			
			hg.startGraph();
			while(rs.hasNext()) {
				QuerySolution sol = (QuerySolution) rs.next();
				String pochette=sol.get("?pochette").toString();
				String mot=sol.get("?mot").toString();
				hg.ajouterPochetteMotRelation(pochette, mot);
			}
			hg.endGraph();
		}finally{
			qexec.close();
		}
	}
	
	public EndPointQueries userQueriesFromEndPoint(String user_query){
		/*
		 * SELECT DISTINCT ?user WHERE { ?user user:hasMusicalExperience ?experi }
		 */
		ResultSet rs=null;
		String req1=prolog1 + NL + prolog2 + NL + prolog3 + NL + prolog4 + NL +
				prolog5 + NL + prolog6 + NL + prolog7 + NL + user_query;
		Query query = QueryFactory.create(req1);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		ArrayList<ArrayList<String>> renvoie=new ArrayList<ArrayList<String>>();
		ArrayList<String> intermed=new ArrayList<String>();
		//System.out.println(user_query);
		String[] recup;
		try{
			rs = qexec.execSelect() ;
			String req=user_query.replaceAll("SELECT DISTINCT |SELECT REDUCED |SELECT ", "");
			req=req.replaceAll("WHERE .*", "");
			recup=req.split("\\s");
			while(rs.hasNext()){
				QuerySolution sol = (QuerySolution) rs.next();
				for (int i=0;i<recup.length;i++){
					String rec=sol.get(recup[i]).toString();
					intermed.add(rec);
				}
				renvoie.add(intermed);
			}
			
		}finally{
			qexec.close();
		}
		EndPointQueries epq=new EndPointQueries();
		epq.response=renvoie;
		epq.headOfArray=new ArrayList<String>();
		for (int i=0;i<recup.length;i++){
			epq.headOfArray.add(recup[i].substring(1));
		}
		return epq;
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
