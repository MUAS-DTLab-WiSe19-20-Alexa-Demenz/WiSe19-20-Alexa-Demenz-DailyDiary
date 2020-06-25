package dailydiary.handlers.builtin;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import dailydiary.handlers.DailyDiaryRequestHandler;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

/**
 * RequestHanlder for conversation ending
 */
public class SessionEndedRequestHandler implements RequestHandler {
	
	public static final String TXT_RESPONSE = "Auf Wiedersehen!";
	
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(SessionEndedRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
        		.withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, TXT_RESPONSE)
        		.withSpeech(TXT_RESPONSE)
        		.withShouldEndSession(true).build();
    }
}
