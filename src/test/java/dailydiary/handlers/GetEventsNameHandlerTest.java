package dailydiary.handlers;

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
import dailydiary.handlers.TestUtil.SlotItem;
import dailydiary.models.DailyDiary;
import dailydiary.models.Event;

public class GetEventsNameHandlerTest {

    private static RequestHandler handler;
    private static DailyDiary diary;

    @BeforeClass
    public static void setup() {
        handler = new GetEventsNameHandler();
        diary = new DailyDiary(DailyDiaryStreamHandler.REQUEST_DEFAULT_USER_ID);
        clean();
    }

    @Before
    public void testCanHandle() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }

    /**
     * Test handler for different values.
     */
    @Test
    public void testHandle() {        
    	String name = "TestName";
    	
        // Test with no events
    	testWithSlots(name, false);

        // Create new event
        Event event = new Event(name, null, null, null, new Date());
        diary.save(event);

        // Test with events
    	testWithSlots(name, true);
    	
    	// Test empty slots
        Response response = TestUtil.slotLessHandlerTest(handler);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(GetEventsNameHandler.TXT_NO_EVENTS_FOUND));
    }
    
	/**
	 * Test handler for given slots
	 * @param name
	 * @param contains
	 */
	public void testWithSlots(String name, boolean contains) {
		SlotItem[] slots = { new SlotItem(GetEventsNameHandler.SLOT_NAME, name) };
		Response response = TestUtil.testWithSlot(handler, slots, null);
		String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
		
        if (contains) {
        	assertTrue(responseText.contains(GetEventsNameHandler.TXT_EVENTS_FOUND));
        } else {
        	assertTrue(responseText.contains(GetEventsNameHandler.TXT_NO_EVENTS_FOUND));
        }
	}

    @AfterClass
    public static void clean() {
        List<Event> events = diary.getEvents();
        diary.delete(events);
    }
}
