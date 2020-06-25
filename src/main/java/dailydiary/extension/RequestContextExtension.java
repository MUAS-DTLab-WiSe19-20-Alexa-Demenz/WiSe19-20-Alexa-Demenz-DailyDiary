package dailydiary.extension;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;

import dailydiary.DailyDiaryStreamHandler;

/**
 * Extension class for HandlerInput
 *
 */
public class RequestContextExtension {
	
    private static final Logger LOGGER = LogManager.getLogger(RequestContextExtension.class);
	
	public static String getUserId(HandlerInput input) {
        try {
        	String userId = input.getRequestEnvelope().getContext().getSystem().getUser().getUserId();
	        return (userId == null) ? DailyDiaryStreamHandler.REQUEST_DEFAULT_USER_ID : userId;
        } catch (Exception e) {
        	LOGGER.info("Couldn't get UserId -> Switching to default userId...");
        	return DailyDiaryStreamHandler.REQUEST_DEFAULT_USER_ID; // JUnit-Tests
        }
	}
	
	// Hidding constructor
	private RequestContextExtension() {}
}
