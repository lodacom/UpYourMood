package models;

import java.io.ByteArrayOutputStream;
import java.util.*;

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
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.function.library.e;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * <p>Classe qui permet d'interroger nos fichiers rdf.</p>
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
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
	public RDFBuildingColors rdfBC;
	public HashMap<Emotion,Integer> emotion;
	public HashMap<Think,Integer> think;
	private static String quoteCharacter="'";

	/**
	 * Constructeur dans lequel on instancie toutes les variables nécessaires.
	 */
	public UpQueries(){
		m = ModelFactory.createOntologyModel();
		String fil_URL = "file:public/rdf/upyourmood.rdf";
		m.read(fil_URL);
		hg=new HyperGraph();
		emotion=new HashMap<Emotion,Integer>();
		think=new HashMap<Think,Integer>();
		rdfBC=RDFBuildingColors.getInstance();
	}

	/**
	 * Fonction permettant de construire l'hyper-graphe de toutes les 
	 * expériences musicales de tous les utilisateurs
	 */
	public void hyperGraph(){

		ResultSet rs=null;
		String req1=prolog5 + NL + prolog7 + NL + prolog6 + NL + prolog1 + NL +
				"SELECT ?mot ?pochette ?image ?idMusic " +
				"WHERE { " +
				"?user user:hasMusicalExperience ?experi . " +
				"?experi nicetag:makesMeFeel ?chose . " +
				"?chose wordconnotation:isAssociatedBy ?mot . " +
				"?chose wordconnotation:makesMeThink ?image . " +
				"?experi user:hasListen ?idMusic . " +
				"?idMusic2 foaf:depiction ?pochette ." +
				"FILTER regex(str(?idMusic2) , ?idMusic) "+
				"} ";
		Query query = QueryFactory.create(req1);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		try{
			rs = qexec.execSelect() ;

			hg.startGraph();
			int i=0;
			while(rs.hasNext()) {
				QuerySolution sol = (QuerySolution) rs.next();
				String pochette=sol.get("?pochette").toString();
				String mot=sol.get("?mot").toString();
				String image=sol.get("?image").toString();
				String idMusic=sol.get("?idMusic").toString();
				String couleur=rdfBC.getMaxColorMusic(idMusic);
				emotion.put(new Emotion(pochette,mot,couleur), i);
				think.put(new Think(mot,image),i);
				i++;
			}
			hg.ajouterPochetteMotRelationImage(emotion,think);
			hg.endGraph("global");
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

		String userR = "<"+OntologyUpYourMood.getUymUser()+pseudo+">";

		String req2=prolog5 + NL + prolog7 + NL + prolog6 + NL + prolog1 + NL +
				"SELECT ?mot ?pochette ?image ?idMusic ?user " +
				"WHERE { " +
				userR+" user:hasMusicalExperience ?experi . " +
				"?experi nicetag:makesMeFeel ?chose . " +
				"?chose wordconnotation:isAssociatedBy ?mot . " +
				"?chose wordconnotation:makesMeThink ?image . " +
				"?experi user:hasListen ?idMusic . " +
				"?idMusic2 foaf:depiction ?pochette ." +
				"FILTER regex(str(?idMusic2) , ?idMusic) . "+
				"}";
		Query query = QueryFactory.create(req2);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		try{
			rs = qexec.execSelect() ;

			hg.startGraph();
			int i=0;
			while(rs.hasNext()) {
				QuerySolution sol = (QuerySolution) rs.next();
				String pochette=sol.get("?pochette").toString();
				String mot=sol.get("?mot").toString();
				String image=sol.get("?image").toString();
				String idMusic=sol.get("?idMusic").toString();
				String couleur=rdfBC.getMaxColorMusicByUser(idMusic,pseudo);
				emotion.put(new Emotion(pochette,mot,couleur), i);
				think.put(new Think(mot,image),i);
				i++;

			}
			hg.ajouterPochetteMotRelationImage(emotion,think);
			hg.endGraph(pseudo);
		}finally{
			qexec.close();
		}
	}

	/**
	 * <p>Simulation d'un "endpoint", mis à dispotion de l'utilisateur pour qu'il puisse lui aussi
	 * interroger nos fichiers rdf.</p>
	 * @param user_query Requête de l'utilisateur.
	 * @return un objet EndPointQueries contenant la réponse de la requête.
	 */
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

		List<Var> variables=query.getProjectVars();
		try{
			rs = qexec.execSelect() ;

			while(rs.hasNext()){
				QuerySolution sol = (QuerySolution) rs.next();
				for (int i=0;i<variables.size();i++){
					String rec=sol.get("?"+variables.get(i).getName()).toString();
					intermed.add(rec);
				}
				renvoie.add(intermed);
				intermed=new ArrayList<String>();
			}

		}finally{
			qexec.close();
		}
		EndPointQueries epq=new EndPointQueries();
		epq.response=renvoie;
		epq.headOfArray=new ArrayList<String>();
		for (int i=0;i<variables.size();i++){
			epq.headOfArray.add(variables.get(i).getName());
		}
		return epq;
	}

	public String userQueriesResponseFromRDF(String user_query){
		ResultSet rs=null;
		String rdf_response = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String req1=prolog1 + NL + prolog2 + NL + prolog3 + NL + prolog4 + NL +
				prolog5 + NL + prolog6 + NL + prolog7 + NL + user_query;
		Query query = QueryFactory.create(req1);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);

		try{
			rs = qexec.execSelect();
			ResultSetFormatter.outputAsRDF(baos, "RDF/XML", rs);
			rdf_response = baos.toString();		
			System.out.println(rdf_response);
		}finally{
			qexec.close();
		}
		return rdf_response;
	}

	/**
	 * Méthode permettant de sélectionner au plus les 10 musiques les mieux notées pour un utilisateur donné,
	 * afin qu'il puisse les réécouter.
	 * @param pseudo Pseudo de l'utilisateur pour lequel on veut avoir le top10.
	 */
	public void listenAgainJamendo(String pseudo){
		ResultSet rs=null;
		ResultSet rs2=null;
		String user = "<"+OntologyUpYourMood.getUymUser()+pseudo+">";

		ConnectionBase.open();
		ConnectionBase.requete("DROP TABLE IF EXISTS "+pseudo);
		ConnectionBase.requete("CREATE TABLE "+pseudo+" (idmusique character varying(255),"
				+ "image character varying(255))");


		// Requête permettant de récupérer les musiques les mieux notées pour un utilisateur donné.
		String queryAverageTop10 = prolog1 + NL + prolog2 + NL + prolog3 + NL + prolog4 + NL + prolog5 + NL + prolog6 + NL + prolog7 +
				"SELECT ?idmusique (AVG(?connotation) AS ?moyenne_connotation ) "+
				"WHERE { " +
				user+" user:hasMusicalExperience ?experi . " +
				"?experi nicetag:makesMeFeel ?chose . " +
				"?chose wordconnotation:isConnoted ?connotation . " +
				"?experi user:hasListen ?idmusique ."+
				"?chose wordconnotation:makesMeThink ?url_image ."+
				"}"+
				"GROUP BY ?idmusique ?user "+
				"ORDER BY DESC (?moyenne_connotation) "+
				"LIMIT 10";

		Query query = QueryFactory.create(queryAverageTop10);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);

		try{
			rs = qexec.execSelect() ;
			while(rs.hasNext()){
				QuerySolution sol = (QuerySolution) rs.next();
				String idmusique=sol.get("?idmusique").toString();

				// Requête permettant de récupérer l'ensemble des urls liées aux musiques
				String queryMusicLinksToImage = prolog1 + NL + prolog2 + NL + prolog3 + NL + prolog4 + NL + prolog5 + NL + prolog6 + NL + prolog7 +
						"SELECT ?idmusique ?url_image "+
						"WHERE { " +
						user+" user:hasMusicalExperience ?experi . " +
						"?experi nicetag:makesMeFeel ?chose . " +
						"?experi user:hasListen ?idmusique ."+
						"?chose wordconnotation:makesMeThink ?url_image " +
						"FILTER (str(?idmusique)=\""+idmusique+"\") "+
						"}";

				Query query2 = QueryFactory.create(queryMusicLinksToImage);
				QueryExecution qexec2 = QueryExecutionFactory.create(query2, m);

				try{
					rs2=qexec2.execSelect();
					while(rs2.hasNext()){
						QuerySolution sol2 = (QuerySolution) rs2.next();
						String idmusique2=sol2.get("?idmusique").toString();
						String image=sol2.get("?url_image").toString();
						//System.out.println(idmusique2+" "+image);
						ConnectionBase.requete("INSERT INTO "+pseudo+" (idmusique,image) "
								+ "VALUES ("+quoteCharacter+idmusique2+quoteCharacter+","+
								quoteCharacter+image+quoteCharacter+")");
					}
				}finally{
					qexec2.close();
				}
			}
		}finally{
			qexec.close();
		}

		ConnectionBase.close();
	}
}
