package dailydiary.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import dailydiary.extension.DateTimeExtenstion;
import dailydiary.models.Event;

public class GetLastEventHandler extends GetEventsHandler {
	
    // Text
    public static final String TXT_REPROMPT = "Erstelle zuerst einen neuen Eintrag.";
    public static final String TXT_EVENT_FOUND = "Der letzte Eintrag war %s und fand am %s um %s statt.";
    public static final String TXT_NO_EVENT_FOUND = "Es wurde noch kein Eintrag angelegt.";

	@Override
	public List<Event> searchEvents(Map<String, Slot> slots) {
		List<Event> list = new ArrayList<>();
		Event lstEvent = diary.getLastEvent();
		
		if (lstEvent != null)
			list.add(lstEvent);
		
        return list;
	}

	@Override
	public void assembleResponseFound(ResponseBuilder responseBuilder, List<Event> events) {
		Event lstEvent = events.get(0);
        String date = DateTimeExtenstion.getFormaterDate().format(lstEvent.getDate());
        String time = DateTimeExtenstion.getFormaterTime().format(lstEvent.getDate());
        String result = String.format(TXT_EVENT_FOUND, lstEvent.getName(), date, time);
        
        responseBuilder
        		.withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, result)
                .withSpeech(result);
	}

	@Override
	public void assembleResponseNotFound(ResponseBuilder responseBuilder) {
        responseBuilder
        	.withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, TXT_NO_EVENT_FOUND)
        	.withSpeech(TXT_NO_EVENT_FOUND)
        	.withReprompt(TXT_REPROMPT);
	}
}
