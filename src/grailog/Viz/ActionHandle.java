package grailog.Viz;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
//import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import java.io.*;


/**
 * This class generates the GUI.
 * @author Zhang
 *
 */
public class ActionHandle{
	//=====================================================
	String InputPath = "InputRules.txt"; 
	//=====================================================
	private JFrame frame=new JFrame("GRAILOG VISUALIZER");
	private JButton showparts=new JButton("Show Splits");
	private JButton showgraph =  new JButton("Generate Graph"); 
	private JLabel jloutput = new JLabel("Output Format :");
	String [] outformats = {"png","ps","svg","svgz","fig","mif","hpgl","pcl","gif","dia","imap","cmapx"};
	private JComboBox jcboutlist = new JComboBox(outformats);
	private JLabel ruleLab=new JLabel("SWRL Rules");
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
		ruleLab.setBounds(10,5,90,20);
		infoText.setBounds(5,220,250,100);
		infoText.setEditable(false);
		infoText.setBackground(null);
		ruleText.setLineWrap(true);	
		showText.setLineWrap(false);
		scr_in.setBounds(100, 5, 470,170);
		showparts.setBounds(370,200,130,30);
		showgraph.setBounds(370,520,150,30);
		showgraph.setEnabled(false);
		jloutput.setBounds(100,520,130,30);
		jcboutlist.setSelectedIndex(0);
		jcboutlist.setBounds(200,520,130,30);
		scr_out.setBounds(100,320, 470, 170);
		frame.add(ruleLab);
		frame.add(infoText);
		frame.add(showparts);
		frame.add(showgraph);
		frame.add(jloutput);
		frame.add(jcboutlist);
		frame.add(scr_in);
		frame.add(scr_out);
		frame.setSize(600,600);
		frame.setLocation(500,20);
		frame.setVisible(true);
		showparts.addActionListener(new ActionListener(){//use actionlistener for the button insert
			public void actionPerformed(ActionEvent arg0){
				if(arg0.getSource()==showparts){
					String swrl_rule=ruleText.getText();
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
	}
	
}
