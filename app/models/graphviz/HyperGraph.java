package models.graphviz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import models.UpQueries;


public class HyperGraph {

	private GraphViz gv;
	private ArrayList<String> hosts;
	private ArrayList<String> hostsImages;
	private HashMap<String,String> pochetteMot;
	private HashMap<String,String> motImage;
	private long nbreStruct=0;
	private long nbreStructImage=0;
	
	public HyperGraph(){
		gv = new GraphViz();
		hosts=new ArrayList<String>();
		hostsImages=new ArrayList<String>();
		pochetteMot=new HashMap<String,String>();
		motImage=new HashMap<String, String>();
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
	public void ajouterPochetteMotRelationImage(){
		ArrayList<ArrayList<String>> recup=UpQueries.pochetteMotImage;
		int i=0;
		String mot="",pochette="",image;
		for (ArrayList<String> tab: recup){
			for (String rec: tab){
				switch (i) {
				case 0:
					pochette=rec;
					getFile(pochette);
					i++;
					continue;
				case 1:
					mot=rec;
					if (pochetteMot.get(pochette)==null){
						gv.addln("struct"+nbreStruct+"->"+mot);
						pochetteMot.put(pochette, mot);
					}else{
						if (!pochetteMot.get(pochette).equals(mot)){
							gv.addln("struct"+nbreStruct+"->"+mot);
							pochetteMot.put(pochette, mot);
						}
					}
					i++;
					continue;
				case 2:
					image=rec;
					if (motImage.get(mot)==null){
						getFileImage(image);
						gv.addln(mot+"->imag"+nbreStructImage);
						motImage.put(mot, image);
					}else{
						if (!motImage.get(mot).equals(image)){
							getFileImage(image);
							gv.addln(mot+"->imag"+nbreStructImage);
							motImage.put(mot, image);
						}
					}
					i=0;
					continue;
				}
			}
		}
	}
	
	/**
	 * Permet de finir le graphe et de fabriquer l'image résultante
	 */
	public void endGraph(){
		gv.addln(gv.end_graph());
		//System.out.println(gv.getDotSource());
		String type = "jpg";
		String representationType="circo";
		File out = new File("public/graph/hypergraph." + type);
		gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type , representationType), out );
	}
	
	private void getFile(String host)
	{

		InputStream input = null;
		FileOutputStream writeFile = null;
		try
		{
			if (!hosts.contains(host)){
				hosts.add(host);
				URL url = new URL(host);
				URLConnection connection = url.openConnection();
				int fileLength = connection.getContentLength();

				if (fileLength == -1){
					System.out.println("Invalid URL or file.");
				}

				input = connection.getInputStream();
				String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);

				nbreStruct++;
				writeFile = new FileOutputStream("public/pochette/"+nbreStruct+fileName);
				byte[] buffer = new byte[1024];
				int read;

				while ((read = input.read(buffer)) > 0)
					writeFile.write(buffer, 0, read);
				writeFile.flush();

				gv.addln("struct"+nbreStruct+" [margin=0 shape=box, style=filled, fillcolor=white, color=red, label=" +
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
	}
	
	private void getFileImage(String host)
	{

		InputStream input = null;
		FileOutputStream writeFile = null;
		try
		{
			if (!hostsImages.contains(host)){
				hostsImages.add(host);
				URL url = new URL(host);
				URLConnection connection = url.openConnection();
				int fileLength = connection.getContentLength();

				if (fileLength == -1){
					System.out.println("Invalid URL or file.");
				}

				input = connection.getInputStream();
				String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);

				nbreStructImage++;
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
	}
}
