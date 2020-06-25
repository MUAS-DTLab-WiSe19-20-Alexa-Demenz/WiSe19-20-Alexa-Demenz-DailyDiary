package dailydiary.handlers.builtin;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import dailydiary.DailyDiaryStreamHandler;

import static com.amazon.ask.request.Predicates.requestType;

/**
 * RequestHanlder for conversation initialization
 */
public class LaunchRequestHandler implements RequestHandler {

	public static final String TXT_TITLE = "Begrüßung zum " + DailyDiaryStreamHandler.SKILL_NAME;
	public static final String TXT_WELCOME = String.format("Hier ist das %s. Hast du mir was zu erzählen.", DailyDiaryStreamHandler.SKILL_NAME);
	public static final String TXT_HELP = "Welche Aktion soll ich ausführen.";
	
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
        		.withSimpleCard(TXT_TITLE, TXT_WELCOME)
                .withSpeech(String.format("Hallo. %s", TXT_WELCOME))
                .withReprompt(TXT_HELP).build();
    }
}
