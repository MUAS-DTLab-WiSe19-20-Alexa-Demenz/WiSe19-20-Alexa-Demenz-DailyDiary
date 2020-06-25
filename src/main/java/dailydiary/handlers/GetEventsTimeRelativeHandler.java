package dailydiary.handlers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import dailydiary.extension.DateTimeExtenstion;
import dailydiary.models.Event;
import dailydiary.models.type.ETimeUnit;

public class GetEventsTimeRelativeHandler extends GetEventsHandler {

	public static final int COUNT_DEFAULT = 3;

    // Slot names
	public static final String SLOT_UNIT = "unit";
	public static final String SLOT_COUNT = "count";

	// Text
	public static final String TXT_REPROMPT = "Ich habe die Zeitangabe leider nicht verstanden.";
	public static final String TXT_EVENTS_FOUND = "Im gesuchten Zeitraum wurden folgende Einträge gefunden: ";
	public static final String TXT_NO_EVENTS_FOUND = "Innerhalb des angegebenen Zeitraumes wurden keine Einträge gefunden";

	@Override
	public List<Event> searchEvents(Map<String, Slot> slots) {
		Slot unitSlot = slots.get(SLOT_UNIT);
		Slot countSlot = slots.get(SLOT_COUNT);
		String strCount = countSlot.getValue();
		
		ETimeUnit eunit = ETimeUnit.convert(unitSlot.getValue());
		int count =  strCount != null && strCount.matches("-?\\d+") ? Integer.valueOf(strCount) : COUNT_DEFAULT;
		Date startDate = DateTimeExtenstion.getDate(eunit, -count);

		return diary.getEvents(null, null, null, startDate, new Date(), null);
	}

	@Override
	public void assembleResponseFound(ResponseBuilder responseBuilder, List<Event> events) {
        StringBuilder result = new StringBuilder();
        result.append(TXT_EVENTS_FOUND);
        result.append(events.stream().map(Event::toSpeech).collect(Collectors.joining(" ")));

        responseBuilder
        	.withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, result.toString())
        	.withSpeech(result.toString());
	}

	@Override
	public void assembleResponseNotFound(ResponseBuilder responseBuilder) {
        responseBuilder
        	.withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, TXT_NO_EVENTS_FOUND)
        	.withSpeech(TXT_NO_EVENTS_FOUND)
        	.withReprompt(TXT_REPROMPT);
	}
}
