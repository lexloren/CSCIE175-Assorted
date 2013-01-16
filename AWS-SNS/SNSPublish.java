import com.amazonaws.services.sns.model.PublishRequest; 
import com.amazonaws.AmazonClientException; 
import com.amazonaws.AmazonServiceException; 
import com.amazonaws.auth.PropertiesCredentials; 
import com.amazonaws.services.sns.AmazonSNS; 
import com.amazonaws.services.sns.AmazonSNSClient; 
 
public class SNSPublish { 
  public static void main(String[] args)throws Exception { 
   AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(  
     
SNSPublish.class.getResourceAsStream("AwsCredentials.properties"))); 
   
   try { 
	   String myTopicArn = "arn:aws:sns:us-east-1:034307772076:LorenTestTopic";         
	   sns.publish(new PublishRequest() 
	   		.withTopicArn(myTopicArn) 
	   		.withMessage("This is my message to"  + myTopicArn) 
	   		.withSubject("Message sent to " + myTopicArn)); 
   } catch (AmazonServiceException ase) { 
   } catch (AmazonClientException ace) { 
   } 
} 
} 
   
   