package dailydiary.handlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import dailydiary.DailyDiaryStreamHandler;
import dailydiary.models.DailyDiary;
import dailydiary.models.Event;

public class GetLastEventHandlerTest {
	
    private static RequestHandler handler;
    private static DailyDiary diary;

    @BeforeClass
    public static void setup() {
        handler = new GetLastEventHandler();
        diary = new DailyDiary(DailyDiaryStreamHandler.REQUEST_DEFAULT_USER_ID);
        clean();
    }

    @Before
    public void testCanHandle() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }

    @Test
    public void testHandle() {
        // No last event
    	test(false);

        // Create new event
        Event event = new Event("Test", null, null, null, new Date());
        diary.save(event);

        // Test with last event
        test(true);
    }
    
	/**
	 * Test handler for given slots
	 * @param contains
	 */
	public void test(boolean contains) {
		Response response = TestUtil.slotLessHandlerTest(handler);
		String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
		
        if (contains) {
        	assertFalse(responseText.contains(GetLastEventHandler.TXT_NO_EVENT_FOUND));
            assertTrue(responseText.contains(GetLastEventHandler.TXT_EVENT_FOUND.substring(0, GetLastEventHandler.TXT_EVENT_FOUND.indexOf('%'))));
        } else {
        	assertTrue(responseText.contains(GetLastEventHandler.TXT_NO_EVENT_FOUND));
        }
	}

    @AfterClass
    public static void clean() {
        List<Event> events = diary.getEvents();
        diary.delete(events);
    }
}
