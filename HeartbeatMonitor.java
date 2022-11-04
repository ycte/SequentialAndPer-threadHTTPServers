/*
 * Heartbeat monitor, a part of code comes from classmate kxy.
 *
 */

import java.net.Socket;
import java.util.List;

class HeartbeatMonitor extends Thread {
    Socket connSocket;
    private List<Socket> pool;

    public HeartbeatMonitor() {

    }

    public void Mnt(Socket connSocket) {
        if (connSocket != null)
            pool.add(connSocket);
    }

    public void run() {
        int T = 1000;
        double Threshold = 0.25;// limit 1ms less than 0.25 request
        while (true) {
            int reqCnt = BasicWebServer.reqCnt;
            long TimeSrcFlag = System.currentTimeMillis();
            try {
                while (System.currentTimeMillis() - TimeSrcFlag < T) {
                }
                if ((BasicWebServer.reqCnt - reqCnt) * 1.0 / T > Threshold) {
                    if (!pool.isEmpty()) {
                        connSocket = pool.remove(0);
                        WebRequestHandler wrh =
                                new WebRequestHandler( connSocket, BasicWebServer.cfgMap,
                                        BasicWebServer.fileCache, BasicWebServer.cacheSize);
                        wrh.outputError(503, "overloading");
                        connSocket.close();// close the thread of the first
                        System.out.println("Thread : " + connSocket + "has been closed!");
                    }
                }
                sleep(2000);
            } catch (Exception e) {
            }
        }
    }
}


/* in main
 * reqCnt++;
	// process a request
	WebRequestHandler wrh =
	new WebRequestHandler( connectionSocket, cfgMap, fileCache, cacheSize);
	cm.Mnt(connectionSocket);
	wrh.processRequest();
 */