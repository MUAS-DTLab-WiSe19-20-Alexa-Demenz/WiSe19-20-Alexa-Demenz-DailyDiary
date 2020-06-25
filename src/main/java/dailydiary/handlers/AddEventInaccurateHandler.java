package dailydiary.handlers;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.model.Slot;

import dailydiary.extension.DateTimeExtenstion;
import dailydiary.models.type.EDayOfWeek;
import dailydiary.models.type.PlainTime;

public class AddEventInaccurateHandler extends AddEventHandler {
	
	private static final Logger LOGGER = LogManager.getLogger(AddEventInaccurateHandler.class);

    // Slot names
	public static final String SLOT_DAYOFWEEK = "dayofweek";
	public static final String SLOT_TIME = "time";

	@Override
	public Date extractDateTime(Map<String, Slot> slots) {
		Slot slotDayOfWeek = slots.get(SLOT_DAYOFWEEK);
		Slot slotTime = slots.get(SLOT_TIME);
		LOGGER.info("Following data for datatime received... dayofweek: " + slotDayOfWeek.getValue() + " time: "  + slotTime.getValue());
		EDayOfWeek edayofweek = EDayOfWeek.convert(slotDayOfWeek.getValue());
		PlainTime ptime = PlainTime.convert(slotTime.getValue());
		return DateTimeExtenstion.getDate(edayofweek, ptime);
	}
}
