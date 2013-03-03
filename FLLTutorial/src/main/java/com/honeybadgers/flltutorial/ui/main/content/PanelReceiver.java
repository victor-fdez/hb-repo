/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import java.awt.Point;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
public interface PanelReceiver {
    void receivePanel(JPanel panelSent, Point point);
}
