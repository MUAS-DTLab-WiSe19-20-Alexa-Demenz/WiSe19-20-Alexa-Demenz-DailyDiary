package dailydiary.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import dailydiary.extension.DateTimeExtenstion;

public class EventTest {
	
	private static final String EVENT_NAME = "Einkaufen".toLowerCase();
	private static final String EVENT_DESC = "Tomaten einkaufen".toLowerCase();
	private static final String EVENT_LOCA = "OEZ".toLowerCase();
	private static final List<Participant> PARTICIPANTS = Arrays.asList(new Participant[] { new Participant("name")});
	private static final Date DATE = new Date();
	
	@Test
	public void testName() {
		Event event = new Event();
		event.setName(EVENT_NAME);
		assertEquals(EVENT_NAME, event.getName());
	}

	@Test
	public void testDescription() {
		Event event = new Event();
		event.setDescription(EVENT_DESC);
		assertEquals(EVENT_DESC, event.getDescription());
	}

	@Test
	public void testLocation() {
		Event event = new Event();
		event.setLocation(EVENT_LOCA);
		assertEquals(EVENT_LOCA, event.getLocation());
	}

	@Test
	public void testParticipants() {
		Event event = new Event();
		event.setParticipants(PARTICIPANTS);
		assertEquals(PARTICIPANTS, event.getParticipants());
	}

	@Test
	public void testDate() {
		Event event = new Event();
		event.setDate(DATE);
		assertEquals(DATE, event.getDate());
	}
	
	@Test
	public void testHashCode() {
		Event event1 = new Event(EVENT_NAME, EVENT_DESC, EVENT_LOCA, PARTICIPANTS, DATE);
		Event event2 = new Event(null, null, null, null, null);
		int hashcode = event1.hashCode();
		assertNotEquals(hashcode, event2.hashCode());
		event2.setName(EVENT_NAME);
		event2.setDescription(EVENT_DESC);
		event2.setLocation(EVENT_LOCA);
		event2.setParticipants(PARTICIPANTS);
		event2.setDate(DATE);
		assertEquals(hashcode, event2.hashCode());
	}
	
	@Test
	public void testEquals() {
		Event event1 = new Event(null, null, null, null, null);
		Event event2 = new Event(null, null, null, null, null);
		assertFalse(event1.equals(null));
		assertFalse(event1.equals(new Object()));
		assertTrue(event1.equals(event1));
		assertTrue(event1.equals(event2));
		
		event1.setName(EVENT_NAME);
		assertFalse(event1.equals(event2));
		assertFalse(event2.equals(event1));
		event2.setName(EVENT_NAME);
		assertTrue(event1.equals(event2));
		
		event1.setDescription(EVENT_DESC);
		assertFalse(event1.equals(event2));
		assertFalse(event2.equals(event1));
		event2.setDescription(EVENT_DESC);
		assertTrue(event1.equals(event2));
		
		event1.setLocation(EVENT_LOCA);
		assertFalse(event1.equals(event2));
		assertFalse(event2.equals(event1));
		event2.setLocation(EVENT_LOCA);
		assertTrue(event1.equals(event2));
		
		event1.setParticipants(PARTICIPANTS);
		assertFalse(event1.equals(event2));
		assertFalse(event2.equals(event1));
		event2.setParticipants(PARTICIPANTS);
		assertTrue(event1.equals(event2));
		
		event1.setDate(DATE);
		assertFalse(event1.equals(event2));
		assertFalse(event2.equals(event1));
		event2.setDate(DATE);
		assertTrue(event1.equals(event2));
	}

	@Test
	public void testToString() {
		Event event = new Event(EVENT_NAME, EVENT_DESC, EVENT_LOCA, PARTICIPANTS, DATE);
		String input = event.toString();
		String output = "Event [name=" + EVENT_NAME
			+ ", description=" + EVENT_DESC
			+ ", location=" + EVENT_LOCA
			+ ", participants=" + PARTICIPANTS
			+ ", date=" + DATE + "]";
		
		assertEquals(input, output);
	}

	@Test
	public void testConstructor() {
		Event e = new Event();
		e.setName(EVENT_NAME);
		assertEquals(EVENT_NAME, e.getName());
	}

	@Test
	public void testToSpeech() throws ParseException {
		List<Participant> p = new ArrayList<>();
		String dd = DateTimeExtenstion.getFormaterDate().format(DATE);
		String dt = DateTimeExtenstion.getFormaterTime().format(DATE);
		
		Event event = new Event(null, null, null, null, null);
		
		assertEquals(String.format("Der Eintrag %s fand am %s um %s statt.", 
					null, 
					DateTimeExtenstion.getFormaterDate().format(new Date(0)), 
					DateTimeExtenstion.getFormaterTime().format(new Date(0))), 
				event.toSpeech());
		
		event.setName(EVENT_NAME);
		event.setDescription("");
		event.setLocation("");
		event.setParticipants(p);
		event.setDate(DATE);
		
		assertEquals(String.format("Der Eintrag %s fand am %s um %s statt.", 
					EVENT_NAME, 
					dd, 
					dt), 
				event.toSpeech());
		
		event.setDescription(EVENT_DESC);
		event.setLocation(EVENT_LOCA);
		
		assertEquals(String.format("Der Eintrag %s fand am %s um %s in %s statt. Die Beschreibung lautet %s.", 
					EVENT_NAME, 
					dd, 
					dt, 
					EVENT_LOCA, 
					EVENT_DESC), 
				event.toSpeech());
		
		p.add(new Participant(PARTICIPANTS.get(0).getName()));
		
		assertEquals(String.format("Der Eintrag %s fand am %s um %s in %s statt. Teilgenommen haben %s. Die Beschreibung lautet %s.", 
				EVENT_NAME, 
				dd, 
				dt, 
				EVENT_LOCA, 
				p.get(0).getName(),
				EVENT_DESC), 
			event.toSpeech());
		
		p.add(new Participant(PARTICIPANTS.get(0).getName()));
		
		assertEquals(String.format("Der Eintrag %s fand am %s um %s in %s statt. Teilgenommen haben %s und %s. Die Beschreibung lautet %s.", 
				EVENT_NAME, 
				dd, 
				dt, 
				EVENT_LOCA, 
				p.get(0).getName(),
				p.get(1).getName(),
				EVENT_DESC), 
			event.toSpeech());
		
		p.add(new Participant(PARTICIPANTS.get(0).getName()));
		
		assertEquals(String.format("Der Eintrag %s fand am %s um %s in %s statt. Teilgenommen haben %s, %s und %s. Die Beschreibung lautet %s.", 
				EVENT_NAME, 
				dd, 
				dt, 
				EVENT_LOCA, 
				p.get(0).getName(),
				p.get(1).getName(),
				p.get(2).getName(),
				EVENT_DESC), 
			event.toSpeech());
	}
}
