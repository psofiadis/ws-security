/*
 * Copyright (c) 2002-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.adva.mtosi.gui.utils;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.TableModel;
import com.adva.mtosi.gui.beans.Notification;
import com.jgoodies.binding.adapter.AbstractTableAdapter;


/**
 * Consists only of static methods that return instances
 * reused in multiple examples of the JGoodies Binding tutorial.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.17 $
 */
public final class NotificationUtils {
    
   
    private NotificationUtils() {
        // Suppresses default constructor, ensuring non-instantiability.
    }
    
    
    /**
     * Creates and returns a TableModel for Notifications with columns
     * for the title, artist, classical and composer.
     *  
     * @param listModel   the ListModel of Notifications to display in the table
     * @return a TableModel on the list of Notifications
     */
    public static TableModel createNotificationTableModel(ListModel listModel) {
        return new NotificationTableModel(listModel);
    }
    
    
    /**
     * Locates the given component on the screen's center.
     * 
     * @param component   the component to be centered
     */
    public static void locateOnOpticalScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
            (screenSize.width  - paneSize.width)  / 2,
            (int) ((screenSize.height - paneSize.height) *0.45));
    }
    
    
    // TableModel *************************************************************
    
    /**
     * Describes how to present an Notification in a JTable.
     */
    public static final class NotificationTableModel extends AbstractTableAdapter {
        
        private static final String[] COLUMNS = {"MD", "NE", "AID", "Additional Text", "Os Time",
            "Native Probable Cause","Probable Cause",
//            "Module Type", "Entity Alias",
            "Source Time", "Service Affecting",
            "Category", "Perceived Severity", "Notification Id","Layer Rate"
            //"Security", "Impairment"
        };
        private final ListModel listModel;
        private NotificationTableModel(ListModel listModel) {
            super(listModel, COLUMNS);
            this.listModel = listModel;
        }

        @Override
        public ListModel getListModel() {
            return listModel;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Notification notification = (Notification) getRow(rowIndex);
            switch (columnIndex) {
                case 0:  return notification.getMd();
                case 1:  return notification.getNe();
                case 2:  return notification.getAid();
                case 3:  return notification.getAdditionalText();
                case 4:  return notification.getOsTime();
                case 5:  return notification.getNativeProbableCause();
                case 6:  return notification.getProbableCause();
                case 7:  return notification.getSourceTime();
                case 8:  return notification.getServiceAffecting();
                case 9:  return notification.getCategory();
                case 10 : return notification.getPerceivedSeverity();
                case 11 : return notification.getNotificationId();
                case 12 : return notification.getLayerRate();
                default :
                    throw new IllegalStateException("Unknown column");
            }
        }
        
    }

    
}
