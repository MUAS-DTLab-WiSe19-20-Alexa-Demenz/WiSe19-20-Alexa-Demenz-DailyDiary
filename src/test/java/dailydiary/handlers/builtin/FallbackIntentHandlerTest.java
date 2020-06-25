package dailydiary.handlers.builtin;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import dailydiary.handlers.TestUtil;
import dailydiary.handlers.builtin.FallbackIntentHandler;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class FallbackIntentHandlerTest {

    private static RequestHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new FallbackIntentHandler();
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
    	assertTrue(response.getOutputSpeech().toString().contains(FallbackIntentHandler.TXT_RESPONSE));
    }
}

