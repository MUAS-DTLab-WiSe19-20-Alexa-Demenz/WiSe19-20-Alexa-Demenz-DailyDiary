package dailydiary.handlers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import dailydiary.extension.ExceptionExtension;
import dailydiary.models.Event;

public abstract class GetEventsHandler extends DailyDiaryRequestHandler {

	private static final Logger LOGGER = LogManager.getLogger(GetEventsHandler.class);
	
	@Override
	public Optional<Response> handle(HandlerInput input) {
		LOGGER.info("Handler process started...");
		ResponseBuilder responseBuilder = input.getResponseBuilder().withShouldEndSession(false);
		IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
		Map<String, Slot> slots = intentRequest.getIntent().getSlots();
		LOGGER.info("Received slots: " + (slots == null ? "" : slots.toString()));

		try {
			List<Event> eventList = searchEvents(slots);

			if (!eventList.isEmpty())
				assembleResponseFound(responseBuilder, eventList);
			else
				assembleResponseNotFound(responseBuilder);
		} catch (Exception e) {
			assembleResponseNotFound(responseBuilder);
    		LOGGER.warn(ExceptionExtension.getStackTrace(e));
		}
		
		return responseBuilder.build();
	}
	
	public abstract List<Event> searchEvents(Map<String, Slot> slots);
	
	public abstract void assembleResponseFound(ResponseBuilder responseBuilder, List<Event> events);
	
	public abstract void assembleResponseNotFound(ResponseBuilder responseBuilder);
}