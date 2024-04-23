package client;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static String getCurrentTime() {
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化当前时间
        String formattedTime = currentTime.format(formatter);

        return formattedTime;
    }
}
