package dailydiary.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

/**
 * Test for the AddEventPreciseHandler.
 */
public class AddEventRoutineHandlerTest {
	
    private static RequestHandler handler;
    private static DailyDiary diary;

    @BeforeClass
    public static void setup() {
        handler = new AddEventRoutineHandler();
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
        testWithSlots("TestName", "TestBeschreibung", "TestOrt", "TestPerson1 TestPerson2");
        
        Response response = TestUtil.slotLessHandlerTest(handler);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(AddEventRoutineHandler.TXT_SAY_REPROMT));
        
        Event eventLast = diary.getLastEvent();
        assertEquals("testname", eventLast.getName());
        diary.delete(eventLast);
        Event eventNull = diary.getLastEvent();
        assertNull(eventNull);
    }

    /**
     * Test handler for given slots
     * @param name
     * @param description
     * @param location
     * @param participants
     */
    public void testWithSlots(String name, String description, String location, String participants) {
    	SlotItem[] slots = {
       		 new SlotItem(AddEventRoutineHandler.SLOT_NAME, name),
       		 new SlotItem(AddEventRoutineHandler.SLOT_DESCRIPTION, description) ,
       		 new SlotItem(AddEventRoutineHandler.SLOT_LOCATION, location) ,
       		 new SlotItem(AddEventRoutineHandler.SLOT_PARTICIPANTS, participants) };
    	
    	Response response = TestUtil.testWithSlot(handler, slots, null);
    	String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
    	assertTrue(responseText.contains(AddEventRoutineHandler.TXT_EVENT_CREATED.substring(0, AddEventRoutineHandler.TXT_EVENT_CREATED.indexOf('%'))));
        
        Event event = diary.getEvents(name, null, null, null, null, null).get(0);
        assertTrue(event.getDate().before(new Date(System.currentTimeMillis())));
        assertTrue(event.getDate().after(new Date(System.currentTimeMillis() - 100000)));
    }

    @AfterClass
    public static void clean() {
        List<Event> events = diary.getEvents();
        diary.delete(events);
    }
}
