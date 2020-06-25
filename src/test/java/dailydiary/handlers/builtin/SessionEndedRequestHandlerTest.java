package dailydiary.handlers.builtin;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import dailydiary.handlers.TestUtil;
import dailydiary.handlers.builtin.SessionEndedRequestHandler;

public class SessionEndedRequestHandlerTest {
	
    private static RequestHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new SessionEndedRequestHandler();
    }

    @Test
    public void testCanHandle() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }

    @Test
    public void testHandle() {
    	final Response response = TestUtil.slotLessHandlerTest(handler);
    	assertTrue(response.getOutputSpeech().toString().contains(SessionEndedRequestHandler.TXT_RESPONSE));
    }
}
