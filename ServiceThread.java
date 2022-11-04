/*
 * 
 * XMU CNNS Class Demo
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServiceThread extends Thread {

    Socket welcomeSocket;
	Map<String, String> fileCache = new HashMap<>();
	int cacheSize = 8096;
	Map<String, String> cfgMap = new HashMap<>();

    public ServiceThread(Socket welcomeSocket, Map<String, String> cfgMap, Map<String, String> fileCache, int cacheSize) {
	    this.welcomeSocket = welcomeSocket;
		this.cfgMap = cfgMap;
		this.fileCache = fileCache;
		this.cacheSize = cacheSize;
    }
  
    public void run() {

	    System.out.println("Thread " + this + " started.");

//	        synchronized (welcomeSocket) {
		        try {
					// process a request
					WebRequestHandler wrh =
						new WebRequestHandler( welcomeSocket, cfgMap, fileCache, cacheSize);

					wrh.processRequest();
		        } catch (Exception e) {
		        }
//	        } // end of extract a request

//	       serveARequest( s );
		
    } // end run

    private void serveARequest(Socket connSock) {
    	
	    try {
	        // create read stream to get input
	        BufferedReader inFromClient = 
		       new BufferedReader(new InputStreamReader(connSock.getInputStream()));
	        String clientSentence = inFromClient.readLine();

	        // process input
	        String capitalizedSentence = clientSentence.toUpperCase() + '\n';

	        // send reply
	        DataOutputStream outToClient = 
	            new DataOutputStream(connSock.getOutputStream());
	        outToClient.writeBytes(capitalizedSentence);

	        connSock.close();

	        System.out.println("Finish a request");

	    } catch (Exception e) {
	        System.out.println("Exception happened in Thread " + this);
	    } // end of catch

    } // end of serveARequest

} // end ServiceThread
