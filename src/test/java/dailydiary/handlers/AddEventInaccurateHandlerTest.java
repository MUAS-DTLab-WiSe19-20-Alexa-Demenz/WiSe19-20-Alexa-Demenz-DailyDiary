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
import dailydiary.extension.DateTimeExtenstion;
import dailydiary.handlers.TestUtil.SlotItem;
import dailydiary.models.DailyDiary;
import dailydiary.models.Event;
import dailydiary.models.type.EDayOfWeek;
import dailydiary.models.type.PlainTime;

/**
 * Test for the AddEventPreciseHandler.
 */
public class AddEventInaccurateHandlerTest {
	
    private static RequestHandler handler;
    private static DailyDiary diary;

    @BeforeClass
    public static void setup() {
        handler = new AddEventInaccurateHandler();
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
    	PlainTime ptime = new PlainTime(20, 0);
    	Date date = DateTimeExtenstion.getDate(EDayOfWeek.MONDAY, ptime);
        testWithSlots("TestName", "TestBeschreibung", "TestOrt", "TestPerson1 TestPerson2", EDayOfWeek.MONDAY.getValue(), ptime.toValue(), date);
        
        Response response = TestUtil.slotLessHandlerTest(handler);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(AddEventInaccurateHandler.TXT_SAY_REPROMT));
        
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
     * @param dayOfWeek
     * @param time
     * @param compareDate
     */
    public void testWithSlots(String name, String description, String location, String participants, String dayOfWeek, String time, Date compareDate) {
    	SlotItem[] slots = {
       		 new SlotItem(AddEventInaccurateHandler.SLOT_NAME, name),
       		 new SlotItem(AddEventInaccurateHandler.SLOT_DESCRIPTION, description) ,
       		 new SlotItem(AddEventInaccurateHandler.SLOT_LOCATION, location) ,
       		 new SlotItem(AddEventInaccurateHandler.SLOT_PARTICIPANTS, participants),
       		 new SlotItem(AddEventInaccurateHandler.SLOT_DAYOFWEEK, dayOfWeek) ,
       		 new SlotItem(AddEventInaccurateHandler.SLOT_TIME, time) };
    	
    	Response response = TestUtil.testWithSlot(handler, slots, null);
    	String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(AddEventInaccurateHandler.TXT_EVENT_CREATED.substring(0, AddEventInaccurateHandler.TXT_EVENT_CREATED.indexOf('%'))));
        
        Event event = diary.getEvents(name, null, null, null, null, null).get(0);
        assertEquals(event.getDate().toString(), compareDate.toString());
    }

    @AfterClass
    public static void clean() {
        List<Event> events = diary.getEvents();
        diary.delete(events);
    }
}
