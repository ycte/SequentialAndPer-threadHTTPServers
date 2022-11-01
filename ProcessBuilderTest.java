import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessBuilderTest {
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("java -jar ProcessJar.jar args1 agrs2 args3");
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println(exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
//————————————————
//        版权声明：本文为CSDN博主「朱小厮」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//        原文链接：https://blog.csdn.net/u013256816/article/details/54603910
    }
}
