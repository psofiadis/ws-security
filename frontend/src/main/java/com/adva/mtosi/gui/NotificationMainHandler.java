/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: $ 
 */

package com.adva.mtosi.gui;

import com.adva.mtosi.gui.beans.Notification;
import com.adva.mtosi.gui.utils.NotificationManager;
import com.adva.mtosi.gui.utils.NotificationManagerModel;
import com.adva.mtosi.gui.utils.NotificationManagerView;
import com.adva.mtosi.gui.utils.NotificationUtils;
import javax.swing.*;

public final class NotificationMainHandler {

    public static NotificationManagerView INSTANCE;
    public static NotificationManager notificationManager;

    public static void main(String args[]){
        try {
          UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Notification Receiver :: ver.0.1.1");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        java.util.List notifications = Notification.NOTIFICATIONS;
        notificationManager = new NotificationManager(notifications);
        NotificationManagerModel model = new NotificationManagerModel(notificationManager);
        INSTANCE = new NotificationManagerView(model);
        JComponent panel = INSTANCE.buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        NotificationUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }

}
