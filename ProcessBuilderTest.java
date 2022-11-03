import jdk.nashorn.internal.runtime.Debug;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProcessBuilderTest {
    public static String runPB(String command, String directory, Map<String, String> env) throws IOException {
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
        processBuilder.environment().put("QUERY_STRING", "/appl/");
        processBuilder.directory(new File(directory));
        Process process = processBuilder.start();
//        OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
//        osw.write("rake routes");
//        osw.close();
        System.out.print(printStream(process.getErrorStream()));
        String result = printStream(process.getInputStream());
        return result;
    }

    public static String printStream(InputStream is) throws IOException {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line, result = "";
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            result += line;
        }
        return result;
    }
    public static void main(String[] args) throws IOException {
//        try {
//            Process process = Runtime.getRuntime().exec("./cgi/price.cgi");
//            InputStream is = process.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            int exitCode = process.waitFor();
//            System.out.println(exitCode);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }

//        ProcessBuilder pb = new ProcessBuilder(args);
//        String fileName = "./cgi/price.cgi?/appl/";
//        String valueTemp = "";
//        if (fileName.contains(".cgi")) {
//            int index = fileName.indexOf(".cgi");
//            valueTemp = fileName.substring(index + 5);
//            System.out.println(valueTemp);
//        }
        Map<String, String> env = new HashMap<>();
        env.put("QUERY_STRING", "/appl/");
        env.put("a","b");
        String result = runPB("./cgi/price.cgi","./", env);
        System.out.println(result);
    }
}
