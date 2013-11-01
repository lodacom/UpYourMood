package models.graphviz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class HyperGraph {

	private GraphViz gv;
	private ArrayList<String> pochettesGraph;
	private long nbreStruct=0;

	public HyperGraph(){
		gv = new GraphViz();
		pochettesGraph=new ArrayList<String>();
	}

	/*
	 * struct1 [margin=0 shape=box, style=bold,filled, fillcolor=white,label=<<TABLE border="0" cellborder="0">
<TR><TD><IMG SCALE="true" FIXEDSIZE="true" SRC="public/pochette/image_name.jpg"/></TD></TR>
</TABLE>>]
	 */

	public void startGraph(){
		gv.addln(gv.start_graph());
	}

	public void ajouterPochetteMotRelation(String pochette,String mot){
		getFile(pochette);
		gv.addln("struct"+nbreStruct+"->"+mot+";");
	}

	public void endGraph(){
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());
		String type = "jpg";
		File out = new File("public/graph/hypergraph." + type);
		gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}

	private void getFile(String host)
	{

		InputStream input = null;
		FileOutputStream writeFile = null;
		try
		{
			URL url = new URL(host);
			URLConnection connection = url.openConnection();
			int fileLength = connection.getContentLength();

			if (fileLength == -1){
				System.out.println("Invalid URL or file.");
			}

			input = connection.getInputStream();
			String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
			File pochette = new File("public/pochette/"+fileName);
			if (!pochette.exists()){
				nbreStruct++;
				writeFile = new FileOutputStream("public/pochette/"+fileName);
				byte[] buffer = new byte[1024];
				int read;

				while ((read = input.read(buffer)) > 0)
					writeFile.write(buffer, 0, read);
				writeFile.flush();

				gv.addln("struct"+nbreStruct+" [margin=0 shape=box, style=filled, fillcolor=white, color=red, label=" +
						"<<table border=\"0\" cellborder=\"0\">"+
						"<tr><td fixedsize=\"true\" width=\"50\" height=\"80\"><img scale=\"true\" src=\"public/pochette/"+fileName+"\"/>" +
						"</td></tr>"+
						"</table>>];");
				pochettesGraph.add(fileName);
				
				try{
					writeFile.close();
					input.close();
				}
				catch (IOException e){

				}
			}else{
				if (!pochettesGraph.contains(fileName)){
					nbreStruct++;
					gv.addln("struct"+nbreStruct+" [margin=0 shape=box, style=filled, fillcolor=white, color=red, label=" +
							"<<table border=\"0\" cellborder=\"0\">"+
							"<tr><td fixedsize=\"true\" width=\"50\" height=\"80\"><img scale=\"true\" src=\"public/pochette/"+fileName+"\"/>" +
							"</td></tr>"+
							"</table>>];");
					pochettesGraph.add(fileName);
				}
			}
		}
		catch (IOException e){
			System.out.println("Error while trying to download the file.");
		}
	}
}
