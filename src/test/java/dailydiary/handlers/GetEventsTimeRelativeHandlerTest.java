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
import dailydiary.models.type.ETimeUnit;

public class GetEventsTimeRelativeHandlerTest {
	
	private static RequestHandler handler;
	private static DailyDiary diary;

    @BeforeClass
	public static void setup() {
		handler = new GetEventsTimeRelativeHandler();
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
		testWithSlots(ETimeUnit.YEAR.getValues().get(0), null, 0);
		testWithSlots(ETimeUnit.YEAR.getValues().get(0), "2", 0);
		testWithSlots(ETimeUnit.MONTH.getValues().get(0), "2", 0);
		testWithSlots(ETimeUnit.WEEK.getValues().get(0), "2", 0);
		testWithSlots(ETimeUnit.DAY.getValues().get(0), "2", 0);
		testWithSlots(ETimeUnit.HOUR.getValues().get(0), "2", 0);
		testWithSlots(ETimeUnit.MINUTE.getValues().get(0), "2", 0);
		testWithSlots(ETimeUnit.SECOND.getValues().get(0), "2", 0);

		// Create new event
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ETimeUnit.YEAR, -1)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ETimeUnit.MONTH, -1)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ETimeUnit.WEEK, -1)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ETimeUnit.DAY, -1)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ETimeUnit.HOUR, -1)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ETimeUnit.MINUTE, -1)));
		diary.save(new Event(name, null, null, null, DateTimeExtenstion.getDate(ETimeUnit.SECOND, -1)));
		
        // Test with events	
		testWithSlots(ETimeUnit.YEAR.getValues().get(0), null, 7);
		testWithSlots(ETimeUnit.YEAR.getValues().get(0), "abc", 7);
		testWithSlots(ETimeUnit.MONTH.getValues().get(0), "2", 6);
		testWithSlots(ETimeUnit.WEEK.getValues().get(0), "2", 5);
		testWithSlots(ETimeUnit.DAY.getValues().get(0), "2", 4);
		testWithSlots(ETimeUnit.HOUR.getValues().get(0), "2", 3);
		testWithSlots(ETimeUnit.MINUTE.getValues().get(0), "30", 2);
		testWithSlots(ETimeUnit.SECOND.getValues().get(0), "1800", 2);
    	
    	// Test empty slots
        Response response = TestUtil.slotLessHandlerTest(handler);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(GetEventsTimeRelativeHandler.TXT_NO_EVENTS_FOUND));
	}

	/**
	 * Test handler for given slots
	 * @param unit
	 * @param span
	 * @param countMatch
	 */
	public void testWithSlots(String unit, String span, int countMatch) {
		SlotItem[] slots = {
        		 new SlotItem(GetEventsTimeRelativeHandler.SLOT_UNIT, unit),
        		 new SlotItem(GetEventsTimeRelativeHandler.SLOT_COUNT, span) };
		
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
