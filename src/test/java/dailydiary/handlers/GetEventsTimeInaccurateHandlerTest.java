package dailydiary.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import dailydiary.extension.DateTimeExtenstion;
import dailydiary.handlers.TestUtil.SlotItem;
import dailydiary.models.DailyDiary;
import dailydiary.models.Event;
import dailydiary.models.type.ERelativeMoment;

public class GetEventsTimeInaccurateHandlerTest {
	
	private static RequestHandler handler;
	private static DailyDiary diary;

    @BeforeClass
	public static void setup() {
		handler = new GetEventsTimeInaccurateHandler();
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
    	String name = "TestName";
    	
        // Test with no events
		testWithSlots(ERelativeMoment.DAY_BEFORE_YESTERDAY.getValues().get(0), 0);
		testWithSlots(ERelativeMoment.YESTERDAY.getValues().get(0), 0);
		testWithSlots(ERelativeMoment.TODAY.getValues().get(0), 0);
		testWithSlots(ERelativeMoment.TOMORROW.getValues().get(0), 0);
		testWithSlots(ERelativeMoment.DAY_AFTER_TOMORROW.getValues().get(0), 0);

		// Create new events
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ERelativeMoment.DAY_BEFORE_YESTERDAY, 0)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ERelativeMoment.YESTERDAY, 0)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ERelativeMoment.TODAY, 0)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ERelativeMoment.TOMORROW, 0)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ERelativeMoment.DAY_AFTER_TOMORROW, 0)));
		
        // Test with events
		testWithSlots(ERelativeMoment.DAY_BEFORE_YESTERDAY.getValues().get(0), 1);
		testWithSlots(ERelativeMoment.YESTERDAY.getValues().get(0), 1);
		testWithSlots(ERelativeMoment.TODAY.getValues().get(0), 1);
		testWithSlots(ERelativeMoment.TOMORROW.getValues().get(0), 1);
		testWithSlots(ERelativeMoment.DAY_AFTER_TOMORROW.getValues().get(0), 1);
    	
    	// Test empty slots
        Response response = TestUtil.slotLessHandlerTest(handler);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(GetEventsTimeInaccurateHandler.TXT_NO_EVENTS_FOUND));
	}

	/**
	 * Test handler for given slots
	 * @param moment
	 * @param countMatch
	 */
	public void testWithSlots(String moment, int countMatch) {
		SlotItem[] slots = { new SlotItem(GetEventsTimeInaccurateHandler.SLOT_MOMENT, moment) };
		
        Response response = TestUtil.testWithSlot(handler, slots, null);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        int count = 0;
		
        if (responseText.contains(GetEventsTimeRelativeHandler.TXT_EVENTS_FOUND)) {
        	String searchText = "Eintrag";
        	count = (responseText.length() - responseText.replace(searchText, "").length()) / searchText.length();
        } else {
			assertTrue(responseText.contains(GetEventsTimeRelativeHandler.TXT_NO_EVENTS_FOUND));
        }
        
		assertEquals(countMatch, count);
	}

    @AfterClass
	public static void clean() {
		List<Event> events = diary.getEvents();
		diary.delete(events);
	}
}
