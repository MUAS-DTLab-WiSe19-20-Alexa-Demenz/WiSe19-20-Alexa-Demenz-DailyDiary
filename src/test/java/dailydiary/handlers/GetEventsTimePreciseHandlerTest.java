package dailydiary.handlers;

import static org.junit.Assert.assertEquals;
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
import dailydiary.extension.DateTimeExtenstion;
import dailydiary.handlers.TestUtil.SlotItem;
import dailydiary.models.DailyDiary;
import dailydiary.models.Event;

public class GetEventsTimePreciseHandlerTest {
	
	private static RequestHandler handler;
	private static DailyDiary diary;

    @BeforeClass
	public static void setup() {
		handler = new GetEventsTimePreciseHandler();
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
		Date date1 = new Date(946681200000L); // 01 Jan 2000 00:00:00 GMT+0100
		Date date2 = new Date(1577833200000L); // 01 Jan 2020 00:00:00 GMT+0100
		String strDate1 = DateTimeExtenstion.getFormaterDate().format(date1);
		String strDate2 = DateTimeExtenstion.getFormaterDate().format(date2);
    	
        // Test with no events
		testWithSlots(strDate1, strDate2, 0);
		testWithSlots(strDate2, strDate1, 0);

		// Create two new events
		Event event1 = new Event(name, null, null, null, date1);
		diary.save(event1);
		Event event2 = new Event(name, null, null, null, date2);
		diary.save(event2);
		
        // Test with events
		testWithSlots(strDate1, strDate2, 2);
		testWithSlots(strDate2, strDate1, 0);
		
    	// Test empty slots
        Response response = TestUtil.slotLessHandlerTest(handler);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(GetEventsTimePreciseHandler.TXT_NO_EVENTS_FOUND));
	}

	/**
	 * Test handler for given slots
	 * @param startDate
	 * @param endDate
	 * @param countMatch
	 */
	public void testWithSlots(String startDate, String endDate, int countMatch) {
		SlotItem[] slots = {
        		 new SlotItem(GetEventsTimePreciseHandler.SLOT_START_DATE, startDate),
        		 new SlotItem(GetEventsTimePreciseHandler.SLOT_END_DATE, endDate) };
		
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
