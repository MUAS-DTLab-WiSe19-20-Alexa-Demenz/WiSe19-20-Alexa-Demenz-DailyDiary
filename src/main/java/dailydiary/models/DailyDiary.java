package dailydiary.models;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import dailydiary.DailyDiaryStreamHandler;

/**
 * A class to create a Daily Diary that manages events
 * 
 */
public class DailyDiary extends DynamoDBTable<Event> {

	/**
	 * Constructor for a Daily Diary
	 * 
	 */
	public DailyDiary(String userId) {
		super(DailyDiaryStreamHandler.DATABASE_TABLE_NAME, userId);
	}

	/**
	 * Getter for the last event
	 * 
	 * @return the last event
	 */
	public Event getLastEvent() {
		List<Event> events = get();
		return events.stream().max(Comparator.comparing(Event::getDate)).orElse(null);
	}

	/**
	 * A filter for certain parameters of the Daily Diary and the used classes
	 * "Event" and "Participant"
	 * 
	 * @param filterName
	 * @param filterDescription
	 * @param filterLocation
	 * @param filterDateStart (include)
	 * @param filterDateStop (exclude)
	 * @param filterParticipant
	 * @return
	 */
	public List<Event> getEvents(
			String filterName, 
			String filterDescription, 
			String filterLocation,
			Date filterDateStart, 
			Date filterDateStop, 
			List<Participant> filterParticipant) {
		return get().stream().
				filter(x -> filterName == null || x.getName().contains(filterName.toLowerCase()))
				.filter(x -> filterDescription == null || (x.getDescription() != null && x.getDescription().contains(filterDescription.toLowerCase())))
				.filter(x -> filterLocation == null || (x.getLocation() != null && x.getLocation().contains(filterLocation.toLowerCase())))
				.filter(x -> filterDateStart == null || !x.getDate().before(filterDateStart))
				.filter(x -> filterDateStop == null || x.getDate().before(filterDateStop))
				.filter(x -> filterParticipant == null || x.getParticipants().stream().anyMatch(filterParticipant::contains))
				.collect(Collectors.toList());
	}

	/**
	 * Getter for all events
	 * 
	 * @return events
	 */
	public List<Event> getEvents() {
		return get();
	}

	@Override
	protected List<Event> get() {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":val1", new AttributeValue().withS(userId));

		DynamoDBScanExpression scanExp = new DynamoDBScanExpression()
				.withFilterExpression("userId = :val1")
				.withExpressionAttributeValues(eav);

		List<Event> list = new LinkedList<>();
		mapper.scan(Event.class, scanExp).stream().forEach(list::add);
		LOGGER.info("Getting entities -> " + list);
		return list;
	}
}
