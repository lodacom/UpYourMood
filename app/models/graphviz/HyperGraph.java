package models.graphviz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import models.ConnectionBase;
import models.Emotion;
import models.Think;


public class HyperGraph {

	private GraphViz gv;
	private HashMap<String,Integer> hosts;
	private HashMap<String,Integer> hostsImages;
	private String quoteCharacter="'";
	private int nbreStruct=0;
	private int nbreStructImage=0;
	
	public HyperGraph(){
		gv = new GraphViz();
		hosts=new HashMap<String,Integer>();
		hostsImages=new HashMap<String,Integer>();
	}

	/**
	 * Premet de faire commencer le graphe
	 */
	public void startGraph(){
		gv.addln(gv.start_graph());
	}

	/**
	 * Permet d'ajouter la relation pochette d'album vers le mot
	 * @param pochette la pochette en question
	 * @param mot le mot en question
	 */
	public void ajouterPochetteMotRelationImage(HashMap<Emotion,Integer> emotion,HashMap<Think,Integer> think){
		Set<Emotion> momo=emotion.keySet();
		Iterator<Emotion> iterMomo=momo.iterator();
		while(iterMomo.hasNext()){
			Emotion emo=iterMomo.next();
			int index=getFile(emo.getPochette(),emo.getCouleur());
			gv.addln("struct"+index+"->"+emo.getMot());
		}
		Set<Think> pense=think.keySet();
		Iterator<Think> iterPense=pense.iterator();
		while(iterPense.hasNext()){
			Think p=iterPense.next();
			int index2=getFileImage(p.getImage());
			gv.addln(p.getMot()+"->imag"+index2);
		}
	}
	
	/**
	 * Permet de finir le graphe et de fabriquer l'image résultante
	 */
	public void endGraph(String nom_graph){
		gv.addln(gv.end_graph());
		//System.out.println(gv.getDotSource());
		String type = "jpg";
		String representationType="circo";
		File out = new File("public/graph/hypergraph_"+nom_graph+"." + type);
		gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type , representationType), out );
	}
	
	private String getColor(String hexadecimal){
		ConnectionBase.open();
		String retour="";
		ResultSet res=ConnectionBase.requete("SELECT color FROM colors WHERE hexadecimal="+quoteCharacter+hexadecimal+quoteCharacter);
		try {
			while (res.next()){
				retour=res.getString("color");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConnectionBase.close();
		return retour;
	}
	
	private int getFile(String host,String couleur)
	{

		InputStream input = null;
		FileOutputStream writeFile = null;
		try
		{
			if (!hosts.containsKey(host)){
				URL url = new URL(host);
				URLConnection connection = url.openConnection();
				int fileLength = connection.getContentLength();

				if (fileLength == -1){
					System.out.println("Invalid URL or file.");
				}

				input = connection.getInputStream();
				String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);

				nbreStruct++;
				hosts.put(host, nbreStruct);
				writeFile = new FileOutputStream("public/pochette/"+nbreStruct+fileName);
				byte[] buffer = new byte[1024];
				int read;

				while ((read = input.read(buffer)) > 0)
					writeFile.write(buffer, 0, read);
				writeFile.flush();
				
				String recup=getColor(couleur);
				gv.addln("struct"+nbreStruct+" [margin=0 shape=box, style=filled, fillcolor=white, color="+recup+", label=" +
						"<<table border=\"0\" cellborder=\"0\">"+
						"<tr><td fixedsize=\"true\" width=\"50\" height=\"80\"><img scale=\"true\" src=\"public/pochette/"+nbreStruct+fileName+"\"/>" +
						"</td></tr>"+
						"</table>>];");

				try{
					writeFile.close();
					input.close();
				}
				catch (IOException e){

				}
			}
		}
		catch (IOException e){
			System.out.println("Error while trying to download the file.");
		}
		return hosts.get(host);
	}
	
	private int getFileImage(String host)
	{

		InputStream input = null;
		FileOutputStream writeFile = null;
		try
		{
			if (!hostsImages.containsKey(host)){
				URL url = new URL(host);
				URLConnection connection = url.openConnection();
				int fileLength = connection.getContentLength();

				if (fileLength == -1){
					System.out.println("Invalid URL or file.");
				}

				input = connection.getInputStream();
				String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);

				nbreStructImage++;
				hostsImages.put(host, nbreStructImage);
				writeFile = new FileOutputStream("public/downloadImages/"+fileName);
				byte[] buffer = new byte[1024];
				int read;

				while ((read = input.read(buffer)) > 0)
					writeFile.write(buffer, 0, read);
				writeFile.flush();

				gv.addln("imag"+nbreStructImage+" [margin=0 shape=box, style=filled, fillcolor=white, label=" +
						"<<table border=\"0\" cellborder=\"0\">"+
						"<tr><td fixedsize=\"true\" width=\"50\" height=\"80\"><img scale=\"true\" src=\"public/downloadImages/"+fileName+"\"/>" +
						"</td></tr>"+
						"</table>>];");

				try{
					writeFile.close();
					input.close();
				}
				catch (IOException e){

				}
			}
		}
		catch (IOException e){
			System.out.println("Error while trying to download the file.");
		}
		return hostsImages.get(host);
	}
}
