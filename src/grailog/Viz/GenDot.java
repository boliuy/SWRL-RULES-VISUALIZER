package grailog.Viz;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
private boolean isNumeric(String s){
	
	for(int i=0; i<s.length(); i++)
		if(!Character.isDigit(s.charAt(i)) && s.charAt(i)!='.')
			return false;
	return true;
}
//==================================================================
private String constants(String cons){
	String tmp = "\"\\";
	tmp += cons.substring(0, cons.length()-1);
	tmp+="\\\"\"";
	return tmp;
}
//==================================================================
public String getLogicalSyntax(){
	String logic = "";
	int i,j;
	String obj = "";
	for(i=0; (i < bodies.length) && !bodies[i][0].equals("null"); i++)
		if(bodies[i][3].equals("class")){
			obj = bodies[i][1];
			logic += bodies[i][1]+"#"+bodies[i][0]+"(\n" ;
			for(j=i+1; (j < bodies.length) && !bodies[j][0].equals("null"); j++)
				if(bodies[j][1].equals(obj) && !isObject(bodies[j][2]))
					logic += bodies[j][0]+"->"+bodies[j][2]+";\n";
			for(j=0; (j < heads.length) && !heads[j][0].equals("null"); j++)
				if(heads[j][1].equals(obj) && !isObject(heads[j][2]))
					logic += heads[j][0]+"->"+heads[j][2]+";\n";
			
			logic=logic.substring(0, logic.length()-2);
			logic+=")\n";
		}	
	
	for(i=0; (i < heads.length) && !heads[i][0].equals("null"); i++)
		if(heads[i][3].equals("class")){
			obj = heads[i][1];
			logic += heads[i][1]+"#"+heads[i][0]+"(\n" ;
			for(j=i+1; (j < bodies.length) && !bodies[j][0].equals("null"); j++)
				if(heads[j][1].equals(obj) && !isObject(heads[j][2]))
					logic += heads[j][0]+"->"+heads[j][2]+";\n";			
		
			logic=logic.substring(0, logic.length()-2);
			logic+=")\n";
		}		
	
	return logic;
}
//==================================================================
/**
 * This method generates the input .dot file required by graphviz and save it to the disk. 
 * Then asks graphiz to use it as input and make the output file.  
 * @param outformat : is the output format that user likes to be generated.
 * @throws IOException 
 */
public void genDotFile() throws IOException{
		int i,j;
		for(i=0; i<100; i++)
			for(j=0; j<4;j++){
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
			fw.write("\n\t node [shape=octagon];");
			//fw.write("\n\t node [shape=hexagon, style=filled];");
			//fw.write("\n\t edge [dir=both,arrowtail=dot];");
			fw.write("\n\t edge [color=RED];\n");			
			
			//================ADDING VARIABLES TO THE .DOT FILE=============
			
			for(i=0; i<100; i++)
				for(j=1; j<3;j++)
					if (!bodies[i][j].equals("null") && !isNumeric(bodies[i][j]) && !bodies[i][j].startsWith("\""))
						fw.write(bodies[i][j]+"[label=\"?"+ bodies[i][j] +"\"];");				
				for(i=0; i<100; i++)
					for(j=1; j<3;j++)
						if (!heads[i][j].equals("null") && !isNumeric(heads[i][j]))
							fw.write(heads[i][j]+"[label=\"?"+ heads[i][j] +"\"];");
			fw.write("\n");	
				
			//================DRAWING THE BODY PART==================================
			for(i=0; (i < bodies.length) && !bodies[i][0].equals("null"); i++){
				if (bodies[i][2].equals("null")){
					fw.write("\n\t " + bodies[i][0].replaceAll(":","") + " [shape=oval,label=\""+bodies[i][0]+"\"];");
					fw.write("\n\t " + bodies[i][0].replaceAll(":","") + " -> " + bodies[i][1] + ";");
				}
				else{
					if(isObject(bodies[i][1]) && !isObject(bodies[i][2])){
						if(bodies[i][1].startsWith("\"") && !bodies[i][2].endsWith("\"")){
							fw.write("\n\t " +  constants(bodies[i][1]) + " -> " + bodies[i][2]+ " [dir=both,arrowtail=dot,label=\"" + bodies[i][0]+"\"];");
							fw.write(constants(bodies[i][1]) + " [shape=box];");
						}	
						else
							if(!bodies[i][1].startsWith("\"") && bodies[i][2].endsWith("\"")){
								fw.write("\n\t " + bodies[i][1] + " -> " + constants(bodies[i][2]) + " [dir=both,arrowtail=dot,label=\"" + bodies[i][0]+"\"];");
								fw.write(constants(bodies[i][2]) +" [shape=box];");
							}	
							else
								if(bodies[i][1].startsWith("\"") && bodies[i][2].endsWith("\"")){
									fw.write("\n\t " + constants(bodies[i][1]) + "-> " + constants(bodies[i][2]) + " [dir=both,arrowtail=dot,label=\"" + bodies[i][0]+"\"];");
									fw.write(constants(bodies[i][1]) + " [shape=box];");
									fw.write(constants(bodies[i][2]) + " [shape=box];");
								}	
								else
									fw.write("\n\t " + bodies[i][1] + " -> " + bodies[i][2] + " [dir=both,arrowtail=dot,label=\"" + bodies[i][0]+"\"];");
					}	
					else{
						if(bodies[i][1].startsWith("\"") && !bodies[i][2].endsWith("\"")){
							fw.write("\n\t " + constants(bodies[i][1]) + " -> " + bodies[i][2] + " [label=\"" + bodies[i][0]+"\"];");
							fw.write(constants(bodies[i][1]) + " [shape=box];");
						}	
						else
							if(!bodies[i][1].startsWith("\"") && bodies[i][2].endsWith("\"")){
								fw.write("\n\t " + bodies[i][1] +" -> "+constants(bodies[i][2]) + " [label=\"" + bodies[i][0]+"\"];");
								fw.write(constants(bodies[i][2]) + " [shape=box];");
							}	
							else
								if(bodies[i][1].startsWith("\"") && bodies[i][2].endsWith("\"")){
									fw.write("\n\t " + constants(bodies[i][1]) + " -> " + constants(bodies[i][2]) + " [label=\"" + bodies[i][0]+"\"];");
									fw.write(constants(bodies[i][1]) + " [shape=box];");
									fw.write(constants(bodies[i][2]) + " [shape=box];");
								}	
								else
									fw.write("\n\t " + bodies[i][1] + " -> " + bodies[i][2] + " [label=\"" + bodies[i][0]+"\"];");
					}	
				}
			}	
			
			//================DRAWING THE HEAD PART==================================	
			for(i=0; (i < heads.length) && !heads[i][0].equals("null"); i++){
				if (heads[i][2].equals("null")){
					fw.write("\n\t " + heads[i][0] + " [shape=oval, color=green];");
					fw.write("\n\t " + heads[i][0] + " -> " + heads[i][1] + " [color=green];");
				}
				else{
					if(isObject(heads[i][1]) && !isObject(heads[i][2])){
						if(heads[i][1].startsWith("\"") && !heads[i][2].endsWith("\"")){
							fw.write("\n\t " + constants(heads[i][1]) +  " -> " + heads[i][2] + " [color=green,dir=both,arrowtail=dot,label=\"" + heads[i][0]+"\"];");
							fw.write(constants(heads[i][1]) + " [shape=box];");
						}	
						else
							if(!heads[i][1].startsWith("\"") && heads[i][2].endsWith("\"")){
								fw.write("\n\t " + heads[i][1]  +  " -> " +constants(heads[i][2]) + "[color=green,dir=both,arrowtail=dot,label=\"" + heads[i][0]+"\"];");
								fw.write(constants(heads[i][2]) + " [shape=box];");
							}	
							else
								if(heads[i][1].startsWith("\"") && !heads[i][2].endsWith("\"")){
									fw.write("\n\t " +constants(heads[i][1]) + " -> " + constants(heads[i][2]) + "[color=green,dir=both,arrowtail=dot,label=\"" + heads[i][0]+"\"];");
									fw.write(constants(heads[i][1]) + " [shape=box];");
									fw.write(constants(heads[i][2]) + " [shape=box];");
								}	
								else
									fw.write("\n\t " + heads[i][1] + " -> " + heads[i][2] + " [color=green,dir=both,arrowtail=dot,label=\"" + heads[i][0]+"\"];");
					}	
					else{
						if(heads[i][1].startsWith("\"") && !heads[i][2].endsWith("\"")){
							fw.write("\n\t " + constants(heads[i][1]) + " -> " + heads[i][2] + " [color=green,label=\"" + heads[i][0]+"\"];");
							fw.write(constants(heads[i][1]) + " [shape=box];");
						}	
						else
							if(!heads[i][1].startsWith("\"") && heads[i][2].endsWith("\"")){
								fw.write("\n\t " + heads[i][1] +" -> " + constants(heads[i][2]) +  "[color=green,label=\"" + heads[i][0]+"\"];");
								fw.write(constants(heads[i][2]) + " [shape=box];");
							}	
							else
								if(heads[i][1].startsWith("\"") && heads[i][2].endsWith("\"")){
									fw.write("\n\t " + constants(heads[i][1]) + " -> " + constants(heads[i][2]) + "[color=green,label=\"" + heads[i][0]+"\"];");
									fw.write(constants(heads[i][1])+ " [shape=box];");
									fw.write(constants(heads[i][2]) + " [shape=box];");
								}	
								else
									fw.write("\n\t " + heads[i][1] + " -> " + heads[i][2] + " [color=green,label=\"" + heads[i][0]+"\"];");
									
								
					}	
				}	
			
			}
			//================MAKING NUMBERS AS CONSTANTS========================
			for(i=0; i<100; i++)
				if (isNumeric(bodies[i][2]))
						fw.write(bodies[i][2] + " [shape=box];");
			for(i=0; i<100; i++)
				if (isNumeric(heads[i][2]))
						fw.write(heads[i][2] + " [shape=box];");						
				
			//===================================================================
			
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
			
}	
//=============================================================
	public void genGraph(String outformat){	
	
		String [] cmd = new String [4];
		//cmd[0] = "C:\\Program Files\\Graphviz 2.28\\bin\\dot.exe";
		cmd[0] = "dot";
		cmd[1] = "-T"+ outformat;
		cmd[2] = "output.dot";
		cmd[3] = "-O";
		
		try{
			Runtime rt = Runtime.getRuntime();			
			Process pr = rt.exec(cmd);
			// any error???
	        //int exitVal = pr.waitFor();	        
			pr.waitFor();
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
