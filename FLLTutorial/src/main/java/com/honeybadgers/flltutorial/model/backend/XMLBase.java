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
import org.w3c.dom.Node;
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
                switch (stageID) {
                    case "problem_statement":
                        problemStatement = loadStage(stageElement);
                        break;
                    case "limitations_constraints":
                        limitations = loadStage(stageElement);
                        break;
                    case "task_diagram":
                        taskDiagram = loadStage(stageElement);
                        break;
                    case "morph_chart":
                        morphChart = loadStage(stageElement);
                        break;
                }
            }
        }catch(ParserConfigurationException | SAXException | IOException pce){
            //handle exception here
        }
        //printStage(morphChart);
        
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
        List<Option> options = new ArrayList<>();
        NodeList children = stageElement.getChildNodes();
        for(int i=0; i<children.getLength(); i++){
            if(children.item(i).getNodeType() == Node.ELEMENT_NODE){
                Element childElement = (Element)children.item(i);
                if(childElement.getTagName().equals("option")){
                    options.add(loadOption(childElement));
                }
            }
        }
        
        String videoPath = stageElement.getAttribute("video");
        long timeOnStage = 0; //this is time that a student spent on this stage, in milliseconds
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
        List<Option> subOptions = new ArrayList<>();
        
        NodeList children = optionElement.getChildNodes();
        for(int i=0; i<children.getLength(); i++){
            if(children.item(i).getNodeType() == Node.ELEMENT_NODE){
                Element childElement = (Element)children.item(i);
                if(childElement.getTagName().equals("option")){
                    subOptions.add(loadOption(childElement));
                }
            }
        }
        
        Option parent = null; //todo: set this to something
        String id = optionElement.getAttribute("oid");
        
        return new Option(desc, correct, subOptions, parent, id);
    }
    
    public static void main(String[] args){
        Tutorial t = XMLBase.loadTutorial(new File("src/main/resources/sampleTutorial/tut1-Dan.xml"));
    }
    
    public static void printStage(Stage stage){
        System.out.println(stage.getVideoPath());
        for(Option op : stage.getOptions()){
            printOption(op,0);
        }
    }
    
    public static void printOption(Option option, int level){
        for(int i=0;i<level;i++){
            System.out.print("\t");
        }
        System.out.println(option.getDescription());
        if(option.getOptions()!=null){
            for(Option op : option.getOptions()){
                printOption(op, level+1);
            }
        }
    }
    
}
