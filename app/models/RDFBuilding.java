package models;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;

public class RDFBuilding {
	private static volatile RDFBuilding instance = null;
	private static final String rdf_file = "public/rdf/upyourmood.rdf";
	private final String prefixe = "http://www.upyourmood.org/";
	private Model m;
	
	private RDFBuilding(){
		m=FileManager.get().loadModel(rdf_file);
		m.getNsPrefixURI("music");
	}
	
	public final static RDFBuilding getInstance() {
        if (RDFBuilding.instance == null) {
           synchronized(RDFBuilding.class) {
             if (RDFBuilding.instance == null) {
            	 RDFBuilding.instance = new RDFBuilding();
             }
           }
        }
        return RDFBuilding.instance;
    }
	
	/**
	 * fonction principale qu'il faudra appeler dans la classe Application
	 */
	public void rdfUpYourMood(){
		ajouterMusique();
		ajouterUtilisateur();
		ajouterMot_Connotation();
	}
	
	/**
	 * Dans cette fonction on ajoute:
	 * <ul>
	 * 	<li>l'identifiant de la musique</li>
	 * 	<li>titre de la musique</li>
	 * 	<li>titre de l'album</li>
	 * 	<li>nom de l'artiste</li>
	 * 	<li>url de la photo de l'album</li>
	 * </ul>
	 */
	private void ajouterMusique(){
		
	}
	
	/**
	 * Dans cette fonction on ajoute:
	 * a minima:
	 * <ul>
	 * 	<li>le pseudo</li>
	 * </ul>
	 */
	private void ajouterUtilisateur(){
		
	}
	
	private void ajouterMot_Connotation(){
		
	}
}
