package grailog.Viz;


import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;



/**
 * This class generates the .dot file needed by graphviz. It reads the input
 * data from the InputRules.txt text file. 
 * The generated .dot file will be saved on disk in the same directory as InputRules.txt
 * with name output.["output format extension"].dot  
 * @author Akbari
 */

public class GenDot {
	
	String [][] bodies = new String [100][4];
	String [][] heads = new String [100][4];
	String inputpath = "InputRules.txt";
	String outputpath = "output.dot";	
	
//==================================================================
	
/**
 * This method returns an array of the objects in the rule.
 * @param bodies : this bodies array contains the body axioms of the input rule
 * @param heads : this bodies array contains the conclusion axioms of the input rule
 * @return : returns an array of the objects in the rule.
 */
private String [] getObjects(String [][] bodies, String [][] heads){
	String [] objects = new String [bodies.length+heads.length];
	int i,objindex=0;
	for(i=0; (i < bodies.length) && !bodies[i][0].equals("null"); i++)
		if(bodies[i][3].equals("class"))
			objects[objindex++] = bodies[i][1];
	for(i=0; (i < heads.length) && !heads[i][0].equals("null"); i++)
		if(heads[i][3].equals("class"))
			objects[objindex++] = heads[i][1];
	return objects;
}
//==================================================================


/**
 * This method determines if a specified node is an object or not.
 * @param node : a specified node in the graph
 * @return : if the node is an object returns true, otherwise returns false. 
 */
private boolean isObject(String node){
	String [] objects =  getObjects(bodies, heads);
	int i;
	for (i=0; i<objects.length; i++)
		if(node.equals(objects[i]))
			return true;	
	return false;
}

//==================================================================

/**
 * This method generates the input .dot file required by graphviz and save it to the disk. 
 * Then asks graphiz to use it as input and make the output file.  
 * @param outformat : is the output format that user likes to be generated.
 * @throws IOException 
 */
public void genDotFile(String outformat) throws IOException{
		
		for(int i=0; i<100; i++)
			for(int j=0; j<4;j++){
				bodies[i][j] = "null";
				heads[i][j] = "null";
			}	
		
		
		//------------------------------------------
		try{
			
			File inf = new File(inputpath);	
			//if(!inf.exists())
				//  inf.createNewFile();
			FileReader fr = new FileReader(inf);			
			BufferedReader br = new BufferedReader(fr);
		
			String st;
			st=br.readLine();
			int bindex =0,hindex =0;
			while((st=br.readLine())!=null){
				if(st.contains("head parts:"))
					break;
				bodies[bindex++]=st.split("\\s+");
				
			}
			while((st=br.readLine())!=null){
				heads[hindex++]=st.split("\\s+");
				
			}
			
			//--------------------------------------
			
			File of = new File(outputpath);
			FileWriter fw = new FileWriter(of);
			fw.write("digraph G {");
			fw.write("\n\t node [shape=box];");
			//fw.write("\n\t edge [dir=both,arrowtail=dot];");
			fw.write("\n\t edge [color=RED];");
			int i;
			
			//================DRAWING THE BODY PART==================================
			for(i=0; (i < bodies.length) && !bodies[i][0].equals("null"); i++){
				if (bodies[i][2].equals("null")){
					fw.write("\n\t " + bodies[i][0] + " [shape=oval];");
					fw.write("\n\t " + bodies[i][0] + " -> " + bodies[i][1] + ";");
				}
				else{
					if(isObject(bodies[i][1]) && !isObject(bodies[i][2]))
						fw.write("\n\t " + bodies[i][1] + " -> " + bodies[i][2] + " [dir=both,arrowtail=dot,label=\"" + bodies[i][0]+"\"];");
					else
						fw.write("\n\t " + bodies[i][1] + " -> " + bodies[i][2] + " [label=\"" + bodies[i][0]+"\"];");
				}
			}	
			
			//================DRAWING THE HEAD PART==================================	
			for(i=0; (i < heads.length) && !heads[i][0].equals("null"); i++){
				if (heads[i][2].equals("null")){
					fw.write("\n\t " + heads[i][0] + " [shape=oval, color=green];");
					fw.write("\n\t " + heads[i][0] + " -> " + heads[i][1] + " [color=green];");
				}
				else{
					if(isObject(heads[i][1]) && !isObject(heads[i][2]))
						fw.write("\n\t " + heads[i][1] + " -> " + heads[i][2] + " [color=green,dir=both,arrowtail=dot,label=\"" + heads[i][0]+"\"];");
					else
						fw.write("\n\t " + heads[i][1] + " -> " + heads[i][2] + " [color=green,label=\"" + heads[i][0]+"\"];");
				}	
			
			}
			
			
			fw.write("\n}");
			//===================END OF MAKING THE .DOT TEMPLATE FILE=========================
			
			fw.flush();
			fw.close();	
			fr.close();
			br.close();
		}
		catch( FileNotFoundException e){
			e.printStackTrace();
		}
			
		
		//--------------------------------------------
		
		
		String [] cmd = new String [4];
		//cmd[0] = "C:\\Program Files\\Graphviz 2.28\\bin\\dot.exe";
		cmd[0] = "dot";
		cmd[1] = "-T"+ outformat;
		cmd[2] = "output.dot";
		cmd[3] = "-O";
		
		try{
			Runtime rt = Runtime.getRuntime();
			//System.out.println("Executing dot.exe ....");
			Process pr = rt.exec(cmd);
			// any error???
	        int exitVal = pr.waitFor();
	        //System.out.println("ExitValue: " + exitVal);
			
	        //=========OPENING THE OUTPUT .PNG FILE ==============================
	        File image = new File("output.dot."+ outformat);
			Desktop desktop = Desktop.getDesktop();
			desktop.open(image);
		}
		catch(Throwable te){
			te.printStackTrace();
		}
		
	}
	
}
