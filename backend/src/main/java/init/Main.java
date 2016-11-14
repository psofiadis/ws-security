/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: $ 
 */

package init;

import com.adva.mtosi.gui.NotificationMainHandler;
import com.adva.mtosi.server.config.WebServer;
import com.adva.mtosi.server.config.WebServerJar;

public class Main {
    public static void main(String[] args) throws Exception{
        NotificationMainHandler.main(null);
        new WebServerJar(9090).start();

    }
}
