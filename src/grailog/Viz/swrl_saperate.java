package grailog.Viz;
import java.lang.String;


/**
 * This class splits the input SWRL rule
 * @author Zhang
 *
 */
public class swrl_saperate{
	private String total;
	public  String[] body_head= new String[2];
	public  String[] body=new String[50];
	public  String[] head=new String[50];
	public  String[][] class_node_label_edge_body=new String[100][4];
	public  String[][] class_node_label_edge_head=new String[100][4];
	public int p,q,r;//to calculate the number of  
	
	
	public swrl_saperate(String rule)//initialize&insert the total rule
	{
		this.total=rule;
	}
	
//==============================
	
	
	/**
	 * Separates body and head(conclusion) part of swrl rule
	 * @return : returns body and head(conclusion) parts of swrl rule in an array
	 */
	public String[] saperate_body_head()//body and head(conclusion) part of swrl rules separate
	{
		if(total.indexOf("->")!=-1)
		    this.body_head=this.total.split("->");
		return this.body_head;
	}
	
	/**
	 * splits the body part based on symbol "^"
	 * @return : returns the body axioms of a rule in an array
	 */
	public String[] split_body()//split the body part by symbol "^" 
	{
		if(body_head[0].indexOf("^")!=-1)
		   this.body=body_head[0].split("\\^");
		else if(body_head[0].indexOf("^")==-1)
		{
			this.body[0]=body_head[0];
		}

		return this.body;
	}
	/**
	 * same as the split_body method
	 * @return : returns the head axioms of a rule in an array
	 */
	public String[] split_head()//the same as the split_body method
	{
		if(body_head[1].indexOf("^")!=-1)
		   this.head=body_head[1].split("\\^");
		else if(body_head[1].indexOf("^")==-1)
		{
			this.head[0]=body_head[1];
			this.r=-1;
		}
		return this.head;
	}
	
	
	/**
	 * Returns the body of a SWRL rule as a string.
	 * @return : Returns the body of a SWRL rule as a string.
	 */
	public String get_body()
	{
		String body=body_head[0];
		return body.trim();
	}
		
    /**
     * Returns the conclusion of a SWRL rule as a string.
     * @return :Returns the conclusion of a SWRL rule as a string.
     */
    public String get_conclusion()
    {
    	String conclusion=body_head[1];
    	return conclusion.trim();
    }

    /**
     * Determines the relations and their arguments in the body 
     */
    public void saperate_body()//for each string,if test no "," ,its the unary predicate,else its the binary predicate
    {
       String [] n_c=new String[2];
       String [] n_l=new String[2];
       String [] l_e=new String[2];
       String []temp=new String[2];
    	for(int i=0,j=0;i<this.body.length;i++)
    	{
    		if((this.body[i].indexOf(",")==-1))
    		{
    			n_c=this.body[i].split("\\?");
    			temp=n_c[0].split("\\(");
    			this.class_node_label_edge_body[j][0]=temp[0].trim();//class assert into the first line
    			temp=n_c[1].split("\\)");
    			this.class_node_label_edge_body[j][1]=temp[0].trim();//node assert into the second line
    			this.class_node_label_edge_body[j][3]="class";
    			j++;
    			
    		}
    		else
    		{
    			n_l=this.body[i].split("\\(");
    			l_e=n_l[1].split(",",2);
    			if(l_e[1].indexOf(",")==-1)
    			{
    			    temp[0]=l_e[0].substring(1,l_e[0].length());
    			    this.class_node_label_edge_body[j][0]=n_l[0].trim();//label assert into the first line
    			    this.class_node_label_edge_body[j][1]=temp[0].trim();//node1 into the second line
    			    temp=l_e[1].split("\\)");
    			    if(temp[0].indexOf("?")==-1)
    			         this.class_node_label_edge_body[j][2]=temp[0].trim();//node2 into the third line
    			    else{
    			    	 temp=temp[0].split("\\?");
    			    	 this.class_node_label_edge_body[j][2]=temp[1].trim();
    			    }
    			    if((this.class_node_label_edge_body[j][0].length()>5)&&(this.class_node_label_edge_body[j][0].substring(0, 5).equals("swrlb")))
    			    	this.class_node_label_edge_body[j][3]="SWRL_Building";
    			    else
    			    {
    			    	this.class_node_label_edge_body[j][3]="property";
    			    }
    			    j++;
    			}
    		}
    		this.p=j;
    	}
        
    }
       /**
     * Determines the relations and their arguments in the head
     */
    public void saperate_head()//the same as saperate_body method
       {
          String [] n_c=new String[2];
          String [] n_l=new String[2];
          String [] l_e=new String[2];
          String []temp=new String[2];  
        if(this.r!=-1)
        { 	
       	for(int i=0,j=0;i<this.head.length;i++)
       	{
       		if(this.head[i].indexOf(",")==-1)
       		{
       			n_c=this.head[i].split("\\?");
       			temp=n_c[0].split("\\(");
       			this.class_node_label_edge_head[j][0]=temp[0].trim();//class assert into the first row
       			temp=n_c[1].split("\\)");
       			this.class_node_label_edge_head[j][1]=temp[0].trim();//node assert into the second row
       			this.class_node_label_edge_head[j][3]="class";
       			j++;
       		}
       		else 
       		{
       			n_l=this.head[i].split("\\(");
       			l_e=n_l[1].split(",",2);
       			if(l_e[1].indexOf(",")==-1)
       			{
       			    temp[0]=l_e[0].substring(1,l_e[0].length());
       			    this.class_node_label_edge_head[j][0]=n_l[0].trim();//label assert into the first row
       			    this.class_node_label_edge_head[j][1]=temp[0].trim();//node1 into the second row
       			    temp=l_e[1].split("\\)");
       			    if(temp[0].indexOf("?")==-1)
       			         this.class_node_label_edge_head[j][2]=temp[0].trim();//node2 into the third row
       			    else{
       			    	 temp=temp[0].split("\\?");
       			    	 this.class_node_label_edge_head[j][2]=temp[1].trim();
       			    }
       			    this.class_node_label_edge_head[j][3]="property";
       			    j++;
       			}
       		}
       		this.q=j;
       	}
        }
        else
        {
        	if(this.head[0].indexOf(",")==-1)
       		{
       			n_c=this.head[0].split("\\?");
       			temp=n_c[0].split("\\(");
       			this.class_node_label_edge_head[0][0]=temp[0].trim();//class assert into the first row
       			temp=n_c[1].split("\\)");
       			this.class_node_label_edge_head[0][1]=temp[0].trim();//node assert into the second row
       			this.class_node_label_edge_head[0][3]="class";
       		}
       		else 
       		{
       			n_l=this.head[0].split("\\(");
       			l_e=n_l[1].split(",",2);
       			if(l_e[1].indexOf(",")==-1)
       			{
       			    temp[0]=l_e[0].substring(1,l_e[0].length());
       			    this.class_node_label_edge_head[0][0]=n_l[0].trim();//label assert into the first row
       			    this.class_node_label_edge_head[0][1]=temp[0].trim();//node1 into the second row
       			    temp=l_e[1].split("\\)");
       			    if(temp[0].indexOf("?")==-1)
       			         this.class_node_label_edge_head[0][2]=temp[0].trim();//node2 into the third row
       			    else{
       			    	 temp=temp[0].split("\\?");
       			    	 this.class_node_label_edge_head[0][2]=temp[1].trim();
       			        }
       			}
       			this.class_node_label_edge_head[0][3]="property";
             }
        	this.q=1;
         }
       }
}
	
