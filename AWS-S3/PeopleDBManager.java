import java.io.File;
import java.io.IOException;

import com.amazonaws.auth.*;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.simpledb.*;
import com.amazonaws.services.simpledb.model.*;

public class PeopleDBManager {

	public static final String SimpleDB_Domain_People = "stars-nobels";
    public static final String S3_Bucket_PeopleFiles = "loren-stars-db";
    
    private AmazonS3 s3;
    private AmazonSimpleDB sdb;

    
    public PeopleDBManager(){
        AWSCredentials credentials = null;
        
        try {
        credentials = 
                new PropertiesCredentials(
                		PeopleDBManager.class.getResourceAsStream(
                            "AwsCredentials.properties"));
        }
        catch (IOException e){
        	System.out.println(
                    "We have issues with Credentials entered into AwsCredentials.properties.");
                System.out.println(e.getMessage());
                System.exit(-1);

        }

        this.s3  = new AmazonS3Client(credentials);
        this.sdb = new AmazonSimpleDBClient(credentials);           	
    }

    public void setupStorage(){
    	CreateDomainRequest createPlayersDomainReq;
    	
    	// Create Players domain in SimpleDB.
        createPlayersDomainReq = (new CreateDomainRequest()).withDomainName(SimpleDB_Domain_People);
        // Create Players domain in SimpleDB. 
        this.sdb.createDomain(createPlayersDomainReq);

        // Create the Amazon S3 bucket for player photos
        // bucket names must be unique across all of Amazon S3
        this.s3.createBucket(S3_Bucket_PeopleFiles);
    }
    
    
    // Recursively uploads a files to S3, mimicking the local file structure.
    // When File f is the root directory of the bucket, pathName should be "".
	public void RecursiveUpload (File f, String pathName) {
		if (f.isDirectory() == true) {
			File[] listToUpload = f.listFiles();
			String newPathName  = pathName + f.getName()  + "/";
			for (File nextUp : listToUpload) {
				RecursiveUpload (nextUp, newPathName);
			}
		} else {
			String nextFileName = pathName + f.getName();
			this.s3.putObject(new PutObjectRequest(S3_Bucket_PeopleFiles, nextFileName, f));
			this.s3.setObjectAcl(S3_Bucket_PeopleFiles, nextFileName, CannedAccessControlList.PublicRead);
			System.out.println(nextFileName + " uploaded.");
		}
	}
    
    public void registerStar(String personName, String bestMovie) {  
        // Build an Amazon S3 key for the player photo 
        // from the photos folder name and player's email
        String itemS3Key = personName.replaceAll(" ", "-");        
        
     // Send the player information to Amazon SimpleDB
        // use the email for the item name
        PutAttributesRequest playerAttributesReq = new PutAttributesRequest()
            .withDomainName(SimpleDB_Domain_People)
            .withItemName(itemS3Key);                

        // Add the name as an attribute
        playerAttributesReq.getAttributes().add(
            new ReplaceableAttribute()
                .withName("Name")
                .withValue(personName)
                .withReplace(true)
        );
        
        // Add the best movie as an attribute
        playerAttributesReq.getAttributes().add(
            new ReplaceableAttribute()
                .withName("BestMovie")
                .withValue(bestMovie)
                .withReplace(true)
        );
        
        // Add image
        playerAttributesReq.getAttributes().add(
            new ReplaceableAttribute()
                .withName("Image")
                .withValue("https://s3.amazonaws.com/" + S3_Bucket_PeopleFiles + "/stars/images/" + itemS3Key + ".jpg")
                .withReplace(true)
        );
        
        // Add resume
        playerAttributesReq.getAttributes().add(
            new ReplaceableAttribute()
                .withName("Resume")
                .withValue("https://s3.amazonaws.com/" + S3_Bucket_PeopleFiles + "/stars/resumes/" + itemS3Key + ".doc")
                .withReplace(true)
        );
        
        // Send the put attributes request
        this.sdb.putAttributes(playerAttributesReq);
    	
    }

	public void registerNobel(String personName, String prizeField, String prizeYear) {  
		// Build an Amazon S3 key for the player photo 
		// from the photos folder name and player's email
		String itemS3Key = personName.replaceAll(" ", "-");        
    
		// Send the person's information to Amazon SimpleDB
		PutAttributesRequest playerAttributesReq = new PutAttributesRequest()
			.withDomainName(SimpleDB_Domain_People)
			.withItemName(itemS3Key);                

		// Add the name as an attribute
		playerAttributesReq.getAttributes().add(
			new ReplaceableAttribute()
				.withName("Name")
				.withValue(personName)
				.withReplace(true)
		);
    
		// Add the field as an attribute
		playerAttributesReq.getAttributes().add(
			new ReplaceableAttribute()
				.withName("Field")
				.withValue(prizeField)
				.withReplace(true)
		);

		// Add the year as an attribute
		playerAttributesReq.getAttributes().add(
			new ReplaceableAttribute()
				.withName("YearWon")
				.withValue(prizeYear)
				.withReplace(true)
		);
		
        // Add image
        playerAttributesReq.getAttributes().add(
            new ReplaceableAttribute()
                .withName("Image")
                .withValue("https://s3.amazonaws.com/" + S3_Bucket_PeopleFiles + "/nobels/images/" + itemS3Key + ".jpg")
                .withReplace(true)
        );
        
        // Add resume
        playerAttributesReq.getAttributes().add(
            new ReplaceableAttribute()
                .withName("Resume")
                .withValue("https://s3.amazonaws.com/" + S3_Bucket_PeopleFiles + "/nobels/resumes/" + itemS3Key + ".doc")
                .withReplace(true)
        );
        
        // Send the put attributes request
        this.sdb.putAttributes(playerAttributesReq);
	
	}
}
