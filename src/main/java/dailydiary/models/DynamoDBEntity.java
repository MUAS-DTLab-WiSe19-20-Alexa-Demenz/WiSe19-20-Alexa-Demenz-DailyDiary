package dailydiary.models;

/**
 * Abstract model for a db entity
 * 
 */
public abstract class DynamoDBEntity {
	
	private String userId;
	private String id;
	
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
}
