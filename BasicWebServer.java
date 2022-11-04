/**
 ** Sequential and Per-thread HTTP Servers: service threads competing on welcome socket
 ** Usage: java BasicWebServer -config <config_file_name>
 **
 **/

import java.io.*;
import java.net.*;
import java.util.*;

class BasicWebServer{

    public static int serverPort = 6789;    
    //public static String WWW_ROOT = "/home/httpd/html/zoo/classes/cs433/";
    public static String WWW_ROOT = "./";

	public static int cacheSize = 8096;

	public static int reqCnt = 0;

	static HashMap<String, String> cfgMap = new HashMap<String, String>();
	static Map<String, String> fileCache = new HashMap<>();
    public static void main(String args[]) throws Exception  {
	
	// see if there is .conf
	cfgMap.put("vb_default", "./");
	String confName = "httpd.conf";
	if (args.length >= 2)
		if (args[0].equals("-config")) {
			confName = args[1];
			FileTest cfg = new FileTest();
			String cfgFileContent = cfg.cfgRead(confName);
			cfgMap = cfg.generateCfgMap(cfgFileContent);
			serverPort = Integer.parseInt(cfgMap.get("Listen"));
			cacheSize = Integer.parseInt(cfgMap.get("CacheSize"));
			System.out.println(cfgMap.get("vb_yunxi.site"));
			System.out.println(cfgMap.toString());
		}

	HeartbeatMonitor cm = new HeartbeatMonitor();
	// create server socket
	ServerSocket listenSocket = new ServerSocket(serverPort);
	System.out.println("server listening at: " + listenSocket);
	System.out.println("server www root: " + WWW_ROOT);

	while (true) {
	    try {
		    // take a ready connection from the accepted queue
		    Socket connectionSocket = listenSocket.accept();
		    System.out.println("\nReceive request from " + connectionSocket);
			reqCnt++;
		    // process a request
		    WebRequestHandler wrh = 
		        new WebRequestHandler( connectionSocket, cfgMap, fileCache, cacheSize);
		    wrh.processRequest();
			cm.Mnt(connectionSocket);
	    } catch (Exception e)
		{
		}
	} // end of while (true)
	
    } // end of main

} // end of class WebServer
