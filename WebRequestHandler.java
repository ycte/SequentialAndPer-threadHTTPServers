/**
 ** WebRequestHandler with socket
 **/
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

class WebRequestHandler {

    static boolean _DEBUG = true;
    static int     reqCount = 0;

    String WWW_ROOT;
    Socket connSocket;
    BufferedReader inFromClient;
    DataOutputStream outToClient;

    String urlName;
    String fileName;
    File fileInfo;

	Map<String, String> fileCache;
	Map<String, String> cfgMap;
    public WebRequestHandler(Socket connectionSocket, 
			     Map<String, String> cfgMap, Map<String, String> fileCache) throws Exception
    {
        reqCount ++;
		this.fileCache = fileCache;
	    this.connSocket = connectionSocket;
		this.cfgMap = cfgMap;
	    inFromClient =
	      new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
	    outToClient =
	      new DataOutputStream(connSocket.getOutputStream());
    }

    public void processRequest() 
    {
		try {
	    	mapURL2File();
	    	if ( fileInfo != null ) // found the file and knows its info
	    	{
		    	outputResponseHeader();
		    	outputResponseBody();
	    	} // dod not handle error
	    connSocket.close();
		} catch (Exception e) {
			DEBUG("What error?");
	    	outputError(400, "Server error");
		}
    } // end of processRequest

    private void mapURL2File() throws Exception 
    {
		// Read GET message
	    String requestMessageLine = inFromClient.readLine();
	    DEBUG("Request " + reqCount + ": " + requestMessageLine);

		// Read userAgent and host chosen
		String sentenceFromClient, userAgent = "Mozilla/4.0", vb_server = "default";
		String[] userAgentArray, vb_serverArray;
		while (!(sentenceFromClient = inFromClient.readLine()).equals("")) {
//			DEBUG(sentenceFromClient);
			if(sentenceFromClient.charAt(0) == 'U') {
				userAgentArray = sentenceFromClient.split("\\s");
				for (int i = 1; i < userAgentArray.length; i++) {
					userAgent = userAgent + userAgentArray[i];
				}
				DEBUG(userAgent);
			}
			if(sentenceFromClient.charAt(0) == 'H') {
				vb_serverArray = sentenceFromClient.split("\\s");
				vb_server = vb_serverArray[1];
				DEBUG(vb_server);
			}
		}
//		DEBUG(cfgMap.get("vb_" + vb_server));
		WWW_ROOT = cfgMap.get("vb_" + vb_server);
		DEBUG(WWW_ROOT);

	    // process the request
	    String[] request = requestMessageLine.split("\\s");
		
	    if (request.length < 2 || !request[0].equals("GET"))
	    {
		    outputError(500, "Bad request");
		    return;
	    }

	    // parse URL to retrieve file name
	    urlName = request[1];
	    DEBUG(urlName);
		if (urlName.equals("/"))
			if (userAgent.contains("Phone") || userAgent.contains("phone")) {
				urlName = "index_m.html";
			}
			else urlName = "index.html";
	    else if ( urlName.startsWith("/") == true )
	       urlName  = urlName.substring(1);

	    // map to file name
	    fileName = WWW_ROOT + urlName;
		String valueTemp = "";
		if (fileName.contains(".cgi")) {
			// set env, command and output of .cgi
			int index = fileName.indexOf(".cgi");
			valueTemp = fileName.substring(index + 5);
			Map<String, String> env = new HashMap<>();
			env.put("QUERY_STRING", valueTemp);
			DEBUG(fileName.substring(0, fileName.indexOf(".cgi")+4));
			String result = runPB(fileName.substring(0, fileName.indexOf(".cgi")+4),WWW_ROOT, env);
//			System.out.println(result);
			String fileNameTemp = "./cgiOutput";
			Path path = Paths.get(fileNameTemp);
			try (BufferedWriter writer =
						 Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				writer.write(result);
			}
			fileInfo = new File("./cgiOutput");
			if (!fileInfo.isFile()) {
				outputError(404, "Not Found");
				fileInfo = null;
			}
			DEBUG("write");
			this.fileName = "./cgiOutput";
		}
		else {
			DEBUG("Map to File name: " + fileName);
			fileInfo = new File(fileName);
			if (!fileInfo.isFile()) {
				outputError(404, "Not Found");
				fileInfo = null;
			}
		}
    } // end mapURL2file

    private void outputResponseHeader() throws Exception 
    {
	    outToClient.writeBytes("HTTP/1.0 200 Document Follows\r\n");
	    outToClient.writeBytes("Set-Cookie: MyCool433Seq12345\r\n");

		// Time in the format of Java currentTimeMill
		// rfc1123-date = wkday "," SP date1 SP time SP "GMT"
		// date1        = 2DIGIT SP month SP 4DIGIT
		SimpleDateFormat formatter= new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date(System.currentTimeMillis());
//        System.out.println("Start time:" + formatter.format(date));
		Date date1 = formatter.parse("wed, 26 oct 2022 15:37:15 CST");
//        System.out.println("Start time:" + formatter.format(date1));
		outToClient.writeBytes("Date: "+formatter.format(date)+"\n");

		String htName =  InetAddress.getLocalHost().getHostName();
		outToClient.writeBytes("Server: "+htName+"\n");

	    if (urlName.endsWith(".jpg"))
	        outToClient.writeBytes("Content-Type: image/jpeg\r\n");
	    else if (urlName.endsWith(".gif"))
	        outToClient.writeBytes("Content-Type: image/gif\r\n");
	    else if (urlName.endsWith(".html") || urlName.endsWith(".htm"))
	        outToClient.writeBytes("Content-Type: text/html\r\n");
	    else
	        outToClient.writeBytes("Content-Type: text/plain\r\n");
    }

    private void outputResponseBody() throws Exception 
    {

	    int numOfBytes = (int) fileInfo.length();
	    outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
	    outToClient.writeBytes("\r\n");

		if(fileCache.containsKey(fileName)) {
			byte[] fileInBytes = new byte[numOfBytes];
			fileInBytes = fileCache.get(fileName).getBytes();
			outToClient.write(fileInBytes, 0, numOfBytes);
			DEBUG("fileCache:" + fileName);
		} else {
			// send file content
			DEBUG(fileName);
			FileInputStream fileStream  = new FileInputStream (fileName);

			byte[] fileInBytes = new byte[numOfBytes];
			fileStream.read(fileInBytes);
			// DEBUG(fileInBytes.toString());
			outToClient.write(fileInBytes, 0, numOfBytes);
			if(true) {
				fileCache.put(fileName, new String(fileInBytes));
			}
//			DEBUG(fileCache.get(fileName));
		}
    }

    void outputError(int errCode, String errMsg)
    {
	    try {
	        outToClient.writeBytes("HTTP/1.0 " + errCode + " " + errMsg + "\r\n");
	    } catch (Exception e) {}
    }

	public static String runPB(String command, String directory, Map<String, String> env) throws IOException {
		// .cgi
		ProcessBuilder processBuilder = new ProcessBuilder(
				command);
//        System.out.println(env.keySet());
		Set set = env.keySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()){
			Object key = iterator.next();
			Object value = env.get(key);
			processBuilder.environment().put((String) key, (String) value);
			System.out.println("env add: "+key+"===>"+value);
		}
		processBuilder.directory(new File(directory));
		Process process = processBuilder.start();
		System.out.print(printStream(process.getErrorStream()));
		String result = printStream(process.getInputStream());
		return result;
	}

	public static String printStream(InputStream is) throws IOException {
		// .cgi
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line, result = "";
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			result += line;
		}
		return result;
	}
    static void DEBUG(String s) 
    {
       if (_DEBUG)
          System.out.println( s );
    }
}
