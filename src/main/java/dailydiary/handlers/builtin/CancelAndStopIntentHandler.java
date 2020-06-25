package dailydiary.handlers.builtin;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import dailydiary.handlers.DailyDiaryRequestHandler;

import java.util.Optional;
import java.util.Random;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * RequestHanlder for conversation initialization
 */
public class CancelAndStopIntentHandler implements RequestHandler {
	
	private static final Random random = new Random();
	
	protected static final String[] TXT_RESPONSE = { 
			"Ok bis zum nächsten Mal.",
			"Bis dann.",
			"Ich hoffe ich konnte dir helfen.",
			"Tschau.",
			"Wir sehen uns.",
			"Bis demnächst." };
	
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.StopIntent").or(intentName("AMAZON.CancelIntent").or(intentName("AMAZON.NavigateHomeIntent"))));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
    	int rand = random.nextInt(6);
    	
        return input.getResponseBuilder()
                .withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, TXT_RESPONSE[rand])
                .withSpeech(TXT_RESPONSE[rand])
                .withShouldEndSession(true)
                .build();
    }
}
