package dailydiary.handlers;

import java.util.Date;
import java.util.Map;

import com.amazon.ask.model.Slot;

public class AddEventRoutineHandler extends AddEventHandler {

	@Override
	public Date extractDateTime(Map<String, Slot> slots) {
		return new Date(System.currentTimeMillis());
	}
}
