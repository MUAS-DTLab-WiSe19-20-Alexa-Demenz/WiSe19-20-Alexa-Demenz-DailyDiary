package dailydiary.models.type;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import dailydiary.extension.DateTimeExtenstion;
	
public class PlainDate {
	
    private int year;
    private int month;
    private int day;
    
    public PlainDate(int year, int month, int day){
    	this.year = year;
    	this.month = month;
    	this.day = day;
    }
    
    public int getYear() { return year; }
	public int getMonth() { return month; }
	public int getDay() { return day; }

	public String toValue() {
    	return String.format("%04d-%02d-%02d", year, month, day);
    }
    
    public static PlainDate convert(String value) {
    	value = value.toLowerCase();
    	
    	if (Pattern.matches("^\\d{4}-(sp|su|fa|wi)$", value)) { // ....-SP or ....-SU or ....-FA or ....-WI
    		int year = Integer.parseInt(value.substring(0, 4));
    		
    		switch (value.substring(5, 7)) {
	    		case "sp": return new PlainDate(year, 3, 1);
	    		case "su": return new PlainDate(year, 6, 1);
	    		case "fa": return new PlainDate(year, 9, 1);
	    		case "wi": return new PlainDate(year, 12, 1);
				default: break;
    		}
    	} else if (Pattern.matches("^\\d{3}(\\d|x)-(\\d{2}|xx)-(\\d{2}|xx)$", value)) {
    		value = value.replace("-", "").replace("x", "");
    		
    		switch (value.length()) {
	    		case 3: return new PlainDate(Integer.parseInt(value) * 10, 1, 1); // ...X-XX-XX
	    		case 4: return new PlainDate(Integer.parseInt(value), 1, 1);  // ....-XX-XX
	    		case 6: return new PlainDate(
	    					Integer.parseInt(value.substring(0, 4)), 
	    					Integer.parseInt(value.substring(4, 6)), 
	    					1); // ....-..-XX
	    		case 8: return new PlainDate(
	    					Integer.parseInt(value.substring(0, 4)), 
	    					Integer.parseInt(value.substring(4, 6)), 
	    					Integer.parseInt(value.substring(6, 8))); // ....-..-..
				default: break;
			}
    	} else if (Pattern.matches("^\\d{4}-w\\d{1,2}(-we)?$", value)) { // ....-W.. or ....-W..-WE
    		value = value.replace("-we", "");
    		int pyear = Integer.parseInt(value.substring(0, 4)); 
    		int pweek = Integer.parseInt(value.substring(6, value.length()));
    		
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(new Date(System.currentTimeMillis()));
    		cal.setTimeZone(DateTimeExtenstion.TIMEZONE_DEFAULT);
    		cal.set(Calendar.YEAR, pyear);
    		cal.set(Calendar.WEEK_OF_YEAR, pweek);
    		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    		
    		int year = cal.get(Calendar.YEAR); // get year
    		int month = cal.get(Calendar.MONTH) + 1; // get month
    		int day = cal.get(Calendar.DAY_OF_MONTH); // get day
    		return new PlainDate(year, month, day);
    	}
    	
		throw new IllegalArgumentException();
    }
}
