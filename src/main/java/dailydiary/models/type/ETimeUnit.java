package dailydiary.models.type;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Enum for time unites
 */
public enum ETimeUnit{
	YEAR("jahr", "jahre", "jahren"),
	MONTH("monate", "monaten"),
	WEEK("woche", "wochen"),
	DAY("tag", "tage", "tagen"),
	HOUR("stunde", "stunden"),
	MINUTE("minute", "minuten"),
	SECOND("sekunde", "sekunden");

    private final List<String> values;

    ETimeUnit(String... values) {
        this.values = Arrays.asList(values);
    }
    
    public List<String> getValues() { return values; }

    public static ETimeUnit convert(String value) {
    	return Arrays.asList(ETimeUnit.values())
    			.stream()
    			.filter(x -> x.values.contains(value.toLowerCase()))
    			.findFirst().orElseThrow(NoSuchElementException::new);
    }
}