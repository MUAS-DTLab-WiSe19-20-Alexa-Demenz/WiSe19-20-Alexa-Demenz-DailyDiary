package dailydiary.models;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dailydiary.DailyDiaryStreamHandler;
public class DailyDiaryTest {

    private static Event event1;
    private static Event event2;
    private static DailyDiary diary;

    @BeforeClass
    public static void setup() {
        Participant participant1 = new Participant("Klaus");
        List<Participant> participants1 = new ArrayList<>();
        participants1.add(participant1);
        Date date1 = new Date(10000);
        event1 = new Event("Einkaufen", "Tomaten kaufen", "OEZ", participants1, date1);

        Participant participant2 = new Participant("Möre");
        List<Participant> participants2 = new ArrayList<>();
        participants2.add(participant2);
        Date date2 = new Date(20000);
        event2 = new Event("Party", "Geburtstagsfeier Möre", "Munich", participants2, date2);

        diary = new DailyDiary(DailyDiaryStreamHandler.REQUEST_DEFAULT_USER_ID);
        diary.save(event1);
        diary.save(event2);
    }

    @Test
    public void testGetLastEvent() throws IOException {
        assertEquals(event2, diary.getLastEvent());
    }

    @Test
    public void testGetEvents() throws IOException {
        assertEquals(2, diary.getEvents().size());
        assertEquals(2, diary.getEvents(null, null, null, null, null, null).size());
        assertEquals(1, diary.getEvents("Einkaufen", null, null, null, null, null).size());
        assertEquals(1, diary.getEvents(null, "Tomaten", null, null, null, null).size());
        assertEquals(1, diary.getEvents(null, null, "Munich", null, null, null).size());
        assertEquals(2, diary.getEvents(null, null, null, new Date(10000), null, null).size());
        assertEquals(1, diary.getEvents(null, null, null, new Date(20000), null, null).size());
        assertEquals(0, diary.getEvents(null, null, null, new Date(20001), null, null).size());
        assertEquals(0, diary.getEvents(null, null, null, null, new Date(10000), null).size());
        assertEquals(1, diary.getEvents(null, null, null, null, new Date(20000), null).size());
        assertEquals(2, diary.getEvents(null, null, null, null, new Date(20001), null).size());
        assertEquals(1, diary.getEvents(null, null, null, null, null, event1.getParticipants()).size());
    }

    @AfterClass
    public static void clean() {
        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        diary.delete(events);
    }
}
