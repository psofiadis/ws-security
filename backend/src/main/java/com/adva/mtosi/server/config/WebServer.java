/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: $ 
 */

package com.adva.mtosi.server.config;

import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebServer {

    private static final String DEFAULT_PROFILE = "dev";
    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";
    private static ServletContextHandler context;

    private Server server;
    private int port;
    private String bindInterface;

    public WebServer(int aPort) {
        this(aPort, null);
    }

    public WebServer(int aPort, String aBindInterface) {
        port = aPort;
        bindInterface = aBindInterface;
    }

    public void start() throws Exception {
        server = new Server(port);
//        context = getServletContextHandler(getContext());
//        server.setHandler(context);
        System.out.println("Reached critical position " + WebServer.class.getClassLoader().getResource("."));
//        String s = getClass().getName();
//        int i = s.lastIndexOf(".");
//        if(i > -1) s = s.substring(i + 1);
//        s = s + ".class";
//        System.out.println("name " +s);
        String relativeRootPath, pathToRoot ;
        if(WebServer.class.getClassLoader().getResource(".") != null){
            relativeRootPath = this.getClass().getClassLoader().getResource(".").toString();
            pathToRoot = relativeRootPath + "../../src/main/webapp";
        }else{
            pathToRoot = "../../webapp";
        }
//        System.out.println("Reached critical position: " +  rootPath);
        WebAppContext webapp = new WebAppContext(pathToRoot, "");
        webapp.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
        webapp.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        webapp.addBean(new ServletContainerInitializersStarter(webapp), true);
        // This webapp will use jsps and jstl. We need to enable the
        // AnnotationConfiguration in order to correctly
        // set up the jsp container
//        Configuration.ClassList classlist = Configuration.ClassList
//                .setServerDefault( server );
//        classlist.addBefore(
//                "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
//                "org.eclipse.jetty.annotations.AnnotationConfiguration" );

//        context.addServlet(jspServletHolder(), "*.jsp");
        server.setHandler(webapp);

        server.start();
        server.join();
    }

    private List<ContainerInitializer> jspInitializers()
    {
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = new ArrayList<ContainerInitializer>();
        initializers.add(initializer);
        return initializers;
    }

//    private ServletHolder jspServletHolder()
//    {
//        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
//        holderJsp.setInitOrder(0);
//        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
//        holderJsp.setInitParameter("fork", "false");
//        holderJsp.setInitParameter("xpoweredBy", "false");
//        holderJsp.setInitParameter("compilerTargetVM", "1.7");
//        holderJsp.setInitParameter("compilerSourceVM", "1.7");
//        holderJsp.setInitParameter("keepgenerated", "true");
//        return holderJsp;
//    }

    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
//        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
//        contextHandler.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
        contextHandler.addEventListener(new ContextLoaderListener(context));
//        ((XmlWebApplicationContext) context).setServletContext((ServletContext) contextHandler);
        ((XmlWebApplicationContext) context).refresh();
        contextHandler.setResourceBase(new ClassPathResource("/webapp/WEB-INF").getURI().toString());
        return contextHandler;
    }

//    private WebApplicationContext getContext() {
//        XmlWebApplicationContext context = new XmlWebApplicationContext();
//        context.setConfigLocation("");
//        context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);
//        return context;
//    }
}
