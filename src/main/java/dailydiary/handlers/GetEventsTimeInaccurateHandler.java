package dailydiary.handlers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;


import dailydiary.extension.DateTimeExtenstion;
import dailydiary.models.Event;
import dailydiary.models.type.ERelativeMoment;

public class GetEventsTimeInaccurateHandler extends GetEventsHandler {

    // Slot names
	public static final String SLOT_MOMENT = "moment";

	// Text
	public static final String TXT_REPROMPT = "Ich habe die Zeitangabe leider nicht verstanden.";
	public static final String TXT_EVENTS_FOUND = "Im gesuchten Zeitraum wurden folgende Einträge gefunden: ";
	public static final String TXT_NO_EVENTS_FOUND = "Innerhalb des angegebenen Zeitraumes wurden keine Einträge gefunden";

	@Override
	public List<Event> searchEvents(Map<String, Slot> slots) {
		Slot momentSlot = slots.get(SLOT_MOMENT);
		
		ERelativeMoment moment = ERelativeMoment.convert(momentSlot.getValue());
		Date dateBegin = DateTimeExtenstion.getDate(moment, 0);
		Date dateEnd = DateTimeExtenstion.getDate(moment, 1);

		return diary.getEvents(null, null, null, dateBegin, dateEnd, null);
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
