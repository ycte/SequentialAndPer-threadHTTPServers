import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GETTest {
    public static String requestHttpGet(String strUrl) {

        try { //设置url
            URL url = new URL(strUrl);

            //获取HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置为get请求
            connection.setRequestMethod("GET");
            //设置连接主机超时时间
            connection.setConnectTimeout(5000);
            //设置从主机读取数据超时
            connection.setReadTimeout(5000);
            //获取请求码（来判断网络请求是否正确）
            int code = connection.getResponseCode();

            //判断请求是否成功
            if (code == HttpURLConnection.HTTP_OK) {
                //如果数据请求成功
                //就获取数据
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
                //拼接字符串
                StringBuilder builder = new StringBuilder();
                //把数据读取从成字符串
                String str = "";
                while ((str = reader.readLine()) != null) {
                    //把一行行数据拼接成一行数据
                    builder.append(str);
                }
                //返回拼接后的数据
                return builder.toString();

            }
            //关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(requestHttpGet("http://locolhost:6789"));
    }
}
