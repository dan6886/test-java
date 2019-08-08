package time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Paper
 * @date 2018年1月2日 上午11:01:24
 */
public class CalendarUtils {

    public static Date str2Date(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(str.toString());
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String date2Str(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static List<Date> sortDate(List<Date> dates, boolean desc) {
        ComparatorDate c = new ComparatorDate();
        Collections.sort(dates, c);
        if (desc) {
            Collections.reverse(dates);
        }
        return dates;

    }

    public static List<String> sortDate(List<String> dateStrs, String format, boolean desc) {
        List<String> result = new ArrayList<>();
        List<Date> dates = new ArrayList<>();
        for (String dateStr : dateStrs) {
            Date date = str2Date(dateStr, format);
            if (date != null)
                dates.add(date);
        }
        ComparatorDate c = new ComparatorDate();
        Collections.sort(dates, c);
        if (desc) {
            Collections.reverse(dates);
        }

        for (Date date : dates) {
            result.add(date2Str(date, format));
        }

        return result;

    }

}

class ComparatorDate implements Comparator<Object> {

    public int compare(Object obj1, Object obj2) {
        Date begin = (Date) obj1;
        Date end = (Date) obj2;
        if (begin.after(end)) {
            return 1;
        } else {
            return -1;
        }
    }
}
