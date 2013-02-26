/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model.backend;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.Tutorial;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/*import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;*/
/**
 *
 * @author Dan
 */
public class XMLBase {
    
    public Tutorial loadTutorial(File f){
        //Tutorial tut = new Tutorial();
        List<Option> subTasks;
        List<Option> taskDiagram;
        List<Option> morphologicalChart;
        List<Option> limitationsAndConstraints;
        
        /*try{
            SAXReader reader = new SAXReader();
            Document document = reader.read(f); //throws DocumentException
            Element root = document.getRootElement();
            
            
        }catch(DocumentException e){
            //handle exception here
        }*/
        try{    
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //throws ParserConfigurationException
            Document doc=db.parse(f); //throws SAXException, IOException
            NodeList stages=doc.getElementsByTagName("stage");
            subTasks = loadProblemStatement(doc);
        }catch(ParserConfigurationException pce){
            //handle exception here
        }catch(SAXException saxe){
            //handle exception here
        }catch(IOException ioe){
            //handle exception here
        }
        return null;
    }
    
    
    public static void saveTutorial(Tutorial tut){
           
    }
    public static void main(String[] args){
        System.out.println("helloworld");
        
    }

    private List<Option> loadProblemStatement(Document doc) {
        Node problemStatementNode = doc.getElementById("problem_statement");
        NodeList children = problemStatementNode.getChildNodes();
        for(int i=0; i<children.getLength(); i++){
            
        }  
        return null;
    }
}
