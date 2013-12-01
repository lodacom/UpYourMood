package models;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.management.ObjectName;

import org.apache.jena.riot.RiotException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * <p>Classe permettant de construire le fichier uymcolors.rdf.</p>
 * @author BURC Pierre, DUPLOUY Olivier, KISIALIOVA Katsiaryna, SEGUIN Tristan
 *
 */
public class RDFBuildingColors {

	private static RDFBuildingColors instance;
	private static Model m = null;
	private static final String rdf_filecolors = "public/rdf/uymcolors.rdf";

	private static String prefixs = null;

	/**
	 * Constructeur qui charge le fichier rdf.
	 * @throws RiotException Si le modèle est null.
	 */
	private RDFBuildingColors(){
		try{
			m=FileManager.get().loadModel(rdf_filecolors);
		}catch(RiotException e){
			m = ModelFactory.createDefaultModel();
		}		
	}

	/**
	 * <p> Récupérer l'instance de RDFBuildingCoolors, ou la créer avec les préfixes nécessaires.</p>
	 * @return l'instance de RDFBuildingColors. 
	 */
	public final static RDFBuildingColors getInstance() {
		if (RDFBuildingColors.instance == null) {
			synchronized(RDFBuildingColors.class) {
				if (RDFBuildingColors.instance == null) {
					RDFBuildingColors.instance = new RDFBuildingColors();
					m.setNsPrefix("uym", OntologyUpYourMood.getUym());
					m.setNsPrefix("music", OntologyUpYourMood.getUymMusic());
					m.setNsPrefix("user", OntologyUpYourMood.getUymUser());
					m.setNsPrefix("rdf", RDF.getURI());
					m.setNsPrefix("color", OntologyUpYourMood.getUymColor());
					String NL = System.getProperty("line.separator");
					prefixs = "PREFIX uym: <"+OntologyUpYourMood.getUym()+">"+NL+"PREFIX music: <"+OntologyUpYourMood.getUymMusic()+">"+NL+"PREFIX user: <"+OntologyUpYourMood.getUymUser()+">"+NL+"PREFIX color: <"+OntologyUpYourMood.getUymColor()+">"+NL;

				}
			}
		}
		return RDFBuildingColors.instance;
	}

	/**
	 * <p>Méthode qui ajoute une couleur pour une musique donnée, si celle-ci n'a pas déjà été associée par
	 * cet utilisateur pour cette musique.</p>
	 * @param music_number L'identifiant de la musique écoutée actuellement.
	 * @param pseudo Le pseudo de l'utilisateur qui écoute la musique.
	 * @param color_value La couleur choisie par l'utilisateur pour cette musique.
	 * @see RDFBuildingColors#incrExistColor(String, String, String)
	 */
	public void rdfUYMAddColor(String music_number, String pseudo, String color_value){
		if (!this.incrExistColor(music_number, pseudo, color_value)){
			this.addColor(music_number, pseudo, color_value);
		}
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream("public/rdf/uymcolors.rdf");
			m.write(outStream, "RDF/XML-ABBREV");
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * <p>Méthode qui permet de vérifier si c'est la première fois que cet utilisateur choisit cette couleur
	 * pour cette musique. Si cette couleur a déjà été choisie, alors le rdf sera mis à jour dynamiquement,
	 * le nombre de fois que cette couleur a été choisi par cet utilisateur sera incrémenté de 1.</p>
	 * @param music_number L'identifiant de la musique en cours.
	 * @param pseudo Le pseudo de l'utilisateur qui écoute la musique.
	 * @param color_value La couleur choisie par l'utilisateur.
	 * @return vrai si la couleur était déjà présente pour cette musique, faux sinon.
	 */
	private Boolean incrExistColor(String music_number, String pseudo, String color_value){
		Resource Color = m.getResource(OntologyUpYourMood.getUymColor()+music_number+"/"+pseudo+"/"+color_value);
		Property prop = m.getProperty(OntologyUpYourMood.getUymColor()+"isSelected");
		Statement stmt = m.getProperty(Color, prop);

		if (stmt!=null){
			RDFNode object = stmt.getObject();
			stmt.changeLiteralObject(object.asLiteral().getInt()+1);
			return true;
		}

		return false;
	}

	/**
	 * Méthode qui ajoute au modèle les informations nécessaires et enrichit donc le fichier uymcolors.rdf.
	 * @param music_number L'identifiant de la musique en cours.
	 * @param pseudo Le pseudo de l'utilisateur qui écoute la musique.
	 * @param color_value La couleur choisie.
	 */
	private void addColor(String music_number, String pseudo, String color_value){
		OntologyUpYourMood.Music = m.createResource(OntologyUpYourMood.getUymMusic()+music_number);
		OntologyUpYourMood.User = m.createResource(OntologyUpYourMood.getUymUser()+pseudo);
		OntologyUpYourMood.Color = m.createResource(OntologyUpYourMood.getUymColor()+music_number+"/"+pseudo+"/"+color_value);
		m.add(OntologyUpYourMood.Music, OntologyUpYourMood.isColoredBy, OntologyUpYourMood.Color);
		m.add(OntologyUpYourMood.Color, OntologyUpYourMood.givenBy, OntologyUpYourMood.User);
		m.add(m.createLiteralStatement(OntologyUpYourMood.Color,  OntologyUpYourMood.isSelected, 1));
		m.add(m.createLiteralStatement(OntologyUpYourMood.Color,  OntologyUpYourMood.hasValue, color_value));
	}

	/**
	 * <p>Méthode dans laquelle on récupère la couleur la plus de fois "cochée" par l'ensemble des utilisateurs
	 * pour une musique donnée en paramètre. Cette couleur est récupérée via une requête SPARQL effectuée 
	 * sur le fichier uymcolors.rdf.</p>
	 * @param music_number La musique pour laquelle on souhaite récupérer la couleur la plus de fois sélectionnée.
	 * @return la couleur ayant été sélectionné le plus de fois par l'ensemble des utilisateurs.
	 */
	public String getMaxColorMusic(String music_number){
		String maxcolor = "#FFFFFF";
		Integer value = 0;
		String music = "<"+OntologyUpYourMood.getUymMusic()+music_number+">";

		String str = prefixs + 
				"SELECT ?value (SUM(?times) AS ?t) WHERE {"+
				music+" music:isColoredBy ?c ."+
				"?c color:hasValue ?value ."+
				"?c color:isSelected ?times ."+
				"} GROUP BY ?value";
		Query query = QueryFactory.create(str);
		QueryExecution execquery = QueryExecutionFactory.create(query, m);
		ResultSet rs = execquery.execSelect() ;
		while (rs.hasNext())
		{ 
			QuerySolution s = rs.nextSolution();
			RDFNode r=s.get("?t");
			if (r!=null){
				Integer v = s.get("?t").asLiteral().getInt();
				
				if (v > value){
					value = v;
					maxcolor = s.get("?value").asLiteral().getString();
				}

			}
		}
		execquery.close();
		return maxcolor;
	}

	/**
	 * <p>Méthode qui exécute la même requête SPARQL que précédemment, à la différence près
	 * que l'utilisateur est également mis en paramètre. On cherche donc cette fois à savoir pour un
	 * utilisateur donné quelle est la couleur qu'il a le plus de fois sélectionné pour une musique donnée.</p>
	 * @param music_number L'identifiant de la musique pour laquelle on veut récupérer sa couleur "max".
	 * @param pseudo L'utilisateur en lien avec la musique choisie.
	 * @return la couleur sélectionnée le plus de fois par l'utilisateur pour la musique donnée.
	 * @see RDFBuildingColors#getMaxColorMusic(String)
	 */
	public String getMaxColorMusicByUser(String music_number, String pseudo){
		String maxcolor = "#FFFFFF";
		Integer value = 0;
		String music = "<"+OntologyUpYourMood.getUymMusic()+music_number+">";
		String user = "<"+OntologyUpYourMood.getUymUser()+pseudo+">";

		String str = prefixs + 
				"SELECT ?value ?times WHERE {"+
				music+" music:isColoredBy ?c ."+
				"?c color:hasValue ?value ."+
				"?c color:isSelected ?times ."+
				"?c user:givenBy "+ user +" ."+
				"}";
		Query query = QueryFactory.create(str);
		QueryExecution execquery = QueryExecutionFactory.create(query, m);
		ResultSet rs = execquery.execSelect() ;
		while (rs.hasNext())
		{ 
			QuerySolution s = rs.nextSolution();
			RDFNode r=s.get("?times");
			if (r!=null){
				Integer v = s.get("?times").asLiteral().getInt();
			
				if (v > value){
					value = v;
					maxcolor = s.get("?value").asLiteral().getString();
				}
			}
		}
		execquery.close();
		return maxcolor;
	}
}
