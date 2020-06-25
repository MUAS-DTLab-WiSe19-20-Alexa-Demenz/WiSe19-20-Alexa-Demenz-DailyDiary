package dailydiary.models.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlainTimeTest {

	@Test
	public void testPlainTime() {
		PlainTime time = new PlainTime(24, 30);
		assertNotNull(time);
	}

	@Test
	public void testGetter() {
		PlainTime time = new PlainTime(24, 30);
		assertEquals(24, time.getHour());
		assertEquals(30, time.getMinute());
	}

	@Test
	public void testToValue() {
		PlainTime time = new PlainTime(24, 30);
		assertEquals("24:30", time.toValue());
	}

	@Test
	public void testConvert() {
		assertEquals("03:00", PlainTime.convert("NI").toValue());
		assertEquals("08:00", PlainTime.convert("MO").toValue());
		assertEquals("15:00", PlainTime.convert("AF").toValue());
		assertEquals("20:00", PlainTime.convert("EV").toValue());
		assertEquals("08:00", PlainTime.convert("08:00:00").toValue());
		assertEquals("20:00", PlainTime.convert("20:00:00").toValue());
		assertEquals("08:00", PlainTime.convert("08:00").toValue());
		assertEquals("20:00", PlainTime.convert("20:00").toValue());
		
		try {
			PlainTime.convert("NULL").toValue();
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
}
