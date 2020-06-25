package dailydiary.extension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dailydiary.models.type.PlainDate;
import dailydiary.models.type.EDayOfWeek;
import dailydiary.models.type.ERelativeMoment;
import dailydiary.models.type.PlainTime;
import dailydiary.models.type.ETimeUnit;

public class DateTimeExtenstion {
	
	private static final Logger LOGGER = LogManager.getLogger(DateTimeExtenstion.class);
	
	private static final String TIMEZONE_ID_DEFAULT = "Europe/Berlin";
	private static final String FORMAT_DATE = "yyyy-MM-dd";
	private static final String FORMAT_TIME = "HH:mm";
	private static final String FORMAT_DATETIME = FORMAT_DATE + " " + FORMAT_TIME;
	
	private static final String LOG_MSG_PARAMETER = "Building date with following data...";
	private static final String LOG_MSG_RESULT = "-> Result: data: %s, milliseconds: %d";
	
	public static final TimeZone TIMEZONE_DEFAULT = TimeZone.getTimeZone(TIMEZONE_ID_DEFAULT);

	public static SimpleDateFormat getFormaterDate() {
		SimpleDateFormat formater = new SimpleDateFormat(FORMAT_DATE);
		formater.setTimeZone(TIMEZONE_DEFAULT); 
		return formater;
	}
	
	public static SimpleDateFormat getFormaterTime() {
		SimpleDateFormat formater = new SimpleDateFormat(FORMAT_TIME);
		formater.setTimeZone(TIMEZONE_DEFAULT); 
		return formater;
	}
	
	public static SimpleDateFormat getFormaterDateTime() {
		SimpleDateFormat formater = new SimpleDateFormat(FORMAT_DATETIME);
		formater.setTimeZone(TIMEZONE_DEFAULT); 
		return formater;
	}
	
	public static Date getDate(EDayOfWeek dayOfWeek, PlainTime time) {
		LOGGER.info(String.format("%s dayOfWeek: %s, time: %s", LOG_MSG_PARAMETER, dayOfWeek.toString(), time.toValue()));
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		cal.setTimeZone(TIMEZONE_DEFAULT);
		cal.set(Calendar.DAY_OF_WEEK, (dayOfWeek.ordinal() + 2) % 7);
		cal.set(Calendar.HOUR_OF_DAY, time.getHour());
		cal.set(Calendar.MINUTE, time.getMinute());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		LOGGER.info(String.format(LOG_MSG_RESULT, cal.getTime(), cal.getTimeInMillis()));
		return cal.getTime();
	}
	
	public static Date getDate(ETimeUnit unit, int count) {
		LOGGER.info(String.format("%s unit: %s, count: %d", LOG_MSG_PARAMETER, unit.toString(), count));
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		cal.setTimeZone(TIMEZONE_DEFAULT);
		
		switch (unit) {
			case YEAR: 
				cal.add(Calendar.YEAR, count); break;
			case MONTH: 
				cal.add(Calendar.MONTH, count); break;
			case WEEK: 
				cal.add(Calendar.WEEK_OF_YEAR, count); break;
			case DAY: 
				cal.add(Calendar.DATE, count); break;
			case HOUR: 
				cal.add(Calendar.HOUR_OF_DAY, count); break;
			case MINUTE: 
				cal.add(Calendar.MINUTE, count); break;
			case SECOND: 
				cal.add(Calendar.SECOND, count); break;
			default: break;
		}
		
		LOGGER.info(String.format(LOG_MSG_RESULT, cal.getTime(), cal.getTimeInMillis()));
		return cal.getTime();
	}
	
	public static Date getDate(ERelativeMoment moment, int offSetDay) {
		LOGGER.info(String.format("%s moment: %s, offSetDay: %d", LOG_MSG_PARAMETER, moment.toString(), offSetDay));
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		cal.setTimeZone(TIMEZONE_DEFAULT);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		switch (moment) {
			case DAY_AFTER_TOMORROW: 
				cal.add(Calendar.DATE, 2 + offSetDay); break;
			case TOMORROW: 
				cal.add(Calendar.DATE, 1 + offSetDay); break;
			case TODAY: 
				cal.add(Calendar.DATE, offSetDay); break;
			case YESTERDAY: 
				cal.add(Calendar.DATE, -1 + offSetDay); break;
			case DAY_BEFORE_YESTERDAY: 
				cal.add(Calendar.DATE, -2 + offSetDay); break;
			default: break;
		}

		LOGGER.info(String.format(LOG_MSG_RESULT, cal.getTime(), cal.getTimeInMillis()));
		return cal.getTime();
	}
	
	public static Date getDate(PlainDate date, PlainTime time) {
		LOGGER.info(String.format("%s date: %s %s", LOG_MSG_PARAMETER, date.toValue(), time.toValue()));
		
		try {
			Date datetime = getFormaterDateTime().parse(date.toValue() + " " + time.toValue());
			LOGGER.info(String.format(LOG_MSG_RESULT, datetime, datetime.getTime()));
			return datetime;
		} catch (ParseException e) {
    		LOGGER.error(ExceptionExtension.getStackTrace(e));
    		return new Date(0);
		}
	}
	
	public static Date getDate(PlainDate date, int offSetDay) {
		LOGGER.info(String.format("%s date: %s, offSetDay: %d", LOG_MSG_PARAMETER, date.toValue(), offSetDay));
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TIMEZONE_DEFAULT);
		cal.set(Calendar.YEAR, date.getYear());
		cal.set(Calendar.MONTH, date.getMonth() - 1);
		cal.set(Calendar.DAY_OF_MONTH, date.getDay());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, offSetDay);
		LOGGER.info(String.format(LOG_MSG_RESULT, cal.getTime(), cal.getTimeInMillis()));
		return cal.getTime();
	}

	// Hidding constructor
	private DateTimeExtenstion() {}
}
