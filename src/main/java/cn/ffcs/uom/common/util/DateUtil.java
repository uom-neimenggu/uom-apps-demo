package cn.ffcs.uom.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * =============================================================<br>
 * 版权： 福富软件 版权所有 (c) 2002 - 2003.<br>
 * 文件： com.ffsc.crm.common.date<br>
 * 所含类： DateUtil<br>
 * 编写人员：chenjun<br>
 * 创建日期：2004-11-04<br>
 * 功能说明：时间指的均为应用服务器时间，而不是数据库服务器时间<br>
 * 更新记录：<br>
 * 日期 作者 内容<br>
 * =============================================================<br>
 * 2004-11-04 chenjun 实现日期的基本功能<br>
 * 2004-10-25 chenjun 增加注释，改方法为静态方法
 * =============================================================<br>
 */
public class DateUtil {
	// StrUtil su = new StrUtil();

	private static Log log = LogFactory.getLog(DateUtil.class);

	private static String defaultDatePattern = null;

	private static String timePattern = "HH:mm";

	/**
	 * @author: wuq
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-17 wuq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	protected DateUtil() {
	}

	/**
	 * 将日期格式转化为字符串格式.
	 * 
	 * @param date
	 *            日期
	 * @return String 日期格式的字符串，如"2004-10-25"
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-11-4 chenjun 创建方法，并实现功能
	 *        ==============================================================<br>
	 */
	public static String dateToStr(final Date date) {
		final StringBuffer backstr = new StringBuffer();
		if (date == null) {
			backstr.append("");
		} else {
			final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
			final String dateStr = time.format(date);
			backstr.append(dateStr);
			/*
			 * ymd = DateFormat.getDateInstance().format(date); if (ymd != null
			 * && ymd.charAt(1) != '-') { String[] ymdArr = StrUtil.split(ymd,
			 * "-"); if (ymdArr.length >= 2) { String y = ymdArr[0]; String m =
			 * ymdArr[1]; if (m.length() == 1) { m = "0".concat(m); } String d =
			 * ymdArr[2]; if (d.length() == 1) { d = "0".concat(d); }
			 * backstr.append(y).append("-").append(m).append("-").append( d); }
			 * else { backstr.append(""); } } else { backstr.append(""); }
			 */
		}
		return backstr.toString();
	}

	/**
	 * 获取YYYYMMDD格式的日期字符串.
	 * 
	 * @param date
	 *            时间
	 * @return 时间格式YYYYMMDD
	 * @author: yejb
	 * @修改记录： ==============================================================<br>
	 *        日期:May 12, 2008 yejb 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getYYYYMMDD(Date date) {
		final StringBuffer backstr = new StringBuffer();

		if (date == null) {
			date = new Date();
		}

		if (date == null) {
			backstr.append("");
		} else {
			final SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");
			final String dateStr = time.format(date);
			backstr.append(dateStr);
			/*
			 * ymd = DateFormat.getDateInstance().format(date); if (ymd != null
			 * && ymd.charAt(1) != '-') { String[] ymdArr = StrUtil.split(ymd,
			 * "-"); if (ymdArr.length >= 2) { String y = ymdArr[0]; String m =
			 * ymdArr[1]; if (m.length() == 1) { m = "0".concat(m); } String d =
			 * ymdArr[2]; if (d.length() == 1) { d = "0".concat(d); }
			 * backstr.append(y).append("").append(m).append("").append(d); }
			 * else { backstr.append(""); } } else { backstr.append(""); }
			 */
		}
		return backstr.toString();
	}

	/**
	 * 将日期字符串转化为日期格式.
	 * 
	 * @param strn
	 *            默认日期格式的字符串，如"2004-10-25"
	 * @return Date
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-11-4 chenjun 创建方法，并实现功能
	 *        ==============================================================<br>
	 */
	public static Date strToDate(final String strn) {
		Date returnValue = null;

		if (strn != null && !strn.equals("")) {
			final DateFormat df = DateFormat.getDateInstance();
			try {
				returnValue = df.parse(strn);
			} catch (final ParseException pe) {
			}
		}
		return returnValue;
	}

	/**
	 * 将日期格式转化为具有[日期时间]的字符串格式.
	 * 
	 * @param date
	 *            日期
	 * @return String 日期格式的字符串，如"2004-10-25 14:16:02"
	 * @author: wuq
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-17 wuq 创建方法，并实现功能
	 *        ==============================================================<br>
	 */

	public static String dateTimeToStr(final Date date) {
		String lpDateStr, lpTimeStr;
		lpDateStr = DateUtil.getShortDateStr(date);
		lpTimeStr = DateUtil.getShortTimeStr(date);

		return lpDateStr + " " + lpTimeStr;
	}

	/**
	 * 将[日期时间]字符串转化为具有[日期时间]的日期格式.
	 * 
	 * @param strn
	 *            日期格式的字符串，如"2004-10-25 14:16:02"
	 * @return Date 日期
	 * @author: wuq
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-17 wuq 创建方法，并实现功能
	 *        ==============================================================<br>
	 */

	public static Date strToDateTime(String strn) {
		if (strn != null && strn.length() == 8) {
			String temp = strn.substring(0, 4);
			temp += "-";
			temp += strn.substring(4, 6);
			temp += "-";
			temp += strn.substring(6, 8);
			strn = temp;
		}
		if (strn != null && strn.length() == 10) {
			strn = strn + " 00:00:00";
		}
		Date returnValue = null;
		if (strn != null) {
			final DateFormat df = DateFormat.getDateTimeInstance();
			try {
				returnValue = df.parse(strn);
			} catch (final ParseException pe) {
			}
		}
		return returnValue;
	}

	/**
	 * 返回经计算修改后的时间， 举例：如现在时间为2004-10-14 那么dateAdd("d",2,new
	 * Date())返回的时间为2004-10-16 2004-11-4 chenjun 修正月份相减时(12月份、月份天数不一致时)问题.<br>
	 * 
	 * @param timeinterval
	 *            时间单位(年，月，日..)，取值分别为("y","m","d")
	 * @param number
	 *            时间间隔值
	 * @param sd
	 *            时间始点
	 * @return Date 日期
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-11-04 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Date dateAdd(final String timeinterval, final int number,
			final Date sd) {
		Date returnDate = null;
		int lpYear = 0, lpMonth = 0, lpDay = 0;
		// int myMonth;
		// int ret;
		String dateStr, lpStrDate = "";
		Date lpsd = null, lped = null;

		lpYear = Integer.parseInt(DateUtil.getYear(DateUtil.dateToStr(sd)));
		lpMonth = Integer.parseInt(DateUtil.getMonth(DateUtil.dateToStr(sd)));
		lpDay = Integer.parseInt(DateUtil.getDay(DateUtil.dateToStr(sd)));

		if (timeinterval.toLowerCase().equals("y")) {
			lpYear += number;
		} else if (timeinterval.toLowerCase().equals("m")) {
			if (number == 0) {
				return sd;
			}
			lpMonth += number;
			int internal = 0;
			if (number > 0) {
				lpStrDate = StrUtil.strnull(lpYear) + "-"
						+ StrUtil.strnull(lpMonth) + "-" + "1";
				lpsd = DateUtil.strToDate(lpStrDate);
				lpStrDate = StrUtil.strnull(lpYear) + "-"
						+ StrUtil.strnull(lpMonth + 1) + "-" + "1";
				lped = DateUtil.strToDate(lpStrDate);
				internal = DateUtil.dateDiff("d", lpsd, lped);
			} else if (number < 0) {
				lpStrDate = StrUtil.strnull(lpYear) + "-"
						+ StrUtil.strnull(lpMonth) + "-" + "1";
				lpsd = DateUtil.strToDate(lpStrDate);
				lpStrDate = DateUtil.getYear(sd) + "-" + DateUtil.getMonth(sd)
						+ "-" + "1";
				lped = DateUtil.strToDate(lpStrDate);
				internal = DateUtil.dateDiff("d", lpsd, lped);
			}
			if (lpDay > internal) {
				lpDay = internal;
			}
		} else if (timeinterval.toLowerCase().equals("d")) {
			lpDay += number;
		}
		dateStr = String.valueOf(lpYear) + "-" + String.valueOf(lpMonth) + "-"
				+ String.valueOf(lpDay);
		returnDate = DateUtil.strToDate(dateStr);
		return returnDate;
	}

	/**
	 * 返回经计算修改后的时间，举例：如现在时间为2004-10-14 00:00:00 那么dateAdd("h",2,new Date())<br>
	 * 返回的时间为2004-10-14 02:00:00.
	 * 
	 * @param timeinterval
	 *            时间单位(年，月，日，时，分，秒..)，取值分别为("y","m","d","h","f","s")
	 * @param number
	 *            时间间隔值
	 * @param sd
	 *            时间始点
	 * @return Date
	 * @author: yejb
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-4 yejb 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */

	public static Date dateTimeAdd(final String timeinterval, final int number,
			final Date sd) {
		Date returnDate = null;
		int lpYear = 0, lpMonth = 0, lpDay = 0, lpHour = 0, lpMinutes = 0, lpSeconds = 0;
		String dateStr, lpStrDate = "";
		Date lpsd = null, lped = null;

		lpYear = Integer.parseInt(DateUtil.getYear(DateUtil.dateToStr(sd)));
		lpMonth = Integer.parseInt(DateUtil.getMonth(DateUtil.dateToStr(sd)));
		lpDay = Integer.parseInt(DateUtil.getDay(DateUtil.dateToStr(sd)));
		lpHour = Integer.parseInt(DateUtil.getHour(DateUtil.dateTimeToStr(sd)));
		lpMinutes = Integer.parseInt(DateUtil.getMinutes(DateUtil
				.dateTimeToStr(sd)));
		lpSeconds = Integer.parseInt(DateUtil.getSeconds(DateUtil
				.dateTimeToStr(sd)));

		if (timeinterval.toLowerCase().equals("y")) {
			lpYear += number;
		} else if (timeinterval.toLowerCase().equals("m")) {
			if (number == 0) {
				return sd;
			}
			lpMonth += number;
			int internal = 0;
			if (number > 0) {
				lpStrDate = StrUtil.strnull(lpYear) + "-"
						+ StrUtil.strnull(lpMonth) + "-" + "1";
				lpsd = DateUtil.strToDate(lpStrDate);
				lpStrDate = StrUtil.strnull(lpYear) + "-"
						+ StrUtil.strnull(lpMonth + 1) + "-" + "1";
				lped = DateUtil.strToDate(lpStrDate);
				internal = DateUtil.dateDiff("d", lpsd, lped);
			} else if (number < 0) {
				lpStrDate = StrUtil.strnull(lpYear) + "-"
						+ StrUtil.strnull(lpMonth) + "-" + "1";
				lpsd = DateUtil.strToDate(lpStrDate);
				lpStrDate = DateUtil.getYear(sd) + "-" + DateUtil.getMonth(sd)
						+ "-" + "1";
				lped = DateUtil.strToDate(lpStrDate);
				internal = DateUtil.dateDiff("d", lpsd, lped);
			}
			if (lpDay > internal) {
				lpDay = internal;
			}
		} else if (timeinterval.toLowerCase().equals("d")) {
			lpDay += number;
		} else if (timeinterval.toLowerCase().equals("h")) {
			lpHour += number;
		} else if (timeinterval.toLowerCase().equals("f")) {
			lpMinutes += number;
		} else if (timeinterval.toLowerCase().equals("s")) {
			lpSeconds += number;
		}
		dateStr = String.valueOf(lpYear) + "-" + String.valueOf(lpMonth) + "-"
				+ String.valueOf(lpDay) + " " + String.valueOf(lpHour) + ":"
				+ String.valueOf(lpMinutes) + ":" + String.valueOf(lpSeconds);
		returnDate = DateUtil.strToDateTime(dateStr);
		return returnDate;
	}

	/**
	 * 返回两个日期之间的时间间隔.
	 * 
	 * @param timeinterval
	 *            String
	 * @param sd
	 *            Date 原始日期
	 * @param ed
	 *            Date 目标日期
	 * @return int 返回目标日期减去原始日期的值，如果sd为"2004-10-10 8:00:00",ed为"2004-10-9
	 *         23:00:00" timeinterval="d",返回为0,就是间隔的天数为0
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现功能
	 *        ==============================================================<br>
	 */
	public static int dateDiff(final String timeinterval, final Date sd,
			final Date ed) {
		int returnValue = 0;

		if (timeinterval.toLowerCase().equals("m")) {
			final int lpyear = Integer.parseInt(DateUtil.getYear(DateUtil
					.dateToStr(ed)))
					- Integer
							.parseInt(DateUtil.getYear(DateUtil.dateToStr(sd)));
			if (lpyear >= 0) {
				final int ret = lpyear * 12;
				final int edmomth = ret
						+ Integer.parseInt(DateUtil.getMonth(DateUtil
								.dateToStr(ed)));
				returnValue = edmomth
						- Integer.parseInt(DateUtil.getMonth(DateUtil
								.dateToStr(sd)));
			}
		} else if (timeinterval.toLowerCase().equals("d")) {
			returnValue = Integer.parseInt(String.valueOf((ed.getTime() - sd
					.getTime()) / (3600 * 24 * 1000)));
		} else if (timeinterval.toLowerCase().equals("s")) { // added by panchh
			returnValue = Integer.parseInt(String.valueOf((ed.getTime() - sd
					.getTime()) / 1000));
		}
		return returnValue;
	}

	public static String dateDiffToString(final Date sd, final Date ed) {
		String returnStr = "";

		int iHour = 0;
		int iMinute = 0;

		iHour = Integer.parseInt(String.valueOf((ed.getTime() - sd.getTime())
				/ (1000 * 3600)));

		iMinute = Integer
				.parseInt(String.valueOf(((ed.getTime() - sd.getTime())
						/ (1000 * 60) - iHour * 60)));

		returnStr = Math.abs(iHour) + "小时" + Math.abs(iMinute) + "分";

		return returnStr;
	}

	/**
	 * 计算两个时间的时间差.
	 * 
	 * @param toDate
	 *            String 格式如“2004-10-14”
	 * @param fromDate
	 *            String 格式如“2004-10-14”
	 * @return int 差的天数 如果toDate为"2004-10-10 8:00:00",fromDate为"2004-10-9
	 *         23:00:00" 返回为1,就是间隔的天数为1
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现功能
	 *        ==============================================================<br>
	 */
	public static int dateDiffDays(final String toDate, final String fromDate) {
		int elapsed = 0;
		GregorianCalendar gc1, gc2;
		GregorianCalendar g1, g2;
		g1 = new GregorianCalendar(Integer.parseInt(toDate.substring(0, 4)),
				Integer.parseInt(toDate.substring(5, 7)) - 1,
				Integer.parseInt(toDate.substring(8)));
		g2 = new GregorianCalendar(Integer.parseInt(fromDate.substring(0, 4)),
				Integer.parseInt(fromDate.substring(5, 7)) - 1,
				Integer.parseInt(fromDate.substring(8)));

		if (g2.after(g1)) {
			gc2 = (GregorianCalendar) g2.clone();
			gc1 = (GregorianCalendar) g1.clone();
		} else {
			gc2 = (GregorianCalendar) g1.clone();
			gc1 = (GregorianCalendar) g2.clone();
		}

		gc1.clear(Calendar.MILLISECOND);
		gc1.clear(Calendar.SECOND);
		gc1.clear(Calendar.MINUTE);
		gc1.clear(Calendar.HOUR_OF_DAY);

		gc2.clear(Calendar.MILLISECOND);
		gc2.clear(Calendar.SECOND);
		gc2.clear(Calendar.MINUTE);
		gc2.clear(Calendar.HOUR_OF_DAY);

		while (gc1.before(gc2)) {
			gc1.add(Calendar.DATE, 1);
			elapsed++;
		}
		return elapsed;
	}

	/**
	 * 计算两个时间的时间差.
	 * 
	 * @param toDate
	 *            String 格式如“2004-10-14”
	 * @param fromDate
	 *            String 格式如“2004-10-14”
	 * @return int 差的月份数
	 * @author: wuq
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static int dateDiffMonths(final String toDate, final String fromDate) {
		int elapsed = 0;
		GregorianCalendar gc1, gc2;
		GregorianCalendar g1, g2;
		g1 = new GregorianCalendar(Integer.parseInt(toDate.substring(0, 4)),
				Integer.parseInt(toDate.substring(5, 7)) - 1,
				Integer.parseInt(toDate.substring(8)));
		g2 = new GregorianCalendar(Integer.parseInt(fromDate.substring(0, 4)),
				Integer.parseInt(fromDate.substring(5, 7)) - 1,
				Integer.parseInt(fromDate.substring(8)));

		if (g2.after(g1)) {
			gc2 = (GregorianCalendar) g2.clone();
			gc1 = (GregorianCalendar) g1.clone();
		} else {
			gc2 = (GregorianCalendar) g1.clone();
			gc1 = (GregorianCalendar) g2.clone();
		}

		gc1.clear(Calendar.MILLISECOND);
		gc1.clear(Calendar.SECOND);
		gc1.clear(Calendar.MINUTE);
		gc1.clear(Calendar.HOUR_OF_DAY);
		gc1.clear(Calendar.DATE);

		gc2.clear(Calendar.MILLISECOND);
		gc2.clear(Calendar.SECOND);
		gc2.clear(Calendar.MINUTE);
		gc2.clear(Calendar.HOUR_OF_DAY);
		gc2.clear(Calendar.DATE);

		while (gc1.before(gc2)) {
			gc1.add(Calendar.MONTH, 1);
			elapsed++;
		}
		return elapsed;
	}

	/**
	 * 取得当前日期.
	 * 
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getYMD() {
		final Date myDate = new Date();
		return DateUtil.dateToStr(myDate);
	}

	/**
	 * 取得当前日期(精确).
	 * 
	 * @return Date
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 wuyx 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Date getSaveDate() {
		return new Date();
	}

	/**
	 * 取得当前日期.
	 * 
	 * @return Date
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Date getNowDate() {

		return new Date();
	}

	/**
	 * 取得当前日期.
	 * 
	 * @return Timestamp
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static java.sql.Timestamp getNowSqlDateTime() {
		final long nCurrentTime = System.currentTimeMillis();
		final java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(
				nCurrentTime);
		return sqlTimestamp;
	}

	/**
	 * 取得当前数据库使用的日期.
	 * 
	 * @return Date
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static java.sql.Date getNowSqlDate() {
		final long nCurrentTime = System.currentTimeMillis();
		final java.sql.Date sqlDate = new java.sql.Date(nCurrentTime);
		return sqlDate;
	}

	/**
	 * 取得当前日期.
	 * 
	 * @return Time
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static java.sql.Time getNowSqlTime() {
		final long nCurrentTime = System.currentTimeMillis();
		final java.util.Date utilDate = new java.util.Date(nCurrentTime);
		final java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime());
		return sqlTime;
	}

	/**
	 * 取得当前日期.
	 * 
	 * @return Date
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Date getNewDate() {
		return new Date();
	}

	/**
	 * 使用短日期格式显示日期,举例：当前时间是2004-9-1 DateFormat.getDateInstance().format(new
	 * Date())返回"2004-9-1" 而getShortDateStr(new Date())返回"2004-09-01".
	 * 
	 * @param date
	 *            Date
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getShortDateStr(final Date date) {
		final StringBuffer backstr = new StringBuffer();
		if (date == null) {
			backstr.append("");
		} else {
			final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
			final String dateStr = time.format(date);
			backstr.append(dateStr);
			/*
			 * ymd = DateFormat.getDateInstance().format(date); if (ymd != null
			 * && ymd.charAt(1) != '-') { String[] ymdArr = StrUtil.split(ymd,
			 * "-"); if (ymdArr.length >= 2) { String y = ymdArr[0]; String m =
			 * ymdArr[1]; if (m.length() == 1) { m = "0".concat(m); } String d =
			 * ymdArr[2]; if (d.length() == 1) { d = "0".concat(d); }
			 * backstr.append(y).append("-").append(m).append("-").append( d); }
			 * else { backstr.append(""); } } else { backstr.append(""); }
			 */
		}
		return backstr.toString();
	}

	/**
	 * 使用短时间格式显示时间,举例： 当前时间是8:09:08(早上8点9分5秒)
	 * DateFormat.getTimeInstance().format(new Date())返回"8:09:08"
	 * 而getShortTimeStr(new Date())返回"08:09:05".
	 * 
	 * @param date
	 *            日期
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getShortTimeStr(final Date date) {
		final StringBuffer backstr = new StringBuffer();

		if (date == null) {
			backstr.append("");
		} else {
			final String hms = DateFormat.getTimeInstance().format(date);
			final String[] hmsArr = StrUtil.split(hms, ":");
			if (hmsArr != null && hmsArr.length > 0 && hmsArr.length == 3) {
				String h = hmsArr[0];
				String m = hmsArr[1];
				String s = hmsArr[2];
				if (h.length() == 1) {
					h = "0".concat(h);
				}
				if (m.length() == 1) {
					m = "0".concat(m);
				}
				if (s.length() == 1) {
					s = "0".concat(s);
				}
				backstr.append(h).append(":").append(m).append(":").append(s);
			}
		}
		return backstr.toString();
	}

	/**
	 * 对于指定的年、月、日，返回日期格式的字符串.
	 * 
	 * @param lpYear
	 *            String
	 * @param lpMonth
	 *            String
	 * @param lpDay
	 *            String
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String dateStrSerial(final String lpYear, String lpMonth,
			String lpDay) {
		String lpDateStr = "";

		if (!StrUtil.strnull(lpYear).equals("")
				&& !StrUtil.strnull(lpMonth).equals("")
				&& !StrUtil.strnull(lpDay).equals("")) {
			if (lpMonth.length() == 1) {
				lpMonth = "0" + lpMonth;
			}
			if (lpDay.length() == 1) {
				lpDay = "0" + lpDay;
			}
			lpDateStr = lpYear + "-" + lpMonth + "-" + lpDay;
		}
		return lpDateStr;
	}

	/**
	 * 对于指定的年、月、日，返回Date子类型的日期.
	 * 
	 * @param lpYear
	 *            String
	 * @param lpMonth
	 *            String
	 * @param lpDay
	 *            String
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期::2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */

	public static Date dateSerial(final String lpYear, final String lpMonth,
			final String lpDay) {
		String lpDateStr = "";
		lpDateStr = DateUtil.dateStrSerial(lpYear, lpMonth, lpDay);
		return DateUtil.strToDate(lpDateStr);
	}

	/**
	 * @param date
	 *            Date
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */

	public static String getYear(final Date date) {
		String ymd = "";
		final StringBuffer backstr = new StringBuffer();

		if (date == null) {
			backstr.append("");
		} else {
			final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
			ymd = time.format(date);
			// ymd = DateFormat.getDateInstance().format(date);
			if (ymd != null && ymd.charAt(1) != '-') {
				final String[] ymdArr = StrUtil.split(ymd, "-");
				if (ymdArr.length >= 2) {
					final String y = ymdArr[0];
					backstr.append(y);
				} else {
					backstr.append("");
				}
			} else {
				backstr.append("");
			}
		}
		return backstr.toString();
	}

	/**
	 * 根据字符串型入参，取得日期的年份值.
	 * 
	 * @param ymd
	 *            日期字符串如“2004-05-01”
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getYear(final String ymd) {
		final StringBuffer backstr = new StringBuffer();
		if (ymd != null && ymd.charAt(1) != '-' && ymd.length() >= 4) {
			final String y = ymd.substring(0, 4);
			backstr.append(y);
		} else {
			backstr.append("");
		}
		return backstr.toString();
	}

	/**
	 * 根据日期型入参，取得日期的月份值，如果日期为9月，返回为"9"，而不是"09",其他小于9月的月份同理.
	 * 
	 * @param date
	 *            Date
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getMonth(final Date date) {
		String ymd = "";
		final StringBuffer backstr = new StringBuffer();
		if (date == null) {
			backstr.append("");
		} else {
			// ymd = DateFormat.getDateInstance().format(date);
			final SimpleDateFormat time = new SimpleDateFormat("yyyy-M-d");
			ymd = time.format(date);
			if (ymd != null && ymd.charAt(1) != '-') {
				final String[] ymdArr = StrUtil.split(ymd, "-");
				if (ymdArr.length >= 2) {
					final String m = ymdArr[1];
					backstr.append(m);
				} else {
					backstr.append("");
				}
			} else {
				backstr.append("");
			}
		}
		return backstr.toString();
	}

	/**
	 * 根据字符串型入参，取得日期的月份值,如果日期为9月，返回为"09"，而不是"9",其他小于9月的月份同理.
	 * 
	 * @param ymd
	 *            日期字符串如“2004-05-01”
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getMonth(final String ymd) {
		final StringBuffer backstr = new StringBuffer();

		if (ymd != null && ymd.charAt(1) != '-' && ymd.length() >= 6) {
			final String m = ymd.substring(5, 7);
			backstr.append(m);
		} else {
			backstr.append("");
		}
		return backstr.toString();
	}

	/**
	 * 根据日期型入参，取得日期的日值，如果日期为9号，返回为"9"，而不是"09",其他小于9号的日值同理.
	 * 
	 * @param date
	 *            Date
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getDay(final Date date) {
		String ymd = "";
		final StringBuffer backstr = new StringBuffer();

		if (date == null) {
			backstr.append("");
		} else {
			// ymd = DateFormat.getDateInstance().format(date);
			final SimpleDateFormat time = new SimpleDateFormat("yyyy-M-d");
			ymd = time.format(date);
			if (ymd != null && ymd.charAt(1) != '-') {
				final String[] ymdArr = StrUtil.split(ymd, "-");
				if (ymdArr.length >= 2) {
					final String d = ymdArr[2];
					backstr.append(d);
				} else {
					backstr.append("");
				}
			} else {
				backstr.append("");
			}
		}
		return backstr.toString();
	}

	/**
	 * 根据字符串入参，取得日期的日值，如果日期为9号，返回为"09"，而不是"9",其他小于9号的日值同理.
	 * 
	 * @param ymd
	 *            日期字符串如“2004-05-01”
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getDay(final String ymd) {
		final StringBuffer backstr = new StringBuffer();
		if (ymd != null && ymd.charAt(1) != '-' && ymd.length() >= 10) {
			final String d = ymd.substring(8, 10);
			backstr.append(d);
		} else {
			backstr.append("");
		}
		return backstr.toString();
	}

	/**
	 * 根据字符串入参,取得小时的值.
	 * 
	 * @param str
	 *            yyyy-mm-dd hh24:mi:ss 如:"2008-08-08 08:08:08"
	 * @return String
	 * @author: yejb
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-4 yejb 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getHour(final String str) {
		final StringBuffer backstr = new StringBuffer();
		if (str != null && str.charAt(1) != '-' && str.length() >= 13) {
			final String d = str.substring(11, 13);
			backstr.append(d);
		} else {
			backstr.append("");
		}
		return backstr.toString();
	}

	/**
	 * 根据字符串入参,取得秒的值.
	 * 
	 * @param str
	 *            yyyy-mm-dd hh24:mi:ss 如:"2008-08-08 08:08:08"
	 * @return String
	 * @author: yejb
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-4 yejb 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getSeconds(final String str) {
		final StringBuffer backstr = new StringBuffer();
		if (str != null && str.charAt(1) != '-' && str.length() >= 19) {
			final String d = str.substring(17, 19);
			backstr.append(d);
		} else {
			backstr.append("");
		}
		return backstr.toString();
	}

	/**
	 * 根据字符串入参,取得分的值.
	 * 
	 * @param str
	 *            String
	 * @return String
	 * @author: yejb
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-4 yejb 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getMinutes(final String str) {
		final StringBuffer backstr = new StringBuffer();
		if (str != null && str.charAt(1) != '-' && str.length() >= 16) {
			final String d = str.substring(14, 16);
			backstr.append(d);
		} else {
			backstr.append("");
		}
		return backstr.toString();
	}

	/**
	 * 返回代表一星期中某天的整数.
	 * 
	 * @param date
	 *            Date
	 * @return 返回"0,1,2,3,4,5,6"分别代表Sun,Mon,.....
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static int weekDay(final Date date) {
		int ret = 0;
		String strDate, subStr;

		strDate = date.toString();
		subStr = strDate.substring(0, 3);
		if (subStr.equals("Sun")) {
			ret = 0;
		} else if (subStr.equals("Mon")) {
			ret = 1;
		} else if (subStr.equals("Tue")) {
			ret = 2;
		} else if (subStr.equals("Wed")) {
			ret = 3;
		} else if (subStr.equals("Thu")) {
			ret = 4;
		} else if (subStr.equals("Fri")) {
			ret = 5;
		} else if (subStr.equals("Sat")) {
			ret = 6;
		}
		return ret;
	}

	/**
	 * 取得星期中指定的某一天的中文名.
	 * 
	 * @param date
	 *            Date
	 * @return String 返回一个字符串
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String weekDayName(final Date date) {
		String lpReturn = "";
		String strDate, subStr;

		strDate = date.toString();
		subStr = strDate.substring(0, 3);
		if (subStr.equals("Sun")) {
			lpReturn = "星期日";
		} else if (subStr.equals("Mon")) {
			lpReturn = "星期一";
		} else if (subStr.equals("Tue")) {
			lpReturn = "星期二";
		} else if (subStr.equals("Wed")) {
			lpReturn = "星期三";
		} else if (subStr.equals("Thu")) {
			lpReturn = "星期四";
		} else if (subStr.equals("Fri")) {
			lpReturn = "星期五";
		} else if (subStr.equals("Sat")) {
			lpReturn = "星期六";
		}
		return lpReturn;
	}

	/**
	 * 取得星期中指定的某一天的中文名.
	 * 
	 * @param ymd
	 *            String
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-10-08 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String weekDayName(final String ymd) {
		Date lpDate;
		lpDate = DateUtil.strToDate(ymd);
		return DateUtil.weekDayName(lpDate);
	}

	/**
	 * 将标准的时间字符串转化为定制的时间字符串(20:04:12 --> 200412).
	 * 
	 * @param lpTimeStr
	 *            String
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-02-23 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getStandardTimeToCustomTimeStr(final String lpTimeStr) {
		String lpReturnValue = "";
		// final StrUtil su = new StrUtil();
		if (!StrUtil.strnull(lpTimeStr).equals("")) {
			final String[] timeArr = StrUtil.split(lpTimeStr, ":");
			if (timeArr != null && timeArr.length == 3) {
				String lpHour = timeArr[0];
				if (lpHour.length() == 1) {
					lpHour = "0" + lpHour;
				}
				String lpMinute = timeArr[1];
				if (lpMinute.length() == 1) {
					lpMinute = "0" + lpMinute;
				}
				String lpSecond = timeArr[2];
				if (lpSecond.length() == 1) {
					lpSecond = "0" + lpSecond;
				}
				lpReturnValue = lpHour + lpMinute + lpSecond;
			}
		}
		return lpReturnValue;
	}

	/**
	 * 将定制的时间字符串转化为标准的时间字符串(200412 --> 20:04:12).
	 * 
	 * @param lpTimeStr
	 *            定制的时间串
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-02-23 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getCustomTimeToStandardTimeStr(final String lpTimeStr) {
		String lpReturnValue = "";
		if (!StrUtil.strnull(lpTimeStr).equals("") && lpTimeStr.length() == 6) {
			lpReturnValue = lpTimeStr.substring(0, 2) + ":"
					+ lpTimeStr.substring(2, 4) + ":"
					+ lpTimeStr.substring(4, 6);
		}
		return lpReturnValue;
	}

	/**
	 * 将标准的日期字符串转化为定制的日期字符串(如:2004-10-16 --> 20031016 ).
	 * 
	 * @param strYMD
	 *            String
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-1-6 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getStandardDateStrToCustomDateStr(final String strYMD) {
		String lpReturnValue = "";
		// final StrUtil su = new StrUtil();

		if (strYMD == null) {
			lpReturnValue = "";
		} else {
			final String[] ymdArr = StrUtil.split(strYMD, "-");
			if (ymdArr != null && ymdArr.length == 3) {
				final String lpYear = ymdArr[0];
				String lpMonth = ymdArr[1];
				if (lpMonth.length() == 1) {
					lpMonth = "0" + lpMonth;
				}
				String lpDay = ymdArr[2];
				if (lpDay.length() == 1) {
					lpDay = "0" + lpDay;
				}
				lpReturnValue = lpYear + lpMonth + lpDay;
			} else {
				lpReturnValue = strYMD;
			}
		}
		return lpReturnValue;
	}

	/**
	 * 将个性的日期字符串转化为标准的日期字符串(如:20031016 --> 2004-10-16).
	 * 
	 * @param strYMD
	 *            String
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-1-6 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String geCustomDateStrtToStandardDateStr(final String strYMD) {
		String lpReturnValue = "";

		if (strYMD == null) {
			lpReturnValue = "";
		} else {
			if (strYMD.length() == 8) {
				lpReturnValue = strYMD.substring(0, 4) + "-"
						+ strYMD.substring(4, 6) + "-" + strYMD.substring(6, 8);
			} else {
				lpReturnValue = strYMD;
			}
		}
		return lpReturnValue;
	}

	/**
	 * 将一个java.util.Date转化为java.sql.Date.
	 * 
	 * @param aDate
	 *            Date
	 * @return Date
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-1-6 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static java.sql.Date util2sql(final java.util.Date aDate) {
		if (aDate == null) {
			return null;
		}
		return new java.sql.Date(aDate.getTime());
	}

	/**
	 * 将一个java.sql.Date转化为一个java.util.Date.
	 * 
	 * @param aDate
	 *            Date
	 * @return Date
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-1-6 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static java.util.Date sql2util(final java.sql.Date aDate) {
		if (aDate == null) {
			return null;
		}
		return new java.util.Date(aDate.getTime());
	}

	/**
	 * 静态方法：根据参数 ordate 取得 ogdate 所在月份的第一个日期； 如：getMonthFirstDate("200308") =
	 * "20030801"。
	 * 
	 * @param ogdate
	 *            日期的字符形式，必须是六位（年月）或者八位（年月日）数字； 如："200308"（年月）或者 20030804（年月日）。
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-1-6 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getMonthFirstDate(String ogdate) {
		if (ogdate.length() == 6) {
			ogdate = ogdate + "01";
		} else {
			// 把ogdate变成前6位加01的串，如："20030805" --> "20030801"
			ogdate = ogdate.substring(0, 6) + "01";
		}

		return ogdate;
	}

	/**
	 * 静态方法：根据参数 ordate 取得 ogdate 所在月份的最后一个日期； 如：getMonthLastDate("200308") =
	 * "20030831"。
	 * 
	 * @param ogdate
	 *            日期的字符形式，必须是六位（年月）或者八位（年月日）数字； 如："200308"（年月）或者 20030804（年月日）。
	 * @return String 日期的字符（八位数字）形式，如："20030831"。
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-1-6 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getMonthLastDate(String ogdate) {
		if (ogdate.length() == 6) {
			ogdate = ogdate + "01";
		} else {
			// 把ogdate变成前6位加01的串，如："20030805" --> "20030801"
			ogdate = ogdate.substring(0, 6) + "01";
		}
		ogdate = DateUtil.getNextDateByMonth(ogdate, 1);
		ogdate = DateUtil.getNextDateByNum(ogdate, -1);

		return ogdate;
	}

	/**
	 * 静态方法：得到在参数起始日期 s 上加 i 天以后的日期（八位数字字符形式）； 如：getNextDateByNum("20030805", 4)
	 * = "20030809"。
	 * 
	 * @param s
	 *            起始日期，字符形式（八位数字），如："20030805"。
	 * @param i
	 *            天数（整型），i 可以为负数。
	 * @return 日期字符形式（八位数字），如："20030809"。
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-1-6 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getNextDateByNum(String s, final int i) {
		final SimpleDateFormat simpledateformat = new SimpleDateFormat(
				"yyyyMMdd");
		java.util.Date date = simpledateformat.parse(s, new ParsePosition(0));
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, i);
		date = calendar.getTime();
		s = simpledateformat.format(date);
		return s;
	}

	/**
	 * 静态方法：得到在参数起始日期 s 上加 i 月以后的日期（八位数字字符形式）； 如：getNextDateByNum("20030805", 2)
	 * = "20031005"。
	 * 
	 * @param s
	 *            起始日期，字符形式（八位数字），如："20030805"。
	 * @param i
	 *            月份数（整型），i 可以为负数。
	 * @return 日期字符形式（八位数字），如："20031005"。
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-1-6 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */

	public static String getNextDateByMonth(String s, final int i) {
		final SimpleDateFormat simpledateformat = new SimpleDateFormat(
				"yyyyMMdd");
		java.util.Date date = simpledateformat.parse(s, new ParsePosition(0));
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);
		date = calendar.getTime();
		s = simpledateformat.format(date);
		return s;
	}

	/**
	 * 取得当前定制的日期字符串(如：年月日表示 20040430).
	 * 
	 * @return String
	 * @author: wuq
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getNowCustomDate() {
		String timeStr = "";
		timeStr = DateUtil.getStandardDateStrToCustomDateStr(DateUtil.getYMD());
		return timeStr;
	}

	/**
	 * 取得当前的时间字符串(如：时分秒表示 12:00:12 ).
	 * 
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getNowCustomTime() {
		String timeStr = "";
		timeStr = DateUtil.getShortTimeStr(DateUtil.getNowDate());
		return timeStr;
	}

	/**
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getDatePattern() {
		return DateUtil.defaultDatePattern;
	}

	/**
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getDateTimePattern() {
		return DateUtil.getDatePattern() + " HH:mm:ss.S";
	}

	/**
	 * @param aDate
	 *            String
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static final String getDate(final Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(DateUtil.getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * @param aMask
	 *            String
	 * @param strDate
	 *            String
	 * @return Date
	 * @throws ParseException
	 *             ParseException
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static final Date convertStringToDate(final String aMask,
			final String strDate) throws ParseException {
		if (strDate == null) {
			return null;
		}
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		if (DateUtil.log.isDebugEnabled()) {
			DateUtil.log.debug("converting '" + strDate
					+ "' to date with mask '" + aMask + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (final ParseException pe) {
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * @param theTime
	 *            Date
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getTimeNow(final Date theTime) {
		return DateUtil.getDateTime(DateUtil.timePattern, theTime);
	}

	/**
	 * @return Calendar
	 * @throws ParseException
	 *             ParseException
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Calendar getToday() throws ParseException {
		final Date today = new Date();
		final SimpleDateFormat df = new SimpleDateFormat(
				DateUtil.getDatePattern());

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		final String todayAsString = df.format(today);
		final Calendar cal = new GregorianCalendar();
		cal.setTime(DateUtil.convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * @return Timestamp
	 * @throws ParseException
	 *             ParseException
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Timestamp getCurTime() throws ParseException {
		return new Timestamp(DateUtil.getToday().getTime().getTime());
	}

	/**
	 * @param aMask
	 *            String
	 * @param aDate
	 *            String
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static final String getDateTime(final String aMask, final Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			DateUtil.log.debug("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * @param aDate
	 *            String
	 * @return String 返回格式为"yyyymmddhhmiss"
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static final String getDateTime(final Date aDate) {
		String dateFormat = "yyyyMMddHHmmss";
		return getDateTime(dateFormat, aDate);
	}

	/**
	 * @param aDate
	 *            Date
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static final String convertDateToString(final Date aDate) {
		return DateUtil.getDateTime(DateUtil.getDatePattern(), aDate);
	}

	/**
	 * @param aDate
	 *            date
	 * @return String date value
	 * @author: Liuzhuangfei
	 * @修改记录： ==============================================================<br>
	 *        日期:Feb 25, 2010 Liuzhuangfei 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static final String convertDateTimeToString(final Date aDate) {
		return DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", aDate);
	}

	/**
	 * @param strDate
	 *            String
	 * @return Date
	 * @throws ParseException
	 *             ParseException
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Date convertStringToDate(final String strDate)
			throws ParseException {
		Date aDate = null;

		try {
			if (DateUtil.log.isDebugEnabled()) {
				DateUtil.log.debug("converting date with pattern: "
						+ DateUtil.getDatePattern());
			}
			try {
				aDate = DateUtil.convertStringToDate(DateUtil.getDatePattern(),
						strDate);
			} catch (Exception e) {
				aDate = DateUtil.convertStringToDate("yyyyMMdd", strDate);
			}
		} catch (final ParseException pe) {
			DateUtil.log.error("Could not convert '" + strDate
					+ "' to a date, throwing exception");
			DateUtil.log.error(pe.getStackTrace());
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());

		}

		return aDate;
	}

	/**
	 * @param strDate
	 *            String
	 * @return Date
	 * @throws ParseException
	 *             ParseException
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Date convertStringToDatetime(final String strDate)
			throws ParseException {
		Date aDate = null;

		try {
			String part = DateUtil.getDatePattern() + " HH:mm:ss";
			if (DateUtil.log.isDebugEnabled()) {
				DateUtil.log.debug("converting date with pattern: "
						+ DateUtil.getDateTimePattern());
			}

			aDate = DateUtil.convertStringToDate(part, strDate);
		} catch (final ParseException pe) {
			DateUtil.log.error("Could not convert '" + strDate
					+ "' to a date, throwing exception");
			DateUtil.log.error(pe.getStackTrace());
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());

		}

		return aDate;
	}

	/**
	 * @param str
	 *            String
	 * @return String
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2004-04-30 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String strFormatToFull(final String str) {
		return DateUtil.dateTimeToStr(DateUtil.strToDateTime(str));
	}

	/**
	 * 计算除了周67的工作日.
	 * 
	 * @param d1
	 *            Date
	 * @param d2
	 *            Date
	 * @return int
	 * @author: panchh
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-8-27 panchh 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static int workDays(Date d1, Date d2) {
		d1 = DateUtil.strToDate(DateUtil.getDate(d1));
		d2 = DateUtil.strToDate(DateUtil.getDate(d2));
		Date d = (d1.before(d2) ? d1 : d2);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int count = 0;
		while (!d.after(d2)) {
			if (d.compareTo(d2) == 0) {
				break;
			}
			final int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day < 7 && day > 1) {
				count++;
			}
			cal.add(Calendar.DATE, 1);
			d = cal.getTime();
			d = DateUtil.strToDate(DateUtil.getDate(d));
		}
		return count;
	}

	/**
	 * 当前时间增加几个小时的日期，不包括周六，周日.
	 * 
	 * @param intervalHour
	 *            Integer
	 * @return Date
	 * @author: wuq
	 * @修改记录： ==============================================================<br>
	 *        日期:Mar 31, 2008 wuq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String workToDate(final int intervalHour) {
		final int h = intervalHour;
		Calendar calendar = Calendar.getInstance();
		Date d1 = DateUtil.getNowDate();
		calendar.setTime(d1);
		final int h1 = 24 - calendar.get(Calendar.HOUR_OF_DAY);
		final int h2 = h - h1;
		int countDay1 = 0; // 没有工作的天数
		int d3 = h2 / 24;
		final int d4 = d3;
		final int h3 = h2 % 24;
		int day = 0;
		final Calendar cal = Calendar.getInstance();
		final int day0 = cal.get(Calendar.DAY_OF_WEEK);
		// cal.setTime(d1);
		if (day0 == 7 || day0 == 1) { // 周末受理的单算是周一零点受理的 crm00008001
			if (day0 == 7) { // 如果当天是周六加两天
				cal.add(Calendar.DATE, 2);
			} else if (day0 == 1) { // 如果当天是周日加一天
				cal.add(Calendar.DATE, 1);
			}
			final DateFormat df = DateFormat.getDateTimeInstance();
			final SimpleDateFormat simpledateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
			final String calDateStr = simpledateformat.format(cal.getTime())
					+ " 00:00:00";
			try {
				d1 = df.parse(calDateStr);

			} catch (final ParseException pe) {
				pe.printStackTrace();
			}

		} else {
			d1 = cal.getTime();
		}
		if (h2 > 0) {
			final Date d2 = (Date) d1.clone();
			while (d3 > 0) {
				cal.add(Calendar.DATE, 1); // 取第二天
				day = cal.get(Calendar.DAY_OF_WEEK);
				if (day == 7 || day == 1) { // 周末或假期
					countDay1++; // 没上班的天数
				} else {
					d3 = d3 - 1;
				}
			}
			cal.setTime(d2);
			cal.add(Calendar.HOUR, 24 * (d4 + countDay1) + h1 + h3);

		} else {
			cal.add(Calendar.HOUR, h);
		}
		day = cal.get(Calendar.DAY_OF_WEEK);
		while (day == 7 || day == 1) { // 如果是周末或假日要延续
			cal.add(Calendar.DATE, 1); // 取第二天
			day = cal.get(Calendar.DAY_OF_WEEK);
		}
		final DateFormat df = DateFormat.getDateTimeInstance();
		final String d = df.format(cal.getTime());
		return d;
	}

	public static void main(final String[] args) {
	}

	/**
	 * 方法功能: .
	 * 
	 * @param date
	 * @author: liuzhuangfei
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-5-20 liuzhuangfei 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String subDate(Date date, int count) {
		String temp = DateUtil.dateTimeToStr(date);
		if (count == -1) {
			if (temp.indexOf(" ") != -1) {
				temp = temp.substring(0, temp.indexOf(" "));
			}
		} else {
			if (temp.length() > count) {
				temp = temp.substring(0, count);
			}
		}

		return temp;
	}

	/**
	 * 计算包含的工作日天数.
	 * 
	 * @param d1
	 *            Date
	 * @param d2
	 *            Date
	 * @return int 返回在开始日期和结束日期之间包含的工作日天数(排除周67不足一个工作日的以0天记)
	 * @author: chenjp
	 * @修改记录： ==============================================================<br>
	 *        日期:2008-5-25 chenjp 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static int dateDiffweekDay(Date d1, Date d2) {
		int count = 0;
		count = Integer.parseInt(String.valueOf((d2.getTime() - d1.getTime())
				/ (1000 * 3600 * 24)));
		d1 = DateUtil.strToDate(DateUtil.getDate(d1));
		d2 = DateUtil.strToDate(DateUtil.getDate(d2));
		Date d = (d1.before(d2) ? d1 : d2);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		while (!d.after(d2)) {
			final int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day == 7 || day == 1) {
				count = count - 1;
			}
			cal.add(Calendar.DATE, 1);
			d = cal.getTime();
			d = DateUtil.strToDate(DateUtil.getDate(d));
		}
		if (count < 0) {
			count = 0;
		}
		return count;
	}

	public static java.sql.Date strToSqlDate(final String str) {
		java.sql.Date returnValue = null;
		if (str != null && !str.equals("")) {
			returnValue = new java.sql.Date(DateUtil.strToDate(str).getTime());
		}
		return returnValue;
	}

	/**
	 * @param str
	 *            字符串
	 * @param format
	 *            "yyyy-MM-dd HH:mm:ss"
	 * @return date
	 * @author: zfz
	 * @修改记录： ==============================================================<br>
	 *        日期:Aug 30, 2008 zfz 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static java.util.Date str2date(final String str, final String format) {
		java.util.Date ret = null;
		final SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			ret = sdf.parse(str);
		} catch (final ParseException e) {
			e.printStackTrace();
			ret = null;
		}
		return ret;
	}

	/**
	 * @param date
	 *            时间
	 * @return customizestr
	 * @author: peilh
	 * @修改记录： ==============================================================<br>
	 *        日期:Jan 21, 2009 peilh 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String datetime2customizestr(final Date date) {
		String str = "";
		String datetime = "";
		if (date != null) {
			datetime = DateUtil.dateTimeToStr(date);
		} else {
			datetime = DateUtil.dateTimeToStr(DateUtil.getNowDate());
		}
		if (datetime != null && datetime.length() == 19) {
			str = datetime.substring(0, 4) + datetime.substring(5, 7)
					+ datetime.substring(8, 10) + datetime.substring(11, 13)
					+ datetime.substring(14, 16) + datetime.substring(17, 19);
		}
		return str;
	}

	/**
	 * @param number
	 *            总数
	 * @param sd
	 *            时间
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return date
	 * @author: Administrator
	 * @修改记录： ==============================================================<br>
	 *        日期:2009-7-29 Administrator 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Date dateTimeAddIgnoreStartEnd(final int number,
			final Date sd, final String startTime, final String endTime) {
		Date returnDate = null;
		int lpYear = 0, lpMonth = 0, lpDay = 0, lpHour = 0, lpMinutes = 0, lpSeconds = 0, ignoreMinutes = 0, tempMinutes = 0;
		String dateStr;
		lpYear = Integer.parseInt(DateUtil.getYear(DateUtil.dateToStr(sd)));
		lpMonth = Integer.parseInt(DateUtil.getMonth(DateUtil.dateToStr(sd)));
		lpDay = Integer.parseInt(DateUtil.getDay(DateUtil.dateToStr(sd)));
		lpHour = Integer.parseInt(DateUtil.getHour(DateUtil.dateTimeToStr(sd)));
		lpMinutes = Integer.parseInt(DateUtil.getMinutes(DateUtil
				.dateTimeToStr(sd)));
		lpSeconds = Integer.parseInt(DateUtil.getSeconds(DateUtil
				.dateTimeToStr(sd)));
		final String todayTime = DateUtil.dateTimeToStr(sd);
		String startDate = todayTime.substring(0, 11).concat(startTime);
		String endDate = todayTime.substring(0, 11).concat(endTime);
		// 结束点比开始点小且当前小时大于结束点，则跨天
		if (endTime.compareTo(startTime) < 0
				&& lpHour > Integer.parseInt(DateUtil.getHour(endDate))) {
			endDate = DateUtil.dateTimeToStr(DateUtil.dateTimeAdd(
					"d",
					1,
					DateUtil.strToDateTime(todayTime.substring(0, 11).concat(
							endTime))));
		}
		// 开始日期大于结束日期，则开始日期要减一
		if (startDate.compareTo(endDate) > 0) {
			startDate = DateUtil.dateTimeToStr(DateUtil.dateTimeAdd("d", -1,
					DateUtil.strToDateTime(startDate)));
		}
		// String overTime = dateTimeToStr(dateTimeAdd("f", number, sd));
		// //在开始结束时间段内，则需跳过
		if (startDate.compareTo(todayTime) < 0
				&& endDate.compareTo(todayTime) > 0) {
			lpYear = Integer.parseInt(DateUtil.getYear(endDate));
			lpMonth = Integer.parseInt(DateUtil.getMonth(endDate));
			lpDay = Integer.parseInt(DateUtil.getDay(endDate));
			lpHour = Integer.parseInt(DateUtil.getHour(endDate));
			lpMinutes = Integer.parseInt(DateUtil.getMinutes(endDate));
			lpSeconds = Integer.parseInt(DateUtil.getSeconds(endDate));
		} else if (startDate.compareTo(todayTime) > 0) {
			tempMinutes = DateUtil.dateDiff("s",
					DateUtil.strToDateTime(todayTime),
					DateUtil.strToDateTime(startDate)) / 60;
			ignoreMinutes = DateUtil.dateDiff("s",
					DateUtil.strToDateTime(startDate),
					DateUtil.strToDateTime(endDate)) / 60;
		}
		lpMinutes += (number - tempMinutes) + ignoreMinutes;

		dateStr = String.valueOf(lpYear) + "-" + String.valueOf(lpMonth) + "-"
				+ String.valueOf(lpDay) + " " + String.valueOf(lpHour) + ":"
				+ String.valueOf(lpMinutes) + ":" + String.valueOf(lpSeconds);

		returnDate = DateUtil.strToDateTime(dateStr);
		return returnDate;
	}

	/**
	 * dateStr8To10.
	 * 
	 * @param str
	 *            String
	 * @return String
	 * @author wuyx 2011-5-26 wuyx
	 */
	public static String dateStr8To10(String str) {
		if (str != null && str.length() == 8) {
			return str.substring(0, 4) + "-" + str.substring(4, 6) + "-"
					+ str.substring(6, 8);
		}
		return str;
	}

	/**
	 * dateStr10To8.
	 * 
	 * @param str
	 *            String
	 * @return String
	 * @author wuyx 2011-5-26 wuyx
	 */
	public static String dateStr10To8(String str) {
		if (str != null) {
			return str.replace("-", "");
		}
		return str;
	}

	/**
	 * datetimeStr14To19.
	 * 
	 * @param str
	 *            String
	 * @return String
	 * @author wuyx 2011-5-26 wuyx
	 */
	public static String datetimeStr14To19(String str) {
		if (str != null && str.length() == 8) {
			return str.substring(0, 4) + "-" + str.substring(4, 6) + "-"
					+ str.substring(6, 8) + " " + str.substring(8, 10) + ":"
					+ str.substring(10, 12) + ":" + str.substring(12, 14);
		}
		return str;
	}

	/**
	 * datetimeStr19To14.
	 * 
	 * @param str
	 *            String
	 * @return String
	 * @author wuyx 2011-5-26 wuyx
	 */
	public static String datetimeStr19To14(String str) {
		if (str != null) {
			String nstr = str.replace("-", "");
			nstr = nstr.replace(" ", "");
			nstr = nstr.replace(":", "");
			return nstr;
		}
		return str;
	}

	/**
	 * .
	 * 
	 * @param date
	 * @param unit
	 * @param val
	 * @return
	 * @author zfz 2011-5-28 zfz
	 */
	public static Date dateAdd(Date date, int unit, int val) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(unit, val);
		return calendar.getTime();
	}

	/**
	 * .
	 * 
	 * @param date
	 * @param unit
	 * @param val
	 * @return
	 * @author zfz 2011-5-28 zfz
	 */
	public static Date dateDiff(Date date, int unit, int val) {
		int pval = 0 - val;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(unit, pval);
		return calendar.getTime();
	}

	/**
	 * 方法功能: 获取本月的最大天数 .
	 * 
	 * @return 最大天数
	 * @author: Liuzhuangfei
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-7-22 Liuzhuangfei 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static int getCurMonthDay() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int maxDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
		return maxDay;
	}

	/**
	 * 方法功能: .
	 * 
	 * @param date
	 * @return
	 * @author: Liuzhuangfei
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-7-24 Liuzhuangfei 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static int getMonthDays(int month) {
		Calendar cal = new GregorianCalendar();
		// 或者用Calendar cal = Calendar.getInstance();

		/** 设置date **/

		/** 或者设置月份，注意月是从0开始计数的，所以用实际的月份-1才是你要的月份 **/
		// 一月份:
		cal.set(getYearInt(), month - 1, 1);

		/** 如果要获取上个月的 **/
		// cal.set(Calendar.DAY_OF_MONTH, 1);
		// 日期减一,取得上月最后一天时间对象
		// cal.add(Calendar.DAY_OF_MONTH, -1);
		// 输出上月最后一天日期
		// System.out.println(cal.get(Calendar.DAY_OF_MONTH));
		/** 开始用的这个方法获取月的最大天数，总是得到是31天 **/
		// int num = cal.getMaximum(Calendar.DAY_OF_MONTH);
		/** 开始用的这个方法获取实际月的最大天数 **/
		int num2 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return num2;

	}

	/**
	 * 方法功能: 获取当前年份的数字 .
	 * 
	 * @return int int
	 * @author: Liuzhuangfei
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-7-24 Liuzhuangfei 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static int getYearInt() {
		String year = DateUtil.getYear(new Date());
		int lpResult = 0;
		if (year != null) {
			if (year == null) {
				year = "";
			} else {
				year = year.trim();
			}
			try {
				lpResult = Integer.parseInt(year);
			} catch (final NumberFormatException nfe) {
			}
		}
		return lpResult;
	}

	/**
	 * 方法功能: 返回两个时间差.
	 * 
	 * @param startTime
	 *            8:00
	 * @param endTime
	 *            10:00
	 * @param endTime
	 *            format 返回单位 H 小时 M 分钟
	 * @return
	 * @author: chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-7-24 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Double getTimelenNum(String startTime, String endTime,
			String format) {
		if (startTime != null && endTime != null) {
			int startHour = NumericUtil.toInt(StrUtil.split(startTime, ":")[0]);
			int startMin = NumericUtil.toInt(StrUtil.split(startTime, ":")[1]);
			int endHour = NumericUtil.toInt(StrUtil.split(endTime, ":")[0]);
			int endMin = NumericUtil.toInt(StrUtil.split(endTime, ":")[1]);
			int hour = endHour - startHour; // 小时数
			int min = endMin - startMin; // 分钟数
			Double rel = 0.0;
			if ("H".equals(format)) {
				rel = hour + min * 1.0 / 60;
			} else if ("M".equals(format)) {
				rel = (hour * 60 + min) * 1.0;
			}
			return rel;
		} else {
			return 0.0;
		}
	}

	/**
	 * 方法功能: 验证字符串是否是日期格式 .
	 * 
	 * @param strDate
	 * @param sign
	 * @return
	 * @author: chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-9-29 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static boolean isDate(String strDate, String sign) {
		boolean back = true;
		SimpleDateFormat sdf = new SimpleDateFormat(sign);
		try {
			sdf.parse(strDate);
		} catch (ParseException e) {
			back = false;
		}
		return back;
	}

	/**
	 * 方法功能: . 格式化时间字符串 如20091209 格式化成 2009-12-09
	 * 
	 * @author chenmq 2012-1-14 chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2012-1-14 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String formatDateString(String strn) {
		String val = "";
		if (strn != null && strn.length() == 8) {
			String temp = strn.substring(0, 4);
			temp += "-";
			temp += strn.substring(4, 6);
			temp += "-";
			temp += strn.substring(6, 8);
			val = temp;
		}
		return val;
	}

	/**
	 * 获取下个月的1日00:00.
	 * 
	 * @param date
	 *            入参时间
	 * @return 下个月的1日
	 * @author yejb 2012-1-31 yejb
	 */
	public static Date getNextMonth(Date date) {
		String ymd = DateUtil.getYear(date) + "-" + DateUtil.getMonth(date)
				+ "-1";
		Date month = DateUtil.strToDate(ymd);
		Date nextMonth = DateUtil.dateAdd("m", 1, month);
		return nextMonth;
	}

	/**
	 * 
	 * 时间转换成"xxxx年x月x日".
	 * 
	 * @param aDate
	 *            时间
	 * @param dateFormat
	 *            格式
	 * @return String
	 * @author luxb 2012-2-17 luxb
	 */
	public static final String getDateByDateFormat(String dateFormat, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(dateFormat);
			returnValue = df.format(aDate);
		}
		return (returnValue);
	}

	/**
	 * 将对像转换成时间，转换失败则返回当前时间 .
	 * 
	 * @param obj
	 * @return
	 * @author Wangjianjun 2012-4-19 Wangjianjun
	 */
	public static final Date nullToNowDate(Object obj) {
		try {
			if (obj != null) {
				return (Date) obj;
			}
		} catch (Exception e) {
		}
		return new Date();
	}

	/**
	 * 将对像转换成日期类型，空值时返回null .
	 * 
	 * @param obj
	 * @return
	 * @author Wangjianjun 2012-5-10 Wangjianjun
	 */
	public static final Date dateConvert(Object obj) {
		try {
			if (obj != null) {
				return (Date) obj;
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 
	 * 方法功能: .
	 * 
	 * @param str
	 * @return
	 * @author chenmq 2012-8-11 chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2012-8-11 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String convertEnDateStrToDateStr(String str) {
		if (StrUtil.isEmpty(str)) {
			return "";
		}
		if (str.indexOf("CST") != -1 && str.indexOf(":") != -1) {
			SimpleDateFormat format = new SimpleDateFormat(
					"EEE MMM dd hh:mm:ss zzz yyyy", Locale.US);
			try {
				Date d = format.parse(str);
				return convertDateTimeToString(d);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}

	public static String getYYYYMMDDHHmmss(Date date) {
		final StringBuffer backstr = new StringBuffer();

		if (date == null) {
			date = new Date();
		}

		if (date == null) {
			backstr.append("");
		} else {
			final SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
			final String dateStr = time.format(date);
			backstr.append(dateStr);
			/*
			 * ymd = DateFormat.getDateInstance().format(date); if (ymd != null
			 * && ymd.charAt(1) != '-') { String[] ymdArr = StrUtil.split(ymd,
			 * "-"); if (ymdArr.length >= 2) { String y = ymdArr[0]; String m =
			 * ymdArr[1]; if (m.length() == 1) { m = "0".concat(m); } String d =
			 * ymdArr[2]; if (d.length() == 1) { d = "0".concat(d); }
			 * backstr.append(y).append("").append(m).append("").append(d); }
			 * else { backstr.append(""); } } else { backstr.append(""); }
			 */
		}
		return backstr.toString();
	}

	/**
	 * 
	 * . 将2099年12月31日置为失效时间
	 * 
	 * @param strn
	 * @return
	 * @author Wong 2013-6-4 Wong
	 */
	public static Date expDate() {
		Date returnValue = null;
		final DateFormat df = DateFormat.getDateInstance();
		try {
			returnValue = df.parse("2099-12-31");
		} catch (final ParseException pe) {
		}
		return returnValue;
	}

	/**
	 * 时间转字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToStr(final Date date, String pattern) {
		String dateStr = "";
		if (date != null) {
			final SimpleDateFormat time = new SimpleDateFormat(pattern);
			dateStr = time.format(date);
		}
		return dateStr;
	}
}
