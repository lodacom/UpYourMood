package models.graphviz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import models.UpQueries;

public class HyperGraph {

	public GraphViz gv;
	public UpQueries uq;
	public ArrayList<String> pochettes;
	public long nbreStruct=0;
	
	public HyperGraph(){
		gv = new GraphViz();
		uq =new UpQueries();
		pochettes=new ArrayList<String>();
	}

	/*
	 * struct1 [margin=0 shape=box, style=bold,filled, fillcolor=white,label=<<TABLE border="0" cellborder="0">
<TR><TD><IMG SCALE="true" FIXEDSIZE="true" SRC="public/pochette/image_name.jpg"/></TD></TR>
</TABLE>>]
	 */
	public void buildHyperGraph(){
		gv.addln(gv.start_graph());
		ResultSet results=uq.hyperGraph();
		if (results!=null){
			System.out.println("What?!");
		}
		if (results.hasNext()){
			System.out.println("Requête renvoie ok");
		}else{
			System.out.println("WTF!");
		}
		while(results.hasNext()) {
			System.out.println("coucou");
			QuerySolution sol = (QuerySolution) results.next();
			String pochette=sol.get("?pochette").toString();
			String mot=sol.get("?mot").toString();
			System.out.println(pochette+"->"+mot);
			getFile(pochette);
			gv.addln("struct"+nbreStruct+"->"+mot);
		}
		gv.addln(gv.end_graph());
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
				
				gv.addln("struct"+nbreStruct+" [margin=0 shape=box, style=bold,filled,fillcolor=white,label=" +
						"<<TABLE border=\"0\" cellborder=\"0\">"+
						"<TR><TD><IMG SCALE=\"true\" FIXEDSIZE=\"true\" WIDTH=\"70\" HEIGHT=\"100\" SRC=\"public/pochette/"+fileName+"\"/>" +
						"</TD></TR>"+
						"</TABLE>>]");
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
		}
	}
}
