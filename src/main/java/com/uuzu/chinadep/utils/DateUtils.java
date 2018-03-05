package com.uuzu.chinadep.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH";
	
	public static final String FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
	
	public static SimpleDateFormat sdf1 = new SimpleDateFormat(FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS);
	
	public static final String FORMAT_DATE = "yyyyMMdd";
	
	public static SimpleDateFormat sdf2 = new SimpleDateFormat(FORMAT_DATE);
	
	public static final String FORMAT_DATETIME_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	public static SimpleDateFormat sdf3 = new SimpleDateFormat(FORMAT_DATETIME_YYYYMMDDHHMMSS);
	
	public static final String FORMAT_DATEMONTH= "yyyyMM";
	public static SimpleDateFormat sdf4 = new SimpleDateFormat(FORMAT_DATEMONTH);

	public static final String FORMAT_DATEHOURMINUTE= "yyyyMMdd/HH";

	public static SimpleDateFormat sdf5 = new SimpleDateFormat(FORMAT_DATEHOURMINUTE);

	/**
	 * 按指定格式来格式化日期
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date,String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 
	 * @param flag 日期类型（1：日；2：月）
	 * @param beginDate 开始时间
	 * @param endDate 结束时间 
	 * @return
	 * @throws ParseException 
	 */
	public static List<String> getDateList(int flag, String beginDate, String endDate){
		List<String> lDate = new ArrayList<String>();
		if(endDate == null || beginDate.equals(endDate)){
			lDate.add(beginDate);
		} else {
			int increment;
			String dateFormat = null;
			Date begin = null;
			Date end = null;
			if(flag == 1){
				increment = Calendar.DAY_OF_MONTH;
				dateFormat = "yyyyMMdd";
				try {
					begin = sdf2.parse(beginDate);
					end = sdf2.parse(endDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			} else {
				increment = Calendar.MONTH;
				dateFormat = "yyyyMM";
				try {
					begin =sdf4.parse(beginDate.substring(0, beginDate.length()-2));
					end = sdf4.parse(endDate.substring(0, beginDate.length()-2));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			lDate.add(beginDate);
			Calendar cal =Calendar.getInstance();
			cal.setTime(begin);
			boolean bContinue = true;        
			while (bContinue) {
				 // 根据日历的规则，为给定的日历字段添加或减去指定的时间量        
				 cal.add(increment, 1);            
				 // 测试此日期是否在指定日期之后           
				 if (end.after(cal.getTime())) {                
					 if(flag == 2){
						 lDate.add(format(cal.getTime(),dateFormat) + "00");
					 } else {
						 lDate.add(format(cal.getTime(),dateFormat));
					 }
				 } else {                
					 break;            
				 }        
			 } 
			// 把结束时间加入集合
			lDate.add(endDate);
			
		}
		return lDate;
	}

	/**
	 *
	 * @param date 日期（yyyyMMdd）
	 * @param day 相差天数
	 * @return
	 * @throws ParseException
	 */
	public static String getDateFormatByDay(String date, int day){
		int increment = Calendar.DAY_OF_MONTH;
		String dateFormat = "yyyyMMdd";
		Calendar cal =Calendar.getInstance();
		try {
			cal.setTime(sdf2.parse(date));
			cal.add(increment, day);
			return format(cal.getTime(),dateFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getWeekList(String beginDate, String endDate){
		List<String> lDate = new ArrayList<String>();
		Calendar cal =Calendar.getInstance();
		cal.setTime(parseDate(beginDate));
		lDate.add(cal.getWeekYear() + "00" + cal.get(Calendar.WEEK_OF_YEAR));
		while(true){
			cal.add(Calendar.DAY_OF_MONTH,7);
			if(parseDate(endDate).after(cal.getTime())){
				lDate.add(cal.getWeekYear() + "00" + cal.get(Calendar.WEEK_OF_YEAR));
			} else {
				break;
			}
		}
		return lDate;
	}

	/**
	 * 得到某年某周的第一天
	 *
	 * @param year
	 * @param week
	 * @return
	 */
	public static String getFirstDayOfWeek(int year, int week) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 1);
		Calendar cal = (Calendar) calendar.clone();
		cal.add(Calendar.DATE, week * 7);

		return formatDate(getFirstDayOfWeek(cal.getTime()));
	}

	/**
	 * 得到某年某周的最后一天
	 *
	 * @param year
	 * @param week
	 * @return
	 */
	public static String getLastDayOfWeek(int year, int week) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 1);
		Calendar cal = (Calendar) calendar.clone();
		cal.add(Calendar.DATE, week * 7);

		return formatDate(getLastDayOfWeek(cal.getTime()));
	}

	/**
	 * 取得当前日期所在周的第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK,
				calendar.getFirstDayOfWeek()); // Monday
		return calendar.getTime();
	}

	/***
	 * 获取某年某月最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar c=Calendar.getInstance();
		c.set(Calendar.YEAR,year);
		c.set(Calendar.MONTH,month-1);
		c.set(Calendar.DAY_OF_MONTH,1);
		c.add(Calendar.MONTH,1);
		c.add(Calendar.DAY_OF_MONTH,-1);
		return formatDate(c.getTime());
	}
	/**
	 * 取得当前日期所在周的最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK,
				calendar.getFirstDayOfWeek() + 6); // Sunday
		return calendar.getTime();
	}

	public static String formatDateTime3(Date date) {
		return sdf3.format(date);
	}
	
	public static Date parseDateTime(String str) {
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String formatDateTime(Date date) {
		return sdf.format(date);
	}
	public static String formatDate(Date date) {
		return sdf2.format(date);
	}
	public static Date parseDate(String str) {
		Date date = null;
		try {
			date = sdf2.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String formatDateMonth(Date date) {
		return sdf4.format(date);
	}
	public static Date parseDateMonth(String str) {
		Date date = null;
		try {
			date = sdf4.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static String formatDateTimeAll(Date date) {
		return sdf1.format(date);
	}
	public static Date parseDateTimeAll(String str) {
		Date date = null;
		try {
			date = sdf1.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static boolean compareCurrentDate(Date on, Date off) {
		SimpleDateFormat sdfint = new SimpleDateFormat("yyyyMMddHH");
		int current = Integer.parseInt(sdfint.format(new Date()));
		int online = Integer.parseInt(sdfint.format(on));
		int offline = Integer.parseInt(sdfint.format(off));
		if (current >= online && current <= offline) {
			return true;
		}
		return false;
	}
	
	public static int getHour(Date date) {
		SimpleDateFormat sdfhour = new SimpleDateFormat("HH");
		return Integer.parseInt(sdfhour.format(date));
	}

	/**
	 *
	 * @param date 日期（yyyyMMdd）
	 * @param day 相差天数
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateByDay(Date date, int day){
		Calendar cal =Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	public static String getDateBySecond(String se){
		Long ms =Long.parseLong(se);
		if(ms==null){
			ms=0L;
		}
		long msl=(long)ms*1000;
		String str=null;
		if(ms!=null){
			try {
				str=sdf5.format(msl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return str;
	}
}




