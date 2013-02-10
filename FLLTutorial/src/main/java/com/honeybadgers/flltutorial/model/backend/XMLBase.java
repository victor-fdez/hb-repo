/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model.backend;

import com.honeybadgers.flltutorial.model.Tutorial;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Dan
 */
public class XMLBase {
    
    public Tutorial loadTutorial(File f){
        //Tutorial tut = new Tutorial();
        
        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //throws ParserConfigurationException
            Document doc=db.parse(new File("sample.xml")); //throws SAXException, IOException
            NodeList stages=doc.getElementsByTagName("stage");
            
            
        }catch(ParserConfigurationException pce){
            //handle exception here
        }catch(SAXException saxe){
            //handle exception here
        }catch(IOException ioe){
            //handle exception here
        }
        return null;
    }
    public static void main(String[] args){
        System.out.println("helloworld");
        
    }
}
