package dailydiary.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import dailydiary.models.Event;
import dailydiary.models.Participant;

public class GetEventsParticipantHandler extends GetEventsHandler {

    // Slot names
	public static final String SLOT_PARTICIPANT = "participant";

	// Text
	public static final String TXT_REPROMPT = "Welche Person hat an den gesuchten Einträgen teilgenommen.";
	public static final String TXT_EVENTS_FOUND = "Du hast mit dieser Person folgende Termine gehabt: ";
	public static final String TXT_NO_EVENTS_FOUND = "Mit dieser Person wurden keine Einträge gefunden";

	@Override
	public List<Event> searchEvents(Map<String, Slot> slots) {
		Slot participant = slots.get(SLOT_PARTICIPANT);
		
		String participantName = participant.getValue();
		Participant person = new Participant(participantName);
		List<Participant> list = new ArrayList<>();
		list.add(person);

		return diary.getEvents(null, null, null, null, null, list);
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