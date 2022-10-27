import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class FileTest {
    public static String cfgRead() throws IOException {
        String cfgFileName = "./httpd.conf";
        File cfgFileInfo = new File(cfgFileName);
        int numOfBytes = (int) cfgFileInfo.length();
        System.out.println("Cfg-Length: " + numOfBytes + "\r\n");
        FileInputStream fileStream  = new FileInputStream (cfgFileName);
        byte[] fileInBytes = new byte[numOfBytes];
        fileStream.read(fileInBytes);
        // System.out.println(new String(fileInBytes));
        return new String(fileInBytes);
    }
    public static HashMap<String, String> generateCfgMap(String cfgFileContent) {
        HashMap<String, String> res = new HashMap<String, String>();
        String[] temp0 = cfgFileContent.split("\\s");
        String[] temp1 = new String[10];
        for(int i = 0; i < temp0.length; i++) {
            // System.out.println(i + " " + temp0[i]);
            if(temp0[i].equals("<VirtualHost")) {
                i++;i++;
                for(int j = 0;; i++) {
                    if(temp0[i].equals("<VirtualHost>")) {
                        break;
                    }
                    if(!temp0[i].equals("")) {
                        temp1[j] = temp0[i];
                        // System.out.println(temp1[j]);
                        j++;
                    }
                }
                res.put("vb_" + temp1[3], temp1[1]);
                // System.out.println("vb_" + temp1[3] + ": " + res.get("vb_" + temp1[3]));
            }
            else if (!temp0[i].equals("")) {
                for(int j = 0;j < 2; i++) {
                    if(!temp0[i].equals("")) {
                        temp1[j] = temp0[i];
                        // System.out.println(temp1[j]);
                        j++;
                    }
                }
                i--;
                res.put(temp1[0], temp1[1]);
                // System.out.println("!!newCfg!!" + temp1[0] + ": " + res.get(temp1[0]));
            }
        }
        return res;
    }
    public static void main(String[] args) throws IOException {
        String cfgFileContent = cfgRead();
        HashMap<String, String> cfgMap = new HashMap<String, String>();
        cfgMap = generateCfgMap(cfgFileContent);
        System.out.println(cfgMap.toString());
    }
}
