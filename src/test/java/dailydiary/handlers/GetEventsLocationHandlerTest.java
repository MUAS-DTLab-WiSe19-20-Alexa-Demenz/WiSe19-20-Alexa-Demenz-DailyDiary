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

public class GetEventsLocationHandlerTest {

    private static RequestHandler handler;
    private static DailyDiary diary;

    @BeforeClass
    public static void setup() {
        handler = new GetEventsLocationHandler();
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
    	String location = "TestOrt";
    	
        // Test with no events
    	testWithSlots(location, false);

        // Create new event
        Event event = new Event(name, null, location, null, new Date());
        diary.save(event);

        // Test with events
    	testWithSlots(location, true);
    	
    	// Test empty slots
        Response response = TestUtil.slotLessHandlerTest(handler);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(GetEventsLocationHandler.TXT_NO_EVENTS_FOUND));
    }
    
	/**
	 * Test handler for given slots
	 * @param location
	 * @param contains
	 */
	public void testWithSlots(String location, boolean contains) {
		SlotItem[] slots = { new SlotItem(GetEventsLocationHandler.SLOT_LOCATION, location) };
		Response response = TestUtil.testWithSlot(handler, slots, null);
		String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
		
        if (contains) {
        	assertTrue(responseText.contains(GetEventsLocationHandler.TXT_EVENTS_FOUND));
        } else {
        	assertTrue(responseText.contains(GetEventsLocationHandler.TXT_NO_EVENTS_FOUND));
        }
	}

    @AfterClass
    public static void clean() {
        List<Event> events = diary.getEvents();
        diary.delete(events);
    }
}
