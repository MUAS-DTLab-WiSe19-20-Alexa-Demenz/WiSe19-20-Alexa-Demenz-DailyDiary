package dailydiary;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.handler.GenericRequestHandler;

// Handlers
import dailydiary.handlers.*;
import dailydiary.handlers.builtin.CancelAndStopIntentHandler;
import dailydiary.handlers.builtin.FallbackIntentHandler;
import dailydiary.handlers.builtin.HelpIntentHandler;
import dailydiary.handlers.builtin.LaunchRequestHandler;
import dailydiary.handlers.builtin.SessionEndedRequestHandler;

/*
 * SkillStreamHandler of the project
 */
public class DailyDiaryStreamHandler extends SkillStreamHandler {
	
	private static final Random random = new Random();
	
	public static final String SKILL_NAME = "Daily Diary";
	public static final String DATABASE_TABLE_NAME = "DailyDiary-Data-Development";
	public static final String REQUEST_DEFAULT_USER_ID = "DEFAULT_USER_ID_" + random.nextInt();
	
	private static Skill getSkill() {
		
		List<GenericRequestHandler<HandlerInput, Optional<Response>>> handlers = Arrays.asList( 
				new AddEventInaccurateHandler(), 
		        new AddEventPreciseHandler(), 
		        new AddEventRoutineHandler(), 
		        new GetEventsNameHandler(), 
		        new GetEventsLocationHandler(), 
		        new GetEventsParticipantHandler(), 
		        new GetEventsTimeInaccurateHandler(), 
		        new GetEventsTimePreciseHandler(), 
		        new GetEventsTimeRelativeHandler(), 
		        new GetLastEventHandler(), 
		        new LaunchRequestHandler(), 
		        new CancelAndStopIntentHandler(), 
		        new SessionEndedRequestHandler(), 
		        new HelpIntentHandler(), 
		        new FallbackIntentHandler());
		
	   return Skills.standard()
	           .addRequestHandlers(handlers)
	           .withTableName(DATABASE_TABLE_NAME)
	           .withAutoCreateTable(true)
	           // Add your skill id below
	           //.withSkillId("")
	           .build();
	}
	
	public DailyDiaryStreamHandler() {
	   super(getSkill());
	}
}
