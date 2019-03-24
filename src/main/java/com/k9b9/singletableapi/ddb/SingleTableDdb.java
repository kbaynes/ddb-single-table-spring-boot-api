package com.k9b9.singletableapi.ddb;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

/**
 * DynDb
 */
public class SingleTableDdb {

    public AmazonDynamoDB amazonDynamoDbClient;
    public DynamoDB dynamoDb;
    public DynamoDBMapper dynamoDbMapper;
    // I think this can be replaced with DynamoDBMapperConfig.DEFAULT
    // public DynamoDBMapperConfig dynamoDbMapperConfig;
    public DynamoDBMapperConfig ddbCfgDefault = DynamoDBMapperConfig.DEFAULT;
    public Table table;

    /**
     * Connects using explicit credentials
     */
    public SingleTableDdb(String awsAccessKey, String awsSecretKey, String awsRegion, String tableName) {

        this.amazonDynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .withRegion(awsRegion).build();

        this.dynamoDb = new DynamoDB(this.amazonDynamoDbClient);

        this.dynamoDbMapper = new DynamoDBMapper(this.amazonDynamoDbClient);

        // this.dynamoDbMapperConfig = DynamoDBMapperConfig.builder()
        //         .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT).build();

    }

    /**
     * Connects using Default AWS Credentials (~/.aws/credentials) and region (~/.aws/config),
     * using the DefaultAWSCredentialsProviderChain and DefaultAwsRegionProviderChain chain
     * Table name is specified on the Entity @DynamoDBTable
     */
    public SingleTableDdb(String tableName) {

        this.amazonDynamoDbClient = AmazonDynamoDBClientBuilder.defaultClient();

        this.dynamoDb = new DynamoDB(this.amazonDynamoDbClient);

        this.dynamoDbMapper = new DynamoDBMapper(this.amazonDynamoDbClient);

        this.useTable(tableName);

        // this.dynamoDbMapperConfig = DynamoDBMapperConfig.builder()
        //         .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
        //         .build();
    }

    /**
     * Sets the table to use
     */
    public Table useTable(String tableName) {
        this.table = this.dynamoDb.getTable(tableName);
        return getTable();
    }

    /**
     * Gets the target table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Puts an item in the default Table
     */
    public PutItemOutcome putItem(Item item) {
        PutItemOutcome pio = this.table.putItem(item);
        return pio;
    }
}