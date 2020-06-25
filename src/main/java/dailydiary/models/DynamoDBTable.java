package dailydiary.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import dailydiary.extension.ExceptionExtension;
import dailydiary.linking.DynamoDBAdapter;

/**
 * Abstract class containing the db interaction logic
 * 
 */
public abstract class DynamoDBTable<T extends DynamoDBEntity> {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	protected static final Logger LOGGER = LogManager.getLogger(DynamoDBTable.class);
	
    private final DynamoDBAdapter dbAdapter;
    protected final DynamoDBMapper mapper;
    
    private final String tableName;
    protected final String userId;

    /**
     * Constructor of DynamoDBTable
     * @param tableName
     * @param userId
     */
    public DynamoDBTable(String tableName, String userId) {
        LOGGER.info("Configure mapping to table: " + tableName + " -> " + userId);
        
    	this.tableName = tableName;
    	this.userId = userId;
    	
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(this.tableName))
                .build();
        
        dbAdapter = DynamoDBAdapter.getInstance();
        mapper = dbAdapter.createDbMapper(mapperConfig);
    }
    
    protected abstract List<T> get() throws IOException;
    
    /**
     * Save method for one entity
     * @param entity
     * @return success
     */
    public boolean save(T entity) {
    	List<T> list = new ArrayList<>();
    	list.add(entity);
    	return save(list);
    }
    
    /**
     * Save method for multiple entities
     * @param entities
     * @return success
     */
    public boolean save(List<T> entities) {
    	for(T entity : entities)
    		entity.setUserId(userId);
    	
    	LOGGER.info("Saving entities -> " + entities);
    	
    	try {
        	for(T entity : entities)
        		mapper.save(entity);
    	} catch (Exception e) {
    		LOGGER.error(ExceptionExtension.getStackTrace(e));
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * Delete method for one entity
     * @param entity
     * @return success
     */
    public boolean delete(T entity) {
    	List<T> list = new ArrayList<>();
    	list.add(entity);
    	return delete(list);
    }

    /**
     * Delete method for multiple entities
     * @param entities
     * @return success
     */
    public boolean delete(List<T> entities) {
    	LOGGER.info("Deleting entities -> " + entities);
    	
    	// IDless entity detected...
    	if (entities.stream().filter(x -> x.getId() == null).count() > 0) 
    		LOGGER.error("Deleting entities -> Containing entities without id!");
    	
    	try {
        	for(T entity : entities)
        		mapper.delete(entity);
    	} catch (Exception e) {
    		LOGGER.error(ExceptionExtension.getStackTrace(e));
    		return false;
    	}
    	
    	return true;
    }
}
