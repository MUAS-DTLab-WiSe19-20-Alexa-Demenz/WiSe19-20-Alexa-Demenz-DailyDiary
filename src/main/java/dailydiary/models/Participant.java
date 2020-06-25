package dailydiary.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

/**
 * A class to create Participants
 * 
 */
@DynamoDBDocument
public class Participant {

	private String name;

	/**
	 * Creates a participant object
	 * 
	 * @param name of the participant
	 */
	public Participant(String name) {
		setName(name);
	}
	
	/**
	 * Creates a participant object
	 * 
	 */
	public Participant() { }

	/**
	 * Getter for the name of Participants
	 * 
	 * @return name of the participant
	 */
    @DynamoDBAttribute(attributeName = "name")
	public String getName() { return this.name; }

	/**
	 * Setter for the name of Participants
	 * 
	 */
	public void setName(String name) { this.name = name == null ? null : name.toLowerCase(); }
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		Participant other = (Participant) obj;

		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;

		return true;
	}
	
	@Override
	public String toString() {
		return "Participant [name=" + name + "]";
	}
}
