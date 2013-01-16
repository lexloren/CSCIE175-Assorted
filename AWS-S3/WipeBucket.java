import java.io.IOException;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;


public class WipeBucket {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(
				S3Sample.class.getResourceAsStream("AwsCredentials.properties")));
		
		String bucketName = "loren-stars-db";
		
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
        .withBucketName(bucketName));
        
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        	String objKey =  objectSummary.getKey();
        	s3.deleteObject(bucketName, objKey);
        	System.out.println(objKey + " deleted.");
        	}
        
        s3.deleteBucket(bucketName);

	}

}
