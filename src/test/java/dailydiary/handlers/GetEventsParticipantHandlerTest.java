package dailydiary.handlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import dailydiary.models.Participant;

public class GetEventsParticipantHandlerTest {
	
	private static RequestHandler handler;
	private static DailyDiary diary;

    @BeforeClass
	public static void setup() {
		handler = new GetEventsParticipantHandler();
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
    	String name = "TestName".toLowerCase();
    	String personName1 = "Franz".toLowerCase();
    	String personName2 = "Hans".toLowerCase();
    	
        // Test with no events
		testWithSlots(personName1, false);
		testWithSlots(personName2, false);
		
		// Create two new events
		Participant person1 = new Participant(personName1);
		Participant person2 = new Participant(personName2);
		List<Participant> list1 = new ArrayList<>();
		List<Participant> list2 = new ArrayList<>();
		list1.add(person1);
		list2.add(person2);
		Event event1 = new Event(name, null, null, list1, null);
		Event event2 = new Event(name, null, null, list2, null);
		diary.save(event1);
		diary.save(event2);

        // Test with events
		testWithSlots(personName1, true);
		testWithSlots(personName2, true);
    	
    	// Test empty slots
        Response response = TestUtil.slotLessHandlerTest(handler);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(GetEventsParticipantHandler.TXT_NO_EVENTS_FOUND));
	}

	/**
	 * Test handler for given slots
	 * @param personName
	 * @param contains
	 */
	public void testWithSlots(String personName, boolean contains) {
		SlotItem[] slots = { new SlotItem(GetEventsParticipantHandler.SLOT_PARTICIPANT, personName) };
		Response response = TestUtil.testWithSlot(handler, slots, null);
		String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
		
        if (contains) {
        	assertFalse(responseText.contains(GetEventsParticipantHandler.TXT_NO_EVENTS_FOUND));
        } else {
        	assertTrue(responseText.contains(GetEventsParticipantHandler.TXT_NO_EVENTS_FOUND));
        }
	}

    @AfterClass
	public static void clean() {
		List<Event> events = diary.getEvents();
		diary.delete(events);
	}
}
