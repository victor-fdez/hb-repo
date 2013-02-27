/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model.backend;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.Tutorial;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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
    
    public static Tutorial loadTutorial(File f){
        //Tutorial tut = new Tutorial();
        List<Option> subTasks;
        List<Option> taskDiagram;
        List<Option> morphologicalChart;
        List<Option> limitationsAndConstraints;

        try{    
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //throws ParserConfigurationException
            Document doc=db.parse(f); //throws SAXException, IOException
            NodeList stages=doc.getElementsByTagName("stage");
            /*
            for(int i=0;i<stages.getLength();i++){
                String stageID = ((Element)stages.item(i)).getAttribute("id");
                if(stageID.equals("problem_statement"))
                    ProblemStatement problemStatement = loadProblemStatement((Element)stages.item(i));
                else if(stageID.equals("limitations"))
                    Limitations limitations = loadLimitations((Element)stages.item(i));
                else if(stageID.equals("task_diagram"))
                    TaskDiagram taskDiagram = loadTaskDiagram((Element)stages.item(i));
                else if(stageID.equals("morph_chart"))
                    MorphChart morphologicalChart = loadMorphChart((Element)stages.item(i));
               
            }
            */
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
        //todo
    }
    
    /**test this*/
/*
    private static ProblemStatement loadProblemStatement(Element probStatementElement) {
        String desc = probStatementElement.getElementsByTagName("desc").item(0).getTextContent();
        
        List<Option> options = null;
        NodeList optionElements = probStatementElement.getElementsByTagName("option");
        if(optionElements.getLength()>0){
            options = new ArrayList<Option>();
            for(int i=0;i<optionElements.getLength();i++){
                options.add(loadOption((Element)optionElements.item(i)));
            }
        }
        String video = probStatementElement.getAttribute("video");
        int time = 0; //this is time that a student spend on this stage, in seconds
        return new ProblemStatement(desc, options, video, time);
    }
    */
    /**test this*/
    /*
    private static List<Option> loadLimitations(Element limtationsElement) {
        List<Option> options = null;
        NodeList optionElements = limtationsElement.getElementsByTagName("option");
        if(optionElements.getLength()>0){
            options = new ArrayList<Option>();
            for(int i=0;i<optionElements.getLength();i++){
                options.add(loadOption((Element)optionElements.item(i)));
            }
        }
        String video = limtationsElement.getAttribute("video");
        int time = 0; //this is time that a student spend on this stage, in seconds
        return new Limitaions(options, video, time);
    }
    */
    /**test this*/
    /*
    private static List<Option> loadTaskDiagram(Element taskDiagramElement) {
        List<Option> options = null;
        NodeList optionElements = taskDiagramElement.getElementsByTagName("option");
        if(optionElements.getLength()>0){
            options = new ArrayList<Option>();
            for(int i=0;i<optionElements.getLength();i++){
                options.add(loadOption((Element)optionElements.item(i)));
            }
        }
        String video = taskDiagramElement.getAttribute("video");
        int time = 0; //this is time that a student spend on this stage, in seconds
        return new TaskDiagram(options, video, time);
    }
    */
    /**test this*/
    private static List<Option> loadMorphChart(Document doc) {
        //todo
        return null;
    }
    
    
    /**test this*/
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
        XMLBase.loadTutorial(new File("tut1-Dan.xml"));
        
        
    }
    
    /** tbd, for testing 
    public static void writeFile() {
       try {
            BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
            for(int i=0; i<5; i++){
                out.write(i);
                out.newLine();
            }
            out.close();
        } catch(IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }*/
    
}
