package dailydiary.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.text.ParseException;
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
import dailydiary.models.type.PlainDate;
import dailydiary.models.type.PlainTime;

/**
 * Test for the AddEventPreciseHandler.
 */
public class AddEventPreciseHandlerTest {
	
    private static RequestHandler handler;
    private static DailyDiary diary;

    @BeforeClass
    public static void setup() {
        handler = new AddEventPreciseHandler();
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
     * @throws ParseException 
     */
    @Test
    public void testHandle() throws ParseException {
    	PlainDate pdate = new PlainDate(2019, 12, 31);
    	PlainTime ptime = new PlainTime(20, 0);
    	Date date = DateTimeExtenstion.getDate(pdate, ptime);
		
        testWithSlots("TestName", "TestBeschreibung", "TestOrt", "TestPerson1 TestPerson2", pdate.toValue(), ptime.toValue(), date);
        
        Response response = TestUtil.slotLessHandlerTest(handler);
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        assertTrue(responseText.contains(AddEventPreciseHandler.TXT_SAY_REPROMT));
        
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
     * @param date
     * @param time
     * @param compareDate
     */
    public void testWithSlots(String name, String description, String location, String participants, String date, String time, Date compareDate) {
    	SlotItem[] slots = {
       		 new SlotItem(AddEventPreciseHandler.SLOT_NAME, name),
       		 new SlotItem(AddEventPreciseHandler.SLOT_DESCRIPTION, description) ,
       		 new SlotItem(AddEventPreciseHandler.SLOT_LOCATION, location) ,
       		 new SlotItem(AddEventPreciseHandler.SLOT_PARTICIPANTS, participants),
       		 new SlotItem(AddEventPreciseHandler.SLOT_DATE, date) ,
       		 new SlotItem(AddEventPreciseHandler.SLOT_TIME, time) };
    	
    	Response response = TestUtil.testWithSlot(handler, slots, null);
    	String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
    	assertTrue(responseText.contains(AddEventInaccurateHandler.TXT_EVENT_CREATED.substring(0, AddEventPreciseHandler.TXT_EVENT_CREATED.indexOf('%'))));
        
        Event event = diary.getEvents(name, null, null, null, null, null).get(0);
        assertEquals(event.getDate(), compareDate);
    }

    @AfterClass
    public static void clean() {
        List<Event> events = diary.getEvents();
        diary.delete(events);
    }
}
