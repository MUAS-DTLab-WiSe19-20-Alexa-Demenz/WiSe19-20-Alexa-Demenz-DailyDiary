package dailydiary.handlers.builtin;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import dailydiary.handlers.TestUtil;
import dailydiary.handlers.builtin.CancelAndStopIntentHandler;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

public class CancelAndStopIntentHandlerTest {

    private static RequestHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new CancelAndStopIntentHandler();
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
    	String resp = response.getOutputSpeech().toString();
    	assertTrue(Arrays.stream(CancelAndStopIntentHandler.TXT_RESPONSE).parallel().anyMatch(resp::contains));
    }
}

