package dailydiary.models.type;

import java.util.Arrays; 
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Enum for the relative moment
 */
public enum ERelativeMoment{
	DAY_AFTER_TOMORROW("den übernächsten tag", "übernächsten tag", "übermorgen"),
	TOMORROW("den nächsten tag", "nächsten tag", "morgen"),
	TODAY("heute"),
	YESTERDAY("gestern"),
	DAY_BEFORE_YESTERDAY("vorgestern");
    
    private final List<String> values;

    ERelativeMoment(String... values) {
        this.values = Arrays.asList(values);
    }
    
    public List<String> getValues() { return values; }
    
    public static ERelativeMoment convert(String value) {
    	return Arrays.asList(ERelativeMoment.values())
    			.stream()
    			.filter(x -> x.values.contains(value.toLowerCase()))
    			.findFirst().orElseThrow(NoSuchElementException::new);
    }
}
