package dailydiary.handlers;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import dailydiary.extension.DateTimeExtenstion;
import dailydiary.extension.ExceptionExtension;
import dailydiary.models.Event;
import dailydiary.models.Participant;

public abstract class AddEventHandler extends DailyDiaryRequestHandler {
	
	private static final Logger LOGGER = LogManager.getLogger(AddEventHandler.class);

    // Slot names
	public static final String SLOT_NAME = "name";
	public static final String SLOT_DESCRIPTION = "description";
	public static final String SLOT_LOCATION = "location";
	public static final String SLOT_PARTICIPANTS = "participants";

	// Text
	public static final String TXT_SAY_REPROMT = "Das habe ich leider nicht verstanden. Bitte lege den Eintrag erneut an.";
	public static final String TXT_REPROMT = "Welchen Eintrag möchtest du erstellen?";
	public static final String TXT_EVENT_CREATED = "Ein Eintrag mit dem Namen %s wurde für den %s um %s erstellt.";
	
	@Override
	public Optional<Response> handle(HandlerInput input) {
		LOGGER.info("Handler process started...");
		ResponseBuilder responseBuilder = input.getResponseBuilder().withShouldEndSession(false);
		IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
		Intent intent = intentRequest.getIntent();
		Map<String, Slot> slots = intent.getSlots();
		LOGGER.info("Received slots: " + (slots == null ? "" : slots.toString()));
		
		try {
			if (slots == null) throw new IllegalArgumentException(); // Minimal requirement
			
			// Get slots
			Slot slotName = slots.get(SLOT_NAME);
			Slot slotDescription = slots.get(SLOT_DESCRIPTION);
			Slot slotLocation = slots.get(SLOT_LOCATION);
			Slot slotParticipants = slots.get(SLOT_PARTICIPANTS);
			
			if (slotName.getValue() == null) throw new IllegalArgumentException(); // Minimal requirement
			
			// Create event
			Event event = new Event();
			event.setName(slotName.getValue());
			event.setDate(extractDateTime(slots));
			event.setDescription(slotDescription.getValue());
			event.setLocation(slotLocation.getValue());
			
			if (slotParticipants.getValue() != null)
				event.setParticipants(Arrays.asList(slotParticipants.getValue().split(" ")).stream().map(Participant::new).collect(Collectors.toList()));

			// Saving event
			diary.save(event);

			assembleResponseSuccess(responseBuilder, event);
			
		} catch (Exception e) {
			assembleResponseFailure(responseBuilder);
    		LOGGER.warn(ExceptionExtension.getStackTrace(e));
		}

		return responseBuilder.build();
	}
	
	public void assembleResponseSuccess(ResponseBuilder responseBuilder, Event event) {
		// Generate response string
		String speechText = String.format(TXT_EVENT_CREATED, event.getName(),
				DateTimeExtenstion.getFormaterDate().format(event.getDate()),
				DateTimeExtenstion.getFormaterTime().format(event.getDate()));
		
		responseBuilder
			.withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, speechText)
			.withSpeech(speechText);
	}
	
	public void assembleResponseFailure(ResponseBuilder responseBuilder) {
		// Render an error since we don't know what input was given
		responseBuilder
			.withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, TXT_SAY_REPROMT)
			.withSpeech(TXT_SAY_REPROMT)
			.withReprompt(TXT_REPROMT);
	}
	
	public abstract Date extractDateTime(Map<String, Slot> slots) throws ParseException;
}
