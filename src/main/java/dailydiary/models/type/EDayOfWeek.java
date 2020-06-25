package dailydiary.models.type;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Enum for day of week
 */
public enum EDayOfWeek {
	MONDAY("monday"),
	TUESDAY("tuesday"),
	WEDNESDAY("wednesday"),
	THURSDAY("thursday"),
	FRIDAY("friday"),
	SATURDAY("saturday"),
	SUNDAY("sunday");
	
    private final String value;
    
    EDayOfWeek(String value){
    	this.value = value;
    }
    
    public String getValue() { return value; }
    
    public static EDayOfWeek convert(String value) {
    	return Arrays.asList(EDayOfWeek.values())
    			.stream()
    			.filter(x -> x.value.equalsIgnoreCase(value))
    			.findFirst().orElseThrow(NoSuchElementException::new);
    }
}
