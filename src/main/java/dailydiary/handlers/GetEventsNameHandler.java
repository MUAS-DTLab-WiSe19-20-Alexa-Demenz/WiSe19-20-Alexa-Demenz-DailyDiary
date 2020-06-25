package dailydiary.handlers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import dailydiary.models.Event;

public class GetEventsNameHandler extends GetEventsHandler {

    // Slot names
    public static final String SLOT_NAME = "name";

    // Text
    public static final String TXT_REPROMPT = "Nach welchem Namen oder welcher Beschreibung möchtest du suchen?";
    public static final String TXT_EVENTS_FOUND = "Die folgenden Einträge wurden gefunden: ";
    public static final String TXT_NO_EVENTS_FOUND = "Es wurden kein Eintrag mit diesem Namen oder dieser Beschreibung gefunden.";

	@Override
	public List<Event> searchEvents(Map<String, Slot> slots) {
        Slot nameSlot = slots.get(SLOT_NAME);
        String name = nameSlot.getValue();
        List<Event> eventList = diary.getEvents(name, null, null, null, null, null);
        eventList.addAll(diary.getEvents(null, name, null, null, null, null));
		return eventList;
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
