package dailydiary.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParticipantTest {

	@Test
	public void testName() {
		String input = "name";
		Participant p1 = new Participant(input);
		Participant p2 = new Participant();
		p2.setName(input);
		assertEquals(input, p1.getName());
		assertEquals(input, p2.getName());
	}
	
	@Test
	public void testHashCode() {
		String input = "name";
		Participant p1 = new Participant(input);
		Participant p2 = new Participant(null);
		int hashcode = p1.hashCode();
		assertNotEquals(hashcode, p2.hashCode());
		p2.setName(input);
		assertEquals(hashcode, p2.hashCode());
	}
	
	@Test
	public void testEquals() {
		String input = "name";
		Participant p1 = new Participant(null);
		Participant p2 = new Participant(null);
		assertFalse(p1.equals(null));
		assertFalse(p1.equals(new Object()));
		assertTrue(p1.equals(p1));
		assertTrue(p1.equals(p2));
		
		p1.setName(input);
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
		p2.setName(input);
		assertTrue(p1.equals(p2));
	}
	
	@Test
	public void testToString() {
		String input = "name";
		Participant p = new Participant(input);
		assertEquals("Participant [name=" + input + "]", p.toString());
	}
}
