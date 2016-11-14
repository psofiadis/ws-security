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

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

import com.jgoodies.binding.list.SelectionInList;
import com.adva.mtosi.gui.beans.Notification;

/**
 * Provides the models and Actions for managing and editing Notifications.
 * Works with an underlying NotificationManager that provides a ListModel
 * for the Notifications and operations to add, remove, and change a Notification.
 * In other words, this class turns the raw data and operations
 * form the NotificationManager into a form usable in a user interface.<p>
 * 
 * This model keeps the Notifications in a SelectionInList, refers to another
 * PresentationModel for editing the selected Notification, and provides 
 * Actions for the Notification operations: add, remove and edit the selected Notification.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.11 $
 * 
 * @see com.jgoodies.binding.PresentationModel
 */

public final class NotificationManagerModel {
    
    /**
     * Holds the List of Notifications and provides operations
     * to create, add, remove and change a Notification.
     */
    private final NotificationManager notificationManager;
    
    /**
     * Holds the list of managed notifications plus a single selection.
     */
    private SelectionInList notificationSelection;

    private Action newAction;
    private Action editAction;
    private Action deleteAction;
    private Action sendRequestAction;

    // Instance Creation ******************************************************
    
    /**
     * Constructs an NotificationManager for editing the given list of Notifications.
     * 
     * @param notificationManager   the list of notifications to edit
     */
    public NotificationManagerModel(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
        initModels();
        initEventHandling();
    }
    
    
    /**
     * Initializes the SelectionInList and Action.
     * In this case we eagerly initialize the Actions. 
     * As an alternative you can create the Actions lazily 
     * in the Action getter methods. To synchronize the Action enablement 
     * with the selection state, we update the enablement now. 
     */
    private void initModels() {
        notificationSelection = new SelectionInList(notificationManager.getManagedNotifications());
        
        newAction = new NewAction();
        editAction = new EditAction();
        deleteAction = new DeleteAction();
        sendRequestAction = new SendRequestAction();
        updateActionEnablement();
    }
    
    
    /**
     * Initializes the event handling by just registering a
     * handler that updates the Action enablement if the
     * notificationSelection's 'selectionEmpty' property changes.
     */
    private void initEventHandling() {
        notificationSelection.addPropertyChangeListener(
                SelectionInList.PROPERTYNAME_SELECTION_EMPTY,
                new SelectionEmptyHandler());
    }
    
    
    // Exposing Models and Actions ********************************************
    
    /**
     * Returns the List of Notifications with the current selection.
     * Useful to display the managed Notifications in a JList or JTable.
     * 
     * @return the List of Notifications with selection
     */
    public SelectionInList getNotificationSelection() {
        return notificationSelection;
    }
    
    
    /**
     * Returns the Action that creates a new Notification and adds it 
     * to this model's List of managed Notifications. Opens a NotificationEditorDialog
     * on the newly created Notification.
     *  
     * @return the Action that creates and adds a new Notification
     */
    public Action getNewAction() {
        return newAction;
    }
    
    
    /**
     * Returns the Action that opens a NotificationEditorDialog on the selected Notification.
     * 
     * @return the Action that opens a NotificationEditorDialog on the selected Notification
     */
    public Action getEditAction() {
        return editAction;
    }
    
    
    /**
     * Returns the Action that deletes the selected Notification from
     * this model's List of managed notifications.
     * 
     * @return The Action that deletes the selected Notification
     */
    public Action getDeleteAction() {
        return deleteAction;
    }
    
    
    /**
     * Returns a MouseListener that selects and edits a Notification on double-click.
     * 
     * @return a MouseListener that selects and edits a Notification on double-click.
     */
    public MouseListener getDoubleClickHandler() {
        return new DoubleClickHandler();
    }
    
    
    // Action Operations ******************************************************
    // For every Action we provide a method that is performed for this Action.
    // This makes it easier to overview this class.
    
    private void doNew() {
        Object newNotification = createAndAddItem();
        getNotificationSelection().setSelection(newNotification);
    }

    private void doSendRequest() {
    }


    /**
     * Edits the selected item and marks it as changed, 
     * if the editor dialog has not been canceled.
     */
    private void doEdit() {
        editSelectedItem();
    }
    
    
    /**
     * Lets the NotificationManager removes the selected Notification from the list of Notifications.
     * The NotificationManager fires the list data change event. If the NotificationManager
     * wouldn't fire this event, we could use 
     * {@link com.jgoodies.binding.list.SelectionInList#fireIntervalRemoved(int, int)}.
     */
    private void doDelete() {
        notificationManager.removeItem(getSelectedItem());
    }
    
    
    // Managing Notifications ********************************************************
    
    /**
     * Lets the NotificationManager add the given Notification to the list of Notifications.
     * The NotificationManager fires the list data change event. If the NotificationManager
     * won't fire this event, we could use 
     * {@link com.jgoodies.binding.list.SelectionInList#fireIntervalAdded(int, int)}.
     */
    private void addItem(Notification notificationToAdd) {
        notificationManager.addItem(notificationToAdd);
    }
    
    /**
     * Opens a NotificationEditorDialog for the given Notification.
     * 
     * @param notification  the Notification to be edited
     * @return true if the dialog has been canceled, false if accepted
     */
    private boolean openNotificationEditor(Notification notification) {
//        NotificationEditorDialog dialog = new NotificationEditorDialog(null, notification);
//        dialog.open();
//        return dialog.hasBeenCanceled();
    return false;
    }
    
    private Notification createAndAddItem() {
        Notification newNofitication = notificationManager.createItem();
        boolean canceled = openNotificationEditor(newNofitication);
        if (!canceled) {
            addItem(newNofitication);
            return newNofitication;
        }
        return null;
    }
    
    /**
     * Edits the selected item. If the editor dialog has not been canceled,
     * the presentations is notified that the contents has changed.<p>
     * 
     * This implementation fires the contents change event using
     * {@link com.jgoodies.binding.list.SelectionInList#fireSelectedContentsChanged()}.
     * Since the notification SelectionInList contains a ListModel,
     * the <code>notificationSelection</code> managed by the NotificationManager,
     * the NotificationManager could fire that event. However, I favored to fire
     * the contents change in the SelectionInList because this approach
     * works with underlying Lists, ListModels, and managers that don't
     * fire contents changes.
     */
    private void editSelectedItem() {
        boolean canceled = openNotificationEditor(getSelectedItem());
        if (!canceled) {
            getNotificationSelection().fireSelectedContentsChanged();
        }
    }
    
    
    private Notification getSelectedItem() {
        return (Notification) getNotificationSelection().getSelection();
    }
    

    // Actions ****************************************************************
    
    private final class NewAction extends AbstractAction {
        
        private NewAction() {
            super("New\u2026");
        }
        
        public void actionPerformed(ActionEvent e) {
            doNew();
        }
    }

    private final class SendRequestAction extends AbstractAction {

        private SendRequestAction() {
            super("Send\u2026");
        }

        public void actionPerformed(ActionEvent e) {
            doSendRequest();
        }
    }

        
    private final class EditAction extends AbstractAction {
        
        private EditAction() {
            super("Edit\u2026");
        }
        
        public void actionPerformed(ActionEvent e) {
            doEdit();
        }
    }

        
    private final class DeleteAction extends AbstractAction {
        
        private DeleteAction() {
            super("Delete");
        }
        
        public void actionPerformed(ActionEvent e) {
            doDelete();
        }
    }

      
    // Event Handling *********************************************************
    
    /**
     * A mouse listener that edits the selected item on double click.
     */
    private final class DoubleClickHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                editSelectedItem();
        }
    }
    
    
    private void updateActionEnablement() {
        boolean hasSelection = getNotificationSelection().hasSelection();
        getEditAction().setEnabled(hasSelection);
        getDeleteAction().setEnabled(hasSelection);
    }
    
    
    /**
     * Enables or disables this model's Actions when it is notified
     * about a change in the <em>selectionEmpty</em> property
     * of the SelectionInList.
     */
    private final class SelectionEmptyHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            updateActionEnablement();
        }
    }
    
}
