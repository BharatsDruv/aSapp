
import java.awt.Desktop;
import java.net.URL;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sony
 */
public class Class_Website {
    
    public static void GoToAsappOfficial()
    {
        try
            {
                Desktop.getDesktop().browse(new URL("http://www.druvperman.wixsite.com/asapp").toURI());

            }
        catch(Exception e)
        {  }   
    }
}
