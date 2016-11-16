package com.adva.mtosi.server.config;

//import org.apache.ws.security.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSPasswordCallback;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;


public class ClientPasswordCallback implements CallbackHandler {

  private static final String cxfPropertySuffix = "cxf.username";
  private static final String cxfPropertyPassword = ".password";

  private static Properties cxfProperties;
  private static Set<UserData> userDataSet;

  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    if(cxfProperties == null){
      loadCxfProperties();
    }
    for (int i = 0; i < callbacks.length; i++) {

      WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];

      if (pc.getUsage() == WSPasswordCallback.SIGNATURE
          || pc.getUsage() == WSPasswordCallback.DECRYPT) {

        if (pc.getIdentifier().equals("nms-client-key"))
          pc.setPassword("A1b.5*_B81chgme78");
      }else if(pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN){

          pc.setPassword(getPasswordByName(pc.getIdentifier()));
      }
    }

  }

  private void loadCxfProperties() {
    InputStream cxfPropertiesFile;
    try {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      cxfPropertiesFile = loader.getResourceAsStream("cxf.properties");
      cxfProperties = new Properties();
      cxfProperties.load(cxfPropertiesFile);
      userDataSet = new HashSet<>();
      final Iterator<Object> keyIterator = cxfProperties.keySet().iterator();
      while(keyIterator.hasNext()) {
        String key = keyIterator.next().toString();
        if (key.startsWith(cxfPropertySuffix) && !key.endsWith(cxfPropertyPassword)) {
          userDataSet.add(new UserData((String)cxfProperties.get(key)));
        }
      }
      for(UserData userData : userDataSet){
        userData.setPassword((String) cxfProperties.get(cxfPropertySuffix+"."+userData.getUserName()+cxfPropertyPassword));
      }
    }catch (IOException ex){

    }
  }

  private String getPasswordByName(String userName){
    for(UserData userData : userDataSet){
      if(userData.getUserName().equals(userName)) return userData.getPassword();
    }
    return null;
  }

  class UserData {
    private String userName;
    private String password;

    public UserData(String userName) {
      this.userName = userName;
    }


    public UserData(String userName, String password) {
      this.userName = userName;
      this.password = password;
    }

    public UserData() {
    }

    public String getUserName() {
      return userName;
    }

    public void setUserName(String userName) {
      this.userName = userName;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }
}
