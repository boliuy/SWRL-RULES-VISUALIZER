package grailog.Viz;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
//import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.FileChooserUI;

import java.io.*;


/**
 * This class generates the GUI.
 * @author Zhang
 *
 */
public class ActionHandle{
	//=====================================================
	String InputPath = "InputRules.txt"; 
	String rulefile ="swrl.txt";
	//=====================================================
	private JFrame frame=new JFrame("GRAILOG VISUALIZER");
	private JButton showparts=new JButton("Show Splits");
	private JButton showgraph =  new JButton("Generate Graph"); 
	private JLabel jloutput = new JLabel("Output Format :");
	String [] outformats = {"png","ps","svg","svgz","fig","mif","hpgl","pcl","gif","dia","imap","cmapx"};
	private JComboBox jcboutlist = new JComboBox(outformats);
	private JLabel ruleLab=new JLabel("SWRL Rules");	
	private JFileChooser jfc = new JFileChooser();
	private JButton showdialog=new JButton("choose input file");
	private JTextArea ruleText=new JTextArea(200,100);
	private JTextArea showText=new JTextArea(200,100);
	private JTextArea infoText=new JTextArea("For standard:"+"\r\n"+"using '^' to separate each unary/binary unit,"+"\r\n"+"using '->' to point to the conclusion."+"\r\n\r\n"+"Here are the split part");
	public JScrollPane scr_in=new JScrollPane(ruleText,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
	public JScrollPane scr_out=new JScrollPane(showText,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);	
	public static String[] body_head= new String[2];
	public static String[] body_seperate=new String[50];	
	public ActionHandle(){
		Font fnt=new Font("Seruef",Font.BOLD,12);
		infoText.setFont(fnt);
		frame.setLayout(null);
		ruleLab.setBounds(10,105,90,20);
		infoText.setBounds(5,320,250,100);
		infoText.setEditable(false);
		infoText.setBackground(null);
		ruleText.setLineWrap(true);	
		showText.setLineWrap(false);
		scr_in.setBounds(100, 105, 470,170);
		//====================================
		showparts.setBounds(370,300,130,30);
		showdialog.setBounds(230,20,130,30);
		showgraph.setBounds(370,620,150,30);
		showgraph.setEnabled(false);
		JLabel inmethod1 =  new JLabel("Read From file");
		inmethod1.setBounds(10, 10, 100, 20);
		JLabel inmethod2 =  new JLabel("Manual Input");
		inmethod2.setBounds(10, 50, 100, 20);
		final JRadioButton rb1 = new JRadioButton();
		rb1.setBounds(120, 15, 20, 20);
		final JRadioButton rb2 = new JRadioButton();
		rb2.setBounds(120, 50, 20, 20);
		ButtonGroup bg = new ButtonGroup(); 
		bg.add(rb1);
		bg.add(rb2);
		jloutput.setBounds(100,620,130,30);
		jcboutlist.setSelectedIndex(0);
		jcboutlist.setBounds(200,620,130,30);
		scr_out.setBounds(100,420, 470, 170);
		//scr_in.setEnabled(false);
		showdialog.setEnabled(false);
		ruleText.setEnabled(false);
		frame.add(ruleLab);
		frame.add(infoText);
		frame.add(showparts);
		frame.add(showdialog);
		//frame.add(jfc);
		frame.add(showgraph);
		frame.add(inmethod1);
		frame.add(inmethod2);
		frame.add(rb1);
		frame.add(rb2);
		
		frame.add(jloutput);
		frame.add(jcboutlist);
		frame.add(scr_in);
		frame.add(scr_out);
		frame.setSize(600,700);
		frame.setLocation(500,20);
		frame.setVisible(true);
		showparts.addActionListener(new ActionListener(){//use actionlistener for the button insert
			public void actionPerformed(ActionEvent arg0){
				if(arg0.getSource()==showparts){
					//=============================================
					String swrl_rule="";
					if(rb2.isSelected())
						swrl_rule=ruleText.getText();
					else if(rb1.isSelected()){
						//------------------------------------------
						try{							
							File inf = new File(rulefile);	
							//if(!inf.exists())
								//  inf.createNewFile();
							FileReader fr = new FileReader(inf);			
							BufferedReader br = new BufferedReader(fr);						
							swrl_rule=br.readLine();
							ruleText.setText(swrl_rule);
							
							br.close();
							fr.close();							
						}
						catch( IOException e){
							e.printStackTrace();
						}
						
					}
					swrl_saperate s=new swrl_saperate(swrl_rule);
					s.saperate_body_head();
					s.split_body();
					s.split_head();
		        	s.saperate_body();
		        	s.saperate_head();
		        	
        			///System.out.println("The result of seperate rules:the body part");
					//for(int i=0;i<s.p;i++)
						//System.out.println(s.class_node_label_edge_body[i][0]+'\t'+s.class_node_label_edge_body[i][1]+'\t'+s.class_node_label_edge_body[i][2]+'\t'+s.class_node_label_edge_body[i][3]);
					//System.out.println("The result of seperate rules:the head part");
					//for(int j=0;j<s.q;j++)
						//System.out.println(s.class_node_label_edge_head[j][0]+'\t'+s.class_node_label_edge_head[j][1]+'\t'+s.class_node_label_edge_head[j][2]+'\t'+s.class_node_label_edge_head[j][3]);
					try {
				        BufferedWriter out = new BufferedWriter(new FileWriter(InputPath));//to this path
				        out.write("body parts:\r\n");
				        for(int i=0;i<s.p;i++)
				            out.write(s.class_node_label_edge_body[i][0]+'\t'+s.class_node_label_edge_body[i][1]+'\t'+s.class_node_label_edge_body[i][2]+'\t'+s.class_node_label_edge_body[i][3]+"\r\n");
				        out.write("head parts:\r\n");
				        	for(int j=0;j<s.q;j++)
				        	out.write(s.class_node_label_edge_head[j][0]+'\t'+s.class_node_label_edge_head[j][1]+'\t'+s.class_node_label_edge_head[j][2]+'\t'+s.class_node_label_edge_head[j][3]+"\r\n");	
				            out.close();
				    } catch (IOException e) {
				    }
			        try {
				        BufferedReader br = new BufferedReader(new FileReader(InputPath));
				        String total="";
				        String line=null;
				        line=br.readLine();
				        while(line!=null)
				        {
				        	total+=line+"\r\n";
				        	
				        	line=br.readLine();
				        }
				        showText.setText(total);
				        br.close();
			            } catch (FileNotFoundException e) {
				        // TODO Auto-generated catch block
				        e.printStackTrace();
			            } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			      
		            
				}
				showgraph.setEnabled(true);
				
			}			
		});
		showgraph.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
	            	//==========================================
					
		            GenDot gdot = new GenDot();
					gdot.genDotFile(jcboutlist.getSelectedItem().toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		showdialog.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jfc.showOpenDialog(frame);
				//jfc.setVisible(true);
				
				if(jfc.getSelectedFile()!=null)
					rulefile = jfc.getSelectedFile().getAbsolutePath();
				//System.out.print(rulefile);
					
				
			}
		});
		rb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				showdialog.setEnabled(true);
				ruleText.setEnabled(false);
			}
		});
		rb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				showdialog.setEnabled(false);
				ruleText.setEnabled(true);
			}
		});
	}
	
}
