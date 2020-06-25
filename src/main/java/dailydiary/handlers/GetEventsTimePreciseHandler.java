package dailydiary.handlers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import dailydiary.extension.DateTimeExtenstion;
import dailydiary.models.Event;
import dailydiary.models.type.PlainDate;

public class GetEventsTimePreciseHandler extends GetEventsHandler {

    // Slot names
	public static final String SLOT_START_DATE = "from";
	public static final String SLOT_END_DATE = "to";

	// Text
	public static final String TXT_REPROMPT = "Innerhalb welches Zeitraumes suchst du die Einträge?"
			+ " Bitte gib ein Start- und ein Enddatum an.";

	public static final String TXT_EVENTS_FOUND = "Im gesuchten Zeitraum wurden folgende Einträge gefunden: ";
	public static final String TXT_NO_EVENTS_FOUND = "Innerhalb des angegebenen Zeitraumes wurden keine Einträge gefunden";

	@Override
	public List<Event> searchEvents(Map<String, Slot> slots) {
		Slot startDateSlot = slots.get(SLOT_START_DATE);
		Slot endDateSlot = slots.get(SLOT_END_DATE);
		
		PlainDate startPdate = PlainDate.convert(startDateSlot.getValue());
		PlainDate endPdate = PlainDate.convert(endDateSlot.getValue());
		Date startDate = DateTimeExtenstion.getDate(startPdate, 0);
		Date endDate = DateTimeExtenstion.getDate(endPdate, 1);

		return diary.getEvents(null, null, null, startDate, endDate, null);
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