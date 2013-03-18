/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model;

import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author chingaman
 */
public class OptionTest extends TestCase {
    
    public OptionTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getParent method, of class Option.
     */
    public void testGetParent() {
        
    }

    /**
     * Test of addChild method, of class Option.
     */
    public void testAddChild() {
       
    }

    /**
     * Test of getChildAtIndex method, of class Option.
     */
    /*public void testGetChildAtIndex() {
        Option option = new Option("some description", true, null);
        option.addChild(new Option("some description", true, null));
        option.addChild(new Option("some description", true, null));
        option.addChild(new Option("some description", true, null));
        Option child1st = option.getChildAtIndex(0);
        Option child3rd = option.getChildAtIndex(2);
        assertEquals(".0", child1st.getId());
        assertEquals(".2", child3rd.getId());
        option.addChild(new Option("some description", true, null));
        child1st.addChild(new Option("some decription", true, null));
        child1st.addChild(new Option("some decription", true, null));
        child1st.addChild(new Option("some decription", true, null));
        child3rd.addChild(new Option("some decription", true, null));
        child3rd.addChild(new Option("some decription", true, null));
        Option child3rd2nd = child3rd.getChildAtIndex(1);
        assertEquals(".2.1", child3rd2nd.getId());
        child3rd2nd.addChild(new Option("some decription", true, null));
        child3rd2nd.addChild(new Option("some decription", true, null));
        child3rd2nd.addChild(new Option("some decription", true, null));
        Option child3rd2nd3rd = child3rd2nd.getChildAtIndex(2);
        assertEquals(".2.1.2", child3rd2nd3rd.getId());
        assertEquals(".2.1", child3rd2nd.getId());
        assertEquals(".0.2", option.getChildWithId(".0.2").getId());
    }

    public void testGetChildWithId()
    {
        Option option = new Option("some description", true, null);
        option.addChild(new Option("some description", true, null));
        option.addChild(new Option("some description", true, null));
        option.addChild(new Option("some description", true, null));
        Option child1st = option.getChildAtIndex(0);
        Option child3rd = option.getChildAtIndex(2);
        option.addChild(new Option("some description", true, null));
        child1st.addChild(new Option("some decription", true, null));
        child1st.addChild(new Option("some decription", true, null));
        child1st.addChild(new Option("some decription", true, null));
        child3rd.addChild(new Option("some decription", true, null));
        child3rd.addChild(new Option("some decription", true, null));
        Option child3rd2nd = child3rd.getChildAtIndex(1);
        child3rd2nd.addChild(new Option("some decription", true, null));
        child3rd2nd.addChild(new Option("some decription", true, null));
        child3rd2nd.addChild(new Option("some decription", true, null));
        Option child3rd2nd3rd = child3rd2nd.getChildAtIndex(2);
        /*test root finds
        assertEquals(child3rd2nd3rd, option.getChildWithId(child3rd2nd3rd.getId()));
        assertEquals(child1st.getChildAtIndex(1), option.getChildWithId(child1st.getChildAtIndex(1).getId()));
        /*some child finds
        assertEquals(child3rd2nd3rd, child3rd.getChildWithId(child3rd2nd3rd.getId()));
    }*/

    /**
     * Test of equals method, of class Option.
     */
    public void testEquals() {
       
    }

    /**
     * Test of getChildIndex method, of class Option.
     */
    public void testGetChildIndex() {
        
    }
    
}
