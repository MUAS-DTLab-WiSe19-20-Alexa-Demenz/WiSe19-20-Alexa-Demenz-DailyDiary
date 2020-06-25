package dailydiary.handlers;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.model.Slot;

import dailydiary.extension.DateTimeExtenstion;
import dailydiary.models.type.PlainDate;
import dailydiary.models.type.PlainTime;

/**
 * Handler for precise event creation.
 *
 */
public class AddEventPreciseHandler extends AddEventHandler {
	
	private static final Logger LOGGER = LogManager.getLogger(AddEventPreciseHandler.class);

    // Slot names
	public static final String SLOT_DATE = "date";
	public static final String SLOT_TIME = "time";

	@Override
	public Date extractDateTime(Map<String, Slot> slots) throws ParseException {
		Slot slotDate = slots.get(SLOT_DATE);
		Slot slotTime = slots.get(SLOT_TIME);
		LOGGER.info("Following data for datatime received... date: " + slotDate.getValue() + " time: "  + slotTime.getValue());
		PlainDate pdate = PlainDate.convert(slotDate.getValue());
		PlainTime ptime = PlainTime.convert(slotTime.getValue());
		return DateTimeExtenstion.getDate(pdate, ptime);
	}
}
