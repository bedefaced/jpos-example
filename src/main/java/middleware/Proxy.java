package middleware;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.ws.Endpoint;

import middleware.webservice.Webservice;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.q2.Q2;
import org.jpos.q2.QBean;
import org.jpos.util.Log;

public class Proxy implements QBean, ProxyMBean, Configurable {

    private volatile int state;
    private Log log;
    private int webservicePort;
    private int webserviceThreads;
    private int transactionTimeout;
    private Endpoint webserviceEndpoint;

    public Proxy() {
	state = -1;
	log = Log.getLog(Q2.LOGGER_NAME, "middleware");
	log.info("constructor");
    }

    @Override
    public void init() throws Exception {
	log.info("initializing");
	state = QBean.STARTING;
    }

    @Override
    public void start() throws Exception {
	log.info("starting");
	log.info("webservice port: " + this.getWebservicePort() + ", threadpool: " + this.getWebserviceThreads());
	/* create webservice endpoint and publish */
	this.webserviceEndpoint = Endpoint.create(new Webservice(this.getTransactionTimeout()));
	ExecutorService executor = Executors.newFixedThreadPool(this.getWebserviceThreads());
	this.webserviceEndpoint.setExecutor(executor);
	this.webserviceEndpoint.publish("http://0.0.0.0:" + this.getWebservicePort() + "/ws/");
	state = QBean.STARTED;
    }

    @Override
    public void stop() throws Exception {
	log.info("stopping");
	/* stop endpoing */
	this.webserviceEndpoint.stop();
	state = QBean.STOPPED;
    }

    @Override
    public void destroy() throws Exception {
	log.info("destroying");
	state = QBean.STOPPED;
    }

    @Override
    public int getState() {
	return state;
    }

    @Override
    public String getStateAsString() {
	return state >= 0 ? QBean.stateString[state] : "Unknown";
    }

    public int getWebservicePort() {
	return webservicePort;
    }

    public void setWebservicePort(int webservicePort) {
	this.webservicePort = webservicePort;
    }

    public int getWebserviceThreads() {
	return webserviceThreads;
    }

    public void setWebserviceThreads(int webserviceThreads) {
	this.webserviceThreads = webserviceThreads;
    }

    @Override
    public void setConfiguration(Configuration cfg)
	    throws ConfigurationException {
	this.setWebservicePort(cfg.getInt("webservicePort", 100));
	this.setWebserviceThreads(cfg.getInt("webserviceThreads", 10));
	this.setTransactionTimeout(cfg.getInt("transactionTimeout", 10000));
    }

    int getTransactionTimeout() {
	return transactionTimeout;
    }

    void setTransactionTimeout(int transactionTimeout) {
	this.transactionTimeout = transactionTimeout;
    }

}
