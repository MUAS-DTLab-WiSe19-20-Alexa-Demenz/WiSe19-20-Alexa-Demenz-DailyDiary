package dailydiary.handlers.builtin;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import dailydiary.handlers.DailyDiaryRequestHandler;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

// 2018-July-09: AMAZON.FallackIntent is only currently available in en-US locale.
//              This handler will not be triggered except in that locale, so it can be
//              safely deployed for any locale.
/**
 * RequestHanlder for fallback conversation
 */
public class FallbackIntentHandler implements RequestHandler {
	
    public static final String TXT_RESPONSE = "Tut mir leid. Das habe ich nicht verstanden."
    		+ " Sage einfach Hilfe, wenn du Unterstützung brauchst.";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.FallbackIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, TXT_RESPONSE)
                .withSpeech(TXT_RESPONSE)
                .withReprompt(TXT_RESPONSE)
                .withShouldEndSession(false).build();
    }
}
 