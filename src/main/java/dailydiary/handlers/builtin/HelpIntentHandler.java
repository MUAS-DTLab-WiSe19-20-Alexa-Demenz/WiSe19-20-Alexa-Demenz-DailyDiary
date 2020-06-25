package dailydiary.handlers.builtin;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import dailydiary.handlers.DailyDiaryRequestHandler;

/**
 * RequestHanlder for help conversation
 */
public class HelpIntentHandler implements RequestHandler {

	public static final String TXT_REPROMPT = "Frage mich nach einem Eintrag oder erstelle eine neues.";
    public static final String TXT_RESPONSE = "Ich kann dir bei folgenden Sachen helfen:"
            + " Frage zum Beispiel: Was habe ich zuletzt gemacht?"
            + " Oder lege einen neuen Eintrag an indem du sagst: Erstelle den Eintrag Fahradfahren.";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
        		.withSimpleCard(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE, TXT_RESPONSE)
        		.withSpeech(TXT_RESPONSE)
                .withReprompt(TXT_REPROMPT)
                .withShouldEndSession(false).build();
    }
}
