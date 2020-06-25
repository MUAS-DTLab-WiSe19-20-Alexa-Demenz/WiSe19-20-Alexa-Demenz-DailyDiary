package dailydiary.linking;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.regions.Regions;

/**
 * Singleton class for the db connection/instance
 * 
 */
public class DynamoDBAdapter {
	
    private static final Logger LOGGER = LogManager.getLogger(DynamoDBAdapter.class);

    private static DynamoDBAdapter dbAdapter = null;
    private final AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    private DynamoDBAdapter() {
		LOGGER.info("Setup database client...");
        // Create the client
        client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();
    }

    public static DynamoDBAdapter getInstance() {
        if (dbAdapter == null) 
        	dbAdapter = new DynamoDBAdapter();

        return dbAdapter;
    }

    public AmazonDynamoDB getDbClient() {
        return client;
    }

    public DynamoDBMapper createDbMapper(DynamoDBMapperConfig mapperConfig) {
        // Create the mapper with the mapper config
        if (client != null)
            mapper = new DynamoDBMapper(client, mapperConfig);

        return mapper;
    }

}
