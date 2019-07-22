package com.openjava.datatag.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 处理时间工具
 * @author zmk
 *
 */
public class MyTimeUtil {

	/**
	 * 自动获取星期
	 * @param date
	 * @return
	 */
	public static String toWeekStr(Date date) {
		if(date==null) {
			return null;
		}
	    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        try {
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0) w = 0;
        return weekDays[w];
	}
	/**
	 * 获取时间段 0上午，1下午，2晚上
	 * @param date
	 * @return
	 */
	public static int getTimeSlot(Date date) {
		int  slot=0;
	    Calendar cal = Calendar.getInstance(); // 获得一个日历
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
//        int min = cal.get(Calendar.MINUTE);
//        int second = cal.get(Calendar.SECOND);
        if(hour>0 &&  hour<12 ) {
        	slot = 0;
        }else if(hour>=12 && hour<21) {
        	slot =1;
        }else {
        	slot = 3;
        }
		return slot;
	}


	/**
	 * 获取当前时间的这个星期的星期一Date[0]，星期日Date[1]
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static Date[] getWeekBeginAndLast(Date date) throws ParseException {
		if(date==null) {
			return null;
		}
		SimpleDateFormat fLast = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取周一的日期
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar. DAY_OF_WEEK, Calendar.MONDAY);
//        System.out.println(f.format(cal.getTime()));
        Date dateFirst = fLast.parse(f.format(cal.getTime()));
		//获取礼拜天的日期
		SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Long timeLong = cal.getTime().getTime()+1000*60*60*24*6;
        Date date2 = new Date(timeLong);
        cal.setTime(date2);
//        System.out.println(f2.format(cal.getTime()));
        Date dateLast = fLast.parse(f2.format(cal.getTime()));
        Date[] result = new Date[2];
        result[0] = dateFirst;
        result[1] = dateLast;
//        System.out.println(dateFirst);
//        System.out.println(dateLast);
		return result;
	}
	/**
	 * 获取单天的开始和结束时间
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	public static  Date[] getBeginAndEnd(Date date) throws Exception {
		if(date==null) {
			return null;
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取周一的日期
		SimpleDateFormat begin = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Date dateBegin = f.parse(begin.format(cal.getTime()));
        Date dateEnd = f.parse(end.format(cal.getTime()));
        Date[] result = new Date[2];
//        System.out.println(dateBegin);
//        System.out.println(dateEnd);
        result[0] = dateBegin;
        result[1] = dateEnd;
        return result;
	}
	
	/**
	 * 获取本年开始时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getCurrentYearStartTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH,0);
		return calendar.getTime();
	}

	/**
	 * 获取本年开始时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getCurrentYearEndTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.MONTH,12);
		return calendar.getTime();
	}

	public static String getWeekStr(Date Date){
		String  weekStr= "MON";
		Calendar cal = Calendar.getInstance();
		cal.setTime(Date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
		switch (dayOfWeek){
			case 0:
				weekStr = "SUN";
				break;
			case 1:
				weekStr = "MON";
				break;
			case 2:
				weekStr = "TUE";
				break;
			case 3:
				weekStr = "WED";
				break;
			case 4:
				weekStr = "THU";
				break;
			case 5:
				weekStr = "FRI";
				break;
			case 6:
				weekStr = "SAT";
				break;
			default:
				break;
		}
		return weekStr;
	}
	public static void main(String[] args) throws Exception {
		Date date = new Date();
		System.out.println(toWeekStr(date));
		Date date2 = new Date(date.getTime()+1000*60*60*24*0);
		getWeekBeginAndLast(date2);

		System.out.println(getTimeSlot(date));
		getBeginAndEnd(date);
		
	}
}
