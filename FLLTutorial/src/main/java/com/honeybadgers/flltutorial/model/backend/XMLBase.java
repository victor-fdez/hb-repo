/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model.backend;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.Stage;
import com.honeybadgers.flltutorial.model.Tutorial;
import com.honeybadgers.flltutorial.model.TutorialBase;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
     * Load a Tutorial Base (ie. basic tutorial information, not the full
     * tutorial) from an XML file.
     *
     * @param tutorialFile File to load Tutorial Base from.
     * @return The Tutorial Base loaded from the file.
     */
    public static TutorialBase loadTutorialBase(File tutorialFile) {
        Document doc = loadDOM(tutorialFile);
        if(doc==null){
            return null;
        }
        
        Element rootElement = (Element) doc.getElementsByTagName("tutorial").item(0);

        String title = rootElement.getAttribute("name");
        String author = rootElement.getAttribute("author");
        String description = rootElement.getElementsByTagName("mission").item(0).getTextContent();
        
        return new TutorialBase(title, author, description);
    }

    
    private static Document loadDOM(File xmlFile){
        Document doc=null;
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //throws ParserConfigurationException
            doc = docBuilder.parse(xmlFile); //throws SAXException, IOException
        } catch (ParserConfigurationException | SAXException | IOException e) {
            //handle exception here
            e.printStackTrace();
        }
        return doc;
    }
    /**
     * Loads a tutorial from the given XML file
     *
     * @param tutorialFile The XML file to read the tutorial from
     * @return Tutorial that was loaded from the file
     */
    public static Tutorial loadTutorial(File tutorialFile) {
        List<Stage> stages = new ArrayList<>();
        List<String> members = new ArrayList<>();
        String projectName = "";
        String teamName = "";
        
        Document doc = loadDOM(tutorialFile);
        Element rootElement = (Element) doc.getElementsByTagName("tutorial").item(0);
        
        String tutorialName = rootElement.getAttribute("name");
        String mission = rootElement.getElementsByTagName("mission").item(0).getTextContent();
        
        //a clean tutorial will not have this information
        NodeList projectInfo = rootElement.getElementsByTagName("project");
        if(projectInfo.getLength() > 0)
        {
            Element projectElement = (Element)projectInfo.item(0);
            Element teamNode = (Element)projectElement.getElementsByTagName("team").item(0);
            System.out.println(teamNode);
            
            projectName = projectElement.getAttribute("name");
            teamName = teamNode.getAttribute("name");
            
            NodeList teamMemberNodes = teamNode.getElementsByTagName("member");
            for(int i = 0; i < teamMemberNodes.getLength(); i++)
            {
                String memberName = teamMemberNodes.item(i).getTextContent();
                members.add(memberName);
            }
        }
        
        NodeList stageNodeList = doc.getElementsByTagName("stage");
        for(int i = 0; i < stageNodeList.getLength(); i++) {
            Element stageElement = (Element) stageNodeList.item(i);
            Stage stage = loadStage(stageElement);
            stages.add(stage);
            //printStage(stage);
        }

        return new Tutorial(tutorialName, mission, stages, projectName, teamName, members);
    }

    /**
     * Load a stage from the XML document
     *
     * @param stageElement The root element of the stage tree
     * @return The loaded stage
     */
    private static Stage loadStage(Element stageElement) {
        String name = stageElement.getAttribute("name");
        String videoPath = stageElement.getAttribute("video");
        long timeOnStage = Long.parseLong(stageElement.getAttribute("time")); //this is time that a student spent on this stage, in milliseconds

        Option rootOption = loadOption((Element) stageElement.getElementsByTagName("option").item(0));

        return new Stage(name, rootOption, videoPath, timeOnStage);
    }

    /**
     * Recursive function to load an option and all sub-options from a given
     * root option element
     *
     * @param optionElement The option element to load from
     * @return The loaded Option
     */
    private static Option loadOption(Element optionElement) {
        //default values
        String oid = "";
        boolean correct = true;
        int position = -2;
        int selected = -1;
        String desc = "";
        String reason = "";
        String imagePath = "";
        List<Option> subOptions = new ArrayList<>();

        Option parent = null; //todo: set this to something

        //load the attributes
        if (optionElement.hasAttribute("oid")) {
            oid = optionElement.getAttribute("oid");
        }
        if (optionElement.hasAttribute("correct")) {
            correct = optionElement.getAttribute("correct").equalsIgnoreCase("true");
        }
        if (optionElement.hasAttribute("position")) {
            position = Integer.parseInt(optionElement.getAttribute("position"));
        }
        if (optionElement.hasAttribute("selected")) {
            selected = Integer.parseInt(optionElement.getAttribute("selected"));
        }

        NodeList children = optionElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) children.item(i);
                switch (childElement.getTagName()) {
                    case "desc":
                        desc = childElement.getTextContent();
                        break;
                    case "reason":
                        reason = childElement.getTextContent();
                        break;
                    case "img":
                        imagePath = childElement.getAttribute("src");
                        break;
                    case "option":
                        subOptions.add(loadOption(childElement));
                        break;

                }
            }
        }

        return new Option(desc, correct, subOptions, parent, oid, reason, position, selected, imagePath);
    }

    /**
     * Save the given Tutorial to a new XML file.
     *
     * @param tutorial The tutorial to save
     */
    public static void saveTutorial(Tutorial tutorial) {
        String name = tutorial.getName();
        String fileName = tutorial.getFileName();
        String mission = tutorial.getMission();
        List<Stage> stages = tutorial.getStages();

        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //throws ParserConfigurationException
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("tutorial");
            rootElement.setAttribute("name", name);
            doc.appendChild(rootElement);

            //add mission element
            Element missionElement = doc.createElement("mission");
            missionElement.appendChild(doc.createTextNode(mission));
            rootElement.appendChild(missionElement);
            
            //add project information
            Element projectElement = doc.createElement("project");
            projectElement.setAttribute("name", tutorial.getProjectName());
            rootElement.appendChild(projectElement);
            
            //add team to project
            Element teamElement = doc.createElement("team");
            teamElement.setAttribute("name", tutorial.getTeamName());
            projectElement.appendChild(teamElement);
            
            //add all members to team
            for(String memberName : tutorial.getMembers())
            {
                Element member = doc.createElement("member");
                member.appendChild(doc.createTextNode(memberName));
                teamElement.appendChild(member);
            }
            
            //add team information

            for (Stage stage : stages) {
                rootElement.appendChild(constructStage(stage, doc));
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            System.out.println("trying to save to src/main/resources/Projects/"+name+"/"+fileName);
            StreamResult result = new StreamResult(new File("src/main/resources/Projects/"+name+"/"+fileName)); //***not sure if I can save here

            // Output to console for testing
            //StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result); //throws TransformerException

            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException e) {
            //handle exception here
            e.printStackTrace();
        }

    }

    private static Element constructStage(Stage stage, Document doc) {
        String name = stage.getName();
        String videoPath = stage.getVideoPath();
        long timeOnStage = stage.getTimeOnStage();
        Option rootOption = stage.getRootOption();

        Element stageElement = doc.createElement("stage");
        stageElement.setAttribute("name", name);
        stageElement.setAttribute("video", videoPath);
        stageElement.setAttribute("time", String.valueOf(timeOnStage));
        stageElement.appendChild(constructOption(rootOption, doc));

        return stageElement;
    }

    private static Element constructOption(Option option, Document doc) {
        String description = option.getDescription();
        String reason = option.getReason();
        String imagePath = option.getImagePath();
        boolean correct = option.isCorrect();
        String id = option.getId();
        Option parentOption = option.getParent();
        int position = option.getPosition();
        int selected = option.getSelected();

        Element optionElement = doc.createElement("option");
        optionElement.setAttribute("oid", id);
        optionElement.setAttribute("correct", String.valueOf(correct));
        optionElement.setAttribute("position", String.valueOf(position));
        optionElement.setAttribute("selected", String.valueOf(selected));

        //add desc element
        Element descElement = doc.createElement("desc");
        descElement.appendChild(doc.createTextNode(description));
        optionElement.appendChild(descElement);

        //add reason element
        Element reasonElement = doc.createElement("reason");
        reasonElement.appendChild(doc.createTextNode(reason));
        optionElement.appendChild(reasonElement);

        //add img element
        if (imagePath != null) {
            Element imgElement = doc.createElement("img");
            imgElement.setAttribute("src", imagePath);
            optionElement.appendChild(imgElement);
        }

        List<Option> subOptions = option.getOptions();
        for (Option subOption : subOptions) {
            optionElement.appendChild(constructOption(subOption, doc));
        }
        return optionElement;
    }

    public static void main(String[] args) {
        Tutorial t = XMLBase.loadTutorial(new File("src/main/resources/sampleTutorial/tut1-Dan.xml"));
        XMLBase.saveTutorial(t);
    }

    private static void printStage(Stage stage) {
        System.out.println(stage.getName());
        printOption(stage.getRootOption(), 1);
    }

    private static void printOption(Option option, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(option.getDescription());
        if (option.getOptions() != null) {
            for (Option op : option.getOptions()) {
                printOption(op, level + 1);
            }
        }
    }
}
