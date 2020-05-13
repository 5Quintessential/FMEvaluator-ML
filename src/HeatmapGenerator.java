import java.io.*;
import java.util.Scanner;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.List;
import java.util.LinkedList;

// Features in the feature model
class Vertex {
  public int    id;
  public String name;
  public float  confidence;
}
  
// Relationships in the feature model
class Edge {
  public int  source;
  public int  target;
  public String type;
  public float confidence;
}

public class HeatmapGenerator {
  
  public static void main(String[] args) 
  {
  	// Check the number of parameters
  	// Two arguments expected 
  	// args[0] -- Input specification
  	// args[1] -- Output GraphViz file for the feature model
  	if (args.length != 2) {
  		System.out.println("Wrong number of parameters");
  		System.out.println("Usage");
  		System.out.println("Usage");  	
  		System.exit(1);
  	}
  	
  	// Open input and output files
  	String inFileName  = args[0];
  	String outFileName = args[1];
  	File inFile  = new File(inFileName);
  	File outFile = new File(outFileName);
  	Scanner reader = null;
  	try {
  		reader = new Scanner(inFile);
  	} catch (FileNotFoundException e) {
  		System.err.println("Error opening input file '" + inFileName + "': " + e.toString());
		System.exit(1);	
  	}
  	FileWriter writer = null;
  	try {
  		writer = new FileWriter(outFile); 	
    } catch (IOException e) {
    	System.err.println("Error creating output file '" + outFileName + "': " + e.toString());
    }
  
  	// Read the contents of the input file
  	List<Vertex> vertices = null;
  	List<Edge> edges = null;
  	try {
		vertices = readVertices(reader);
		edges = readEdges(reader); 
	} catch (IOException e) {
		System.err.println("Error reading input file '" + inFileName + "': " + e.toString());
		System.exit(1);
	}
	
	// Write the feature model in GraphViz format
	try {
		writeHeader(writer);
		writeVertices(vertices, writer);
		writeEdges(vertices, edges, writer);
		writeEpilogue(writer);
		writer.flush();
		writer.close();
	} catch (IOException e) {
		System.err.println("Error writing output file '" + outFileName + "':" + e.toString());
		System.exit(1);
	}
  }
	
	// Generate color string in a heatmap
	// Value from 0 to 1 (1 is higher)
	private static String colorString(float value) {
		float number =  (value/2) + 0.5f;
		//float number = value;
		String numberAsString = String.format(Locale.UK, "%.3f", number);
		return numberAsString + " 1.0 1.0";
	}
	
	private static List<Vertex> readVertices(Scanner in) throws IOException {
		List<Vertex> list = new LinkedList<Vertex>();
		
		// Delimiters used to scan for tokens
		Pattern whitespaces = Pattern.compile("[ \t\r\n]*");		
		in.useDelimiter(whitespaces);
		
		// Read all vertices
		int id = 1;
		String line = in.nextLine();
		while (!line.isEmpty()) {
			Vertex v = new Vertex();
			Scanner thisLine = new Scanner(line);
			try {
				v.id = thisLine.nextInt(); 
				System.out.println("Id ok " + v.id);
				v.name= thisLine.next();
				System.out.println("Name ok " + v.name);
				v.confidence = Float.parseFloat(thisLine.next());
				System.out.println("Confidence ok" + v.confidence);
			} catch (Exception e) {
				System.err.println("Error reading vertex " + id + " from input file: " + e.toString());
				System.exit(1);
			}
			list.add(v);
			id++;
			line = in.nextLine();
		}	
		
		return list;
	}
	
	private static List<Edge> readEdges(Scanner in) throws IOException {
		List<Edge> list = new LinkedList<Edge>();
			
		// Read all edges
		int id = 1;
		
		String line;
		while (in.hasNextLine()) {
			line = in.nextLine();
			if (line.isEmpty()) break;
			Scanner thisLine = new Scanner(line);
			Edge edge = new Edge();
			try {
				edge.source = thisLine.nextInt();
				edge.target = thisLine.nextInt();
				edge.type = thisLine.next();
				edge.confidence = Float.parseFloat(thisLine.next());
			} catch (Exception e) {
				System.err.println("Error reading edge " + id + " from input file: " + e.toString());
				System.exit(1);
			}
			list.add(edge);
			id++;
		}
	
		return list;
	}
	 	  
  	private static void writeHeader(FileWriter out) throws IOException {
  		String eol = System.lineSeparator();
  		out.write("digraph FeatureModel {" + eol + eol);
		out.write("\t rankdir = TB; splines = \"line\";" + eol);
		out.write("\t clusterrank= local;" + eol);
		out.write("\t subgraph cluster_0 {" + eol + 
				  "\t\tstyle = rounded; minlen = 0.01;" + eol +
				  "\t\trankdir = TB; ranksep = 0.01;" + eol +
				  "\t\tlabel = \"Confidence\";" + eol +
				  "\t\tcolor = \"black\";" +eol +
				  "\t\tfontname=\"helvetica-bold\";" +eol +
		          "\t\t theColors [label=\"                      \",shape=\"rectangle\",fillcolor=\"0.5 1.0 1.0: 0.55 1.0 1.0: 0.6 1.0 1.0: 0.65 1.0 1.0: 0.7 1.0 1.0: 0.75 1.0 1.0: 0.8 1.0 1.0: 0.85 1.0 1.0: 0.9 1.0 1.0: 0.95 1.0 1.0: 1.0 1.0 1.0\", fontname=\"helvetica-bold\", style=striped];" + eol +
		          "\t\t theLegend [label=\"low           high\",shape=\"plaintext\",fontname=\"helvetica-bold\"];" + eol +
		          "\t\t theColors->theLegend [style=invis,len=0.01];" +eol +
		          "\t}" + eol +
		          "\t subgraph cluster_1 {"  + eol);
  	} 
  
  	private static void writeEpilogue(FileWriter out) throws IOException {
  		String eol = System.lineSeparator();
  		out.write("color = white;}" + eol + "}" + System.lineSeparator());
  	}
  	
  	private static void writeVertices(List<Vertex> vertices, FileWriter out) throws IOException {
  		for(Vertex v: vertices) {
  			
  			
  			String whiteText  = "fontcolor = white,";
  			boolean darkColor = (((v.confidence/2)+0.5f) >= 0.57f) && (((v.confidence/2)+0.5f) <= 0.8f);
  			if (!darkColor) 
  				whiteText = "";
  			
  			if (v.confidence >= 0) {
  				out.write("\tn" + v.id + "\t[label=\"" + v.name + "\", shape=\"rectangle\"," + whiteText  
  							+ "fillcolor=\"" + colorString(v.confidence) +  "\", fontname=\"helvetica-bold\", style=filled];"); 
  				out.write(System.lineSeparator()); 
  			} else {
  				out.write("\tn" + v.id + "\t[label=\"" + v.name + "\", shape=\"rectangle\","
  						  + " fontname=\"helvetica-bold\", style=dashed];"); 
  			}
  		}
  	} 
  
  	private static void writeEdges(List<Vertex> vertices, List<Edge> edges, FileWriter out) 
  	throws IOException {
  		for(Edge e: edges) {
  			String arrowType = "odot";
  			if (e.type.equals("M")) arrowType = "dot";  // Mandatory feature - filled dot
  			if (e.type.equals("O")) arrowType = "odot"; // Optional feature - empty dot
  			if (e.type.equals("R")) arrowType = "inv"; // OR feature - ?
  			if (e.type.equals("A")) arrowType = "dot"; // AND feature - filled dot
  			if (e.type.equals("E")) arrowType = "none"; // Excludes feature - empty dot
  			if (e.type.equals("X")) arrowType = "invempty"; // Alternative/XOR feature - empty dot
  			if (e.type.equals("I")) arrowType = "none"; // Requires/implies feature - empty dot

  			out.write("\tn" + e.source + ":s-> n" + e.target + ":n [color=\"" + colorString(e.confidence) + "\",dir=forward,arrowhead=" + arrowType + "];");
  			out.write(System.lineSeparator());
  		}
  	}  	
  	  
  
}
