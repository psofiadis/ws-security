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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Builds a user interface for managing Notifications using a table to display
 * the Notifications and buttons to add, edit, and delete the selected notification.
 * The models and Actions are provided by an underlying NotificationManagerModel.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.3 $
 *
 * @see com.jgoodies.binding.PresentationModel
 */

public final class NotificationManagerView {
    
    /**
     * Provides a list of Notification with selection and Action
     * for operating on the managed Notifications.
     */
    public final NotificationManagerModel notificationManagerModel;

    public JTable notificationTable;
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton sendRequestButton;

 
    // Instance Creation ******************************************************
    
    /**
     * Constructs a list editor for editing the given list of notifications.
     * 
     * @param notificationManagerModel   the list of notification to edit
     */
    public NotificationManagerView(NotificationManagerModel notificationManagerModel) {
        this.notificationManagerModel = notificationManagerModel;
    }
    
    // Component Creation and Initialization **********************************

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        notificationTable = new JTable();
        notificationTable.setModel(NotificationUtils.createNotificationTableModel(
                notificationManagerModel.getNotificationSelection()));
        notificationTable.setSelectionModel(
                new SingleListSelectionAdapter(
                notificationManagerModel.getNotificationSelection().getSelectionIndexHolder()));
        
        newButton = new JButton(notificationManagerModel.getNewAction());
        editButton = new JButton(notificationManagerModel.getEditAction());
        deleteButton = new JButton(notificationManagerModel.getDeleteAction());
        sendRequestButton = new JButton(notificationManagerModel.getDeleteAction());
    }
    
    private void initEventHandling() {
        notificationTable.addMouseListener(notificationManagerModel.getDoubleClickHandler());
    }
    
    // Building ***************************************************************

    /**
     * Builds and returns the panel for the Notification Manager View.
     * 
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents();
        initEventHandling();

        FormLayout layout = new FormLayout(
                "fill:900dlu:grow",
                "p, 1dlu, fill:500dlu, 6dlu, p");
                
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        builder.addTitle("Events",               cc.xy(1, 1));
        builder.add(new JScrollPane(notificationTable), cc.xy(1, 3));
        builder.add(buildButtonBar(),            cc.xy(1, 5));
         
        return builder.getPanel();
    }

    private JComponent buildButtonBar() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addButton(newButton);
        builder.addButton(editButton);
        builder.addButton(deleteButton);
        return builder.build();
    }
    
    
}
