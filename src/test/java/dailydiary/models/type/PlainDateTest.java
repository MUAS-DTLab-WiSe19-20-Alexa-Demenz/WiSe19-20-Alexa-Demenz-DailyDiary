package dailydiary.models.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlainDateTest {

	@Test
	public void testPlainDate() {
		PlainDate date = new PlainDate(2000, 1, 31);
		assertNotNull(date);
	}

	@Test
	public void testGetter() {
		PlainDate date = new PlainDate(2000, 1, 31);
		assertEquals(2000, date.getYear());
		assertEquals(1, date.getMonth());
		assertEquals(31, date.getDay());
	}

	@Test
	public void testToValue() {
		PlainDate date = new PlainDate(2000, 1, 31);
		assertEquals("2000-01-31", date.toValue());
	}

	@Test
	public void testConvert() {
		// ....-SP or ....-SU or ....-FA or ....-WI
		assertEquals("2000-03-01", PlainDate.convert("2000-SP").toValue());
		assertEquals("2000-06-01", PlainDate.convert("2000-SU").toValue());
		assertEquals("2000-09-01", PlainDate.convert("2000-FA").toValue());
		assertEquals("2000-12-01", PlainDate.convert("2000-WI").toValue());
		// ...X-XX-XX
		assertEquals("1990-01-01", PlainDate.convert("199X-XX-XX").toValue());
		assertEquals("2000-01-01", PlainDate.convert("200X-XX-XX").toValue());
		// ....-XX-XX
		assertEquals("1995-01-01", PlainDate.convert("1995-XX-XX").toValue());
		assertEquals("2000-01-01", PlainDate.convert("2000-XX-XX").toValue());
		// ....-..-XX
		assertEquals("2000-01-01", PlainDate.convert("2000-01-XX").toValue());
		assertEquals("2000-06-01", PlainDate.convert("2000-06-XX").toValue());
		assertEquals("2000-12-01", PlainDate.convert("2000-12-XX").toValue());
		// ....-..-..
		assertEquals("2000-01-31", PlainDate.convert("2000-01-31").toValue());
		assertEquals("2000-12-31", PlainDate.convert("2000-12-31").toValue());
		// ....-W.. or ....-W..-WE
		assertEquals("2020-01-06", PlainDate.convert("2020-W2").toValue());
		assertEquals("2020-01-06", PlainDate.convert("2020-W02").toValue());
		assertEquals("2020-01-06", PlainDate.convert("2020-W2-WE").toValue());
		assertEquals("2020-01-06", PlainDate.convert("2020-W02-WE").toValue());
		assertEquals("2019-12-30", PlainDate.convert("2020-W1").toValue());
		assertEquals("2019-12-30", PlainDate.convert("2020-W01").toValue());
		assertEquals("2019-12-30", PlainDate.convert("2020-W1-WE").toValue());
		assertEquals("2019-12-30", PlainDate.convert("2020-W01-WE").toValue());
		
		try {
			PlainDate.convert("NULL").toValue();
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

}
