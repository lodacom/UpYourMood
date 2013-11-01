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

	public GraphViz gv;
	public ArrayList<String> pochettes;
	public long nbreStruct=0;
	
	public HyperGraph(){
		gv = new GraphViz();
		pochettes=new ArrayList<String>();
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
		System.out.println(pochette+"->"+mot);
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

		if (!pochettes.contains(host)){
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
				pochettes.add(host);
				nbreStruct++;
				writeFile = new FileOutputStream("public/pochette/"+fileName);
				byte[] buffer = new byte[1024];
				int read;

				while ((read = input.read(buffer)) > 0)
					writeFile.write(buffer, 0, read);
				writeFile.flush();
				
				gv.addln("struct"+nbreStruct+" [margin=0 shape=box, style=filled, fillcolor=white, color=red, label=" +
						"<<table border=\"0\" cellborder=\"0\">"+
						"<tr><td><img scal=\"true\" fixedsize=\"true\" width=\"70\" height=\"100\" src=\"public/pochette/"+fileName+"\"/>" +
						"</td></tr>"+
						"</table>>];");
			}
			catch (IOException e){
				System.out.println("Error while trying to download the file.");
			}
			finally{
				try{
					writeFile.close();
					input.close();
				}
				catch (IOException e){

				}
			}
		}else{
			URL url;
			try {
				url = new URL(host);
				String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
				gv.addln("struct"+nbreStruct+" [margin=0 shape=box, style=filled, fillcolor=white, color=red, label=" +
						"<<table border=\"0\" cellborder=\"0\">"+
						"<tr><td><img scal=\"true\" fixedsize=\"true\" width=\"70\" height=\"100\" src=\"public/pochette/"+fileName+"\"/>" +
						"</td></tr>"+
						"</table>>];");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
