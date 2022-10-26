
/*
 *
 * Learn to use java.util.Timer
 *
 * Usage: none
 */
//import org.apache.commons.lang.time.*;
//import org.apache.commons.lang3.time.DateFormatUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimerTest {
    public static void main(String[] args) throws ParseException {
//        Timer timer = new Timer();

        // Time in the format of Java currentTimeMill
        // rfc1123-date = wkday "," SP date1 SP time SP "GMT"
        // date1        = 2DIGIT SP month SP 4DIGIT
        SimpleDateFormat formatter= new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        Date date = new Date(System.currentTimeMillis());
//        System.out.println("Start time:" + formatter.format(date));
        Date date1 = formatter.parse("wed, 26 oct 2022 15:37:15 CST");
//        System.out.println("Start time:" + formatter.format(date1));

//        final int[] i = {0};
//
//        TimerTask task = new TimerTask() {
//
//            @Override
//            public void run() {
//                i[0]++;
//
//                // Time in the format of Java currentTimeMill
//                // rfc1123-date = wkday "," SP date1 SP time SP "GMT"
//                // date1        = 2DIGIT SP month SP 4DIGIT
//                SimpleDateFormat formatter= new SimpleDateFormat("EE dd MMM yyyy HH:mm:ss GMT");
//                Date date = new Date(System.currentTimeMillis());
//                System.out.println("Start time:" + formatter.format(date));
//                System.out.println(formatter.format(this.scheduledExecutionTime()) + "  定时任务开始第" + i[0] + "次执行");
//
//                if (i[0] == 5) {
//                    System.out.println("定时任务结束执行，共执行" + i[0] + "次");
//                    /**
//                     * TimerTask类中的cancel()方法侧重的是将自身从任务队列中清除，其他任务不受影响
//                     * 而Timer类中的cancel()方法则是将任务队列中全部的任务清空
//                     */
//                    this.cancel();
//                }
//            }
//        };
//
//        /**
//         * task:要调度的任务
//         * delay:在执行任务之前，以毫秒为单位进行延迟的时间
//         * period:连续任务以毫秒为单位的时间间隔
//         */
//        timer.schedule(task, 0, 2*1000);
    }
}
