package org.lightmare.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.lightmare.deploy.management.DeployManager;
import org.lightmare.jetty.JettyServer;
import org.lightmare.listeners.LoaderListener;
import org.lightmare.servlets.PersonManager;

public class WebServer implements Runnable {

    private static final int HTTP_SERVER_PORT = 8080;

    private static final Logger LOG = Logger.getLogger(JettyServer.class);

    private static final ExecutorService POOL = Executors
	    .newSingleThreadExecutor();

    @Override
    public void run() {

	try {
	    Server jettyServer = new Server(HTTP_SERVER_PORT);
	    ContextHandlerCollection contexts = new ContextHandlerCollection();
	    jettyServer.setHandler(contexts);

	    ServletContextHandler ctxManager = new ServletContextHandler(
		    contexts, "/DeployManager", ServletContextHandler.SESSIONS);

	    DeployManager deploy = new DeployManager();
	    ServletHolder managerHolder = new ServletHolder();

	    managerHolder.setServlet(deploy);
	    ctxManager.addServlet(managerHolder, "/*");

	    ServletContextHandler ctxPerson = new ServletContextHandler(
		    contexts, "/", ServletContextHandler.SESSIONS);
	    ctxPerson.addEventListener(new LoaderListener());

	    PersonManager manager = new PersonManager();
	    ServletHolder personHolder = new ServletHolder();

	    personHolder.setServlet(manager);
	    ctxPerson.addServlet(personHolder, "/*");

	    contexts.setHandlers(new Handler[] { ctxManager, ctxPerson });
	    jettyServer.start();
	    jettyServer.join();

	} catch (Exception ex) {
	    LOG.error("Error while starting jetty server", ex);
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

	WebServer web = new WebServer();
	POOL.submit(web);
    }

}
