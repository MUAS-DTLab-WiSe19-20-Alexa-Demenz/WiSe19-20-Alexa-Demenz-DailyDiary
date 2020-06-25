package dailydiary.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import dailydiary.extension.RequestContextExtension;
import dailydiary.models.DailyDiary;

public abstract class DailyDiaryRequestHandler implements RequestHandler {
	
	public static final String RESPONSE_CARD_TITLE = "DailyDiary";
	
	private static final Logger LOGGER = LogManager.getLogger(DailyDiaryRequestHandler.class);
    protected DailyDiary diary;
    
	@Override
	public boolean canHandle(HandlerInput input) {
		String handler = getClass().getSimpleName().replace("Handler", "");
		LOGGER.debug("Feasibility check for the handler '"+ handler + "'...");
		
        if (!input.matches(intentName(handler))) 
        	return false;
        
        diary = new DailyDiary(RequestContextExtension.getUserId(input));
        return true;
	}
	
	@Override
	public abstract Optional<Response> handle(HandlerInput input);
}