import java.io.File;
import java.io.IOException;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class S3FolderUpload {

	/**
	 * @param args
	 * 
	 */
	public static void main (String[] args) throws IOException {
		
		String localFolderString = "blackbird";
		String remoteBucket = "lorbucket";
		
		AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(
				S3Sample.class.getResourceAsStream("AwsCredentials.properties")));
		
			
		String localFolderName = "C:\\Users\\Lisa\\workspace\\" + localFolderString + "\\";
		String destBucket = remoteBucket;
		
		File localFolder = new File(localFolderName);
		File[] listToUpload = localFolder.listFiles();
		
		for (File nextUp : listToUpload) {
			String nextFileName = localFolderString + "/" + nextUp.getName();
			s3.putObject(new PutObjectRequest(destBucket, nextFileName, nextUp));
			System.out.println(nextFileName + " uploaded.");
			}
	}

}
