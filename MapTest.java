import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapTest {
    public static void main(String[] args) throws IOException {
        Map<String, String> fileCache = new HashMap<>();
        String fileName = "index.html";
        File fileInfo = new File(fileName);
        int numOfBytes = (int) fileInfo.length();
        // send file content
        FileInputStream fileStream  = new FileInputStream (fileName);

        byte[] fileInBytes = new byte[numOfBytes];
        fileStream.read(fileInBytes);
        fileCache.put(fileName, new String(fileInBytes));
        System.out.println(fileCache.get(fileName));
    }
}
