package dailydiary.models.type;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Class for PlainTime
 */
public class PlainTime {
	
	/**
	 * Enum for time
	 */
	private enum ETime {
		NIGHT		("NI"),	// NI 03:00
		MORNING		("MO"),	// MO 08:00
		AFTERNOON	("AF"),	// AF 15:00
		EVENING		("EV");	// EV 20:00
		
	    private final String value;
	    
	    ETime(String value) {
	    	this.value = value;
	    }
	    
	    public static ETime convert(String value) {
	    	return Arrays.asList(ETime.values())
	    			.stream()
	    			.filter(x -> x.value.equals(value))
	    			.findFirst().orElse(null);
	    }
	}
	
    private int hour;
    private int minute;
    
    public PlainTime(int hour, int minute){
    	this.hour = hour;
    	this.minute = minute;
    }
    
    public int getHour() { return hour; }
	public int getMinute() { return minute; }
    
    public String toValue() {
    	return String.format("%02d:%02d", hour, minute);
    }
    
    public static PlainTime convert(String value) {
    	int hour = 0;
    	int minute = 0;
    	
    	ETime time = ETime.convert(value);
    	
    	if (time != null) {
			switch (time) {
	    		case NIGHT: hour = 3; break;
	    		case MORNING: hour = 8; break;
	    		case AFTERNOON: hour = 15; break;
	    		case EVENING: hour = 20; break;
				default: break;
			}
			
			return new PlainTime(hour, minute);
    	}
		
		if (Pattern.matches("^\\d{2}:\\d{2}.*", value)) {
			hour = Integer.parseInt(value.substring(0, 2));
			minute = Integer.parseInt(value.substring(3, 5));
			return new PlainTime(hour, minute);
		}
    	
		throw new IllegalArgumentException();
    }
}
