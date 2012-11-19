package grailog.Viz;



import java.awt.SplashScreen;
import java.io.*;
import java.net.URL;


/**
 * method main calls a new instance of class ActionHnadle.
 * @author Akbari 
 */
public class mainclass {
	

public static void main(String arg[]) throws IOException{
	/*SplashScreen splash = SplashScreen.getSplashScreen();
	URL imageurl = URL.class.getResource("grailog\\Viz\\splash.png");
	//splash.setImageURL(new File("splash.png").toURI().toURL());
	splash.setImageURL(imageurl);
	
	//splash.update();
	 * 
	 */
	new ActionHandle();
	
}

}

