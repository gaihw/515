package winCoin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Login {
    public static void main(String[] args) {
        System.out.println(dateToStamp("2023-03-10 15:46:56")+(2*60+30)*60*1000);
        System.out.println(stampTodate(1678443416145l-(2*60+30)*60*1000));
    }
    public static String stampTodate(long l){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(l));
    }
    public static long dateToStamp(String s){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
