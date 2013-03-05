/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model.backend;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.Stage;
import com.honeybadgers.flltutorial.model.Tutorial;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;*/
/**
 *
 * @author Dan Doozan
 */
public class XMLBase {
    
    /**
     * Loads a tutorial from the given XML file
     * @param xmlFile  The XML file to read the tutorial from
     * @return  Tutorial that was loaded from the file
     */
    public static Tutorial loadTutorial(File xmlFile){
        String mission=null;
        Stage problemStatement=null;
        Stage limitations=null;
        Stage taskDiagram=null;
        Stage morphChart=null;
        

        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //throws ParserConfigurationException
            Document doc=db.parse(xmlFile); //throws SAXException, IOException
            
            mission = doc.getElementsByTagName("mission").item(0).getTextContent();
        
            NodeList stages=doc.getElementsByTagName("stage");
            for(int i=0;i<stages.getLength();i++){
                Element stageElement = (Element)stages.item(i);
                String stageID = stageElement.getAttribute("id");
                if(stageID.equals("problem_statement")){
                    problemStatement = loadStage(stageElement);
                }else if(stageID.equals("limitations")){
                    limitations = loadStage(stageElement);
                }else if(stageID.equals("task_diagram")){
                    taskDiagram = loadStage(stageElement);
                }else if(stageID.equals("morph_chart")){
                    morphChart = loadStage(stageElement);
                }
            }
        }catch(ParserConfigurationException | SAXException | IOException pce){
            //handle exception here
        }
        
        return new Tutorial(mission, problemStatement, limitations, taskDiagram, morphChart);
        
    }
    
    /**
     * Save the given Tutorial to a new XML file.
     * @param tutorial  The tutorial to save
     */
    public static void saveTutorial(Tutorial tutorial){
        //todo
    }
    
    /**
     * Load a stage from the XML document
     * @param stageElement  The root element of the stage tree
     * @return  The loaded stage
     */
    private static Stage loadStage(Element stageElement) {
        List<Option> options = null;
        NodeList optionElements = stageElement.getElementsByTagName("option");
        if(optionElements.getLength()>0){
            options = new ArrayList<Option>();
            for(int i=0;i<optionElements.getLength();i++){
                options.add(loadOption((Element)optionElements.item(i)));
            }
        }
        String videoPath = stageElement.getAttribute("video");
        long timeOnStage = 0; //this is time that a student spend on this stage, in milliseconds
        return new Stage(options, videoPath, timeOnStage);
    }
    
    /**
     * Recursive function to load an option and all sub-options from a given root option element
     * @param optionElement  The option element to load from
     * @return  The loaded Option
     */
    private static Option loadOption(Element optionElement){
        String desc = optionElement.getElementsByTagName("desc").item(0).getTextContent();
        boolean correct = optionElement.getAttribute("correct").equalsIgnoreCase("true");
        List<Option> subOptions = null;
        NodeList subOptionElements = optionElement.getElementsByTagName("option");
        if(subOptionElements.getLength()>0){
            subOptions = new ArrayList<Option>();
            for(int i=0;i<subOptionElements.getLength();i++){
                subOptions.add(loadOption((Element)subOptionElements.item(i)));
            }
        }
        Option parent = null; //set this to something
        String id = optionElement.getAttribute("oid");
        
        return new Option(desc, correct, subOptions, parent, id);
    }
    
    public static void main(String[] args){
        XMLBase.loadTutorial(new File("resources/sampleTutorial/tut1-Dan.xml"));
        
    }
    
}
