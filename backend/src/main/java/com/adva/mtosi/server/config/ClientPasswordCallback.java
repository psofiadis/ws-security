/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: ClientPasswordCallback.java 96000 2016-02-08 10:56:27Z ext_psofiadis $
 */
package com.adva.mtosi.server.config;

//import org.apache.ws.security.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSPasswordCallback;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;


public class ClientPasswordCallback implements CallbackHandler {

  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

    for (int i = 0; i < callbacks.length; i++) {

      WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];

      if (pc.getUsage() == WSPasswordCallback.SIGNATURE
          || pc.getUsage() == WSPasswordCallback.DECRYPT) {

        if (pc.getIdentifier().equals("nms-client-key"))
          pc.setPassword("A1b.5*_B81chgme78");
      }else if(pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN){

          pc.setPassword("password");
      }
    }

  }
}
