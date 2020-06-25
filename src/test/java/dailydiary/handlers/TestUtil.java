package dailydiary.handlers;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.mockito.Mockito;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Intent.Builder;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

public class TestUtil {

    public static HandlerInput mockHandlerInput(Map<String, Object> sessionAttributes,
            Map<String, Object> persistentAttributes, Map<String, Object> requestAttributes) {
        final AttributesManager attributesManagerMock = Mockito.mock(AttributesManager.class);
        when(attributesManagerMock.getSessionAttributes()).thenReturn(sessionAttributes);
        when(attributesManagerMock.getPersistentAttributes()).thenReturn(persistentAttributes);
        when(attributesManagerMock.getRequestAttributes()).thenReturn(requestAttributes);

        // Mock Slots
        final RequestEnvelope requestEnvelopeMock = RequestEnvelope.builder()
                .withRequest(IntentRequest.builder().withIntent(Intent.builder().build()).build()).build();

        // Mock Handler input attributes
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        when(input.getAttributesManager()).thenReturn(attributesManagerMock);
        when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(input.getRequestEnvelope()).thenReturn(requestEnvelopeMock);

        return input;
    }

    /**
     * Function to test handler with slots and test if response contains specific
     * String.
     * 
     * @param handler          The handler that should the request be tested on
     * @return The response from the handler
     */
    public static Response slotLessHandlerTest(RequestHandler handler) {
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final HandlerInput inputMock = TestUtil.mockHandlerInput(sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotEquals("Test", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
        return response;
    }

    /**
     * Class to define slots with name and value to be used for testWithSlot
     * function.
     */
    public static class SlotItem {
    	
        /**
         * Name of the Slot.
         */
        private String name;
        
        /**
         * Value of the Slot.
         */
        private String value;

        /**
         * Custom-Constructor.
         * 
         * @param name
         * @param value
         */
        public SlotItem(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public String getValue() { return value; }
    }

    /**
     * Function to test handler with slots and test if response contains specific
     * String.
     * 
     * @param handler          The handler that should the request be tested on
     * @param slots            The slots to pass to the handler
     * @param responseContains The String that should be contained by the response.
     * @return The response from the handler
     */
    public static Response testWithSlot(RequestHandler handler, SlotItem[] slots, String responseContains) {
        Builder builder = Intent.builder();
        
        for (SlotItem slotItem : slots)
            builder.putSlotsItem(
            		slotItem.getName(),
            		Slot.builder().withName(slotItem.getName()).withValue(slotItem.getValue()).build());

        final RequestEnvelope requestEnvelopeMock = 
        		RequestEnvelope.builder().withRequest(IntentRequest.builder().withIntent(builder.build()).build()).build();
        
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(input.getRequestEnvelope()).thenReturn(requestEnvelopeMock);

        final Optional<Response> res = handler.handle(input);
        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotNull(response.getOutputSpeech());
        String responseText = response.getOutputSpeech().toString().split("<speak>")[1].split("</speak>")[0];
        
        if (responseContains != null)
        	assertTrue(responseText.contains(responseContains));
        
        assertTrue(response.getCard().toString().contains(DailyDiaryRequestHandler.RESPONSE_CARD_TITLE));
        return response;
    }
}