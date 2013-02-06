package com.honeybadgers.flltutorial;

import com.honeybadgers.flltutorial.model.backend.TutorialManager;
import com.honeybadgers.flltutorial.ui.BeginTopComponent;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.out.println("Is this the EDT thread: "+SwingUtilities.isEventDispatchThread());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                  BeginTopComponent topComp = new BeginTopComponent(); 
                  topComp.setVisible(true);
                  System.out.println("Is this the EDT thread: "+SwingUtilities.isEventDispatchThread());
            }
        });
        ArrayList<TutorialManager> allTutorialsAtFolder = TutorialManager.getAllTutorialsAtFolder();
    }
}
