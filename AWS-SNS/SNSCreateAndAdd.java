import com.amazonaws.auth.PropertiesCredentials; 
import com.amazonaws.services.sns.AmazonSNS; 
import com.amazonaws.services.sns.AmazonSNSClient; 
import com.amazonaws.services.sns.model.CreateTopicRequest; 
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;



public class SNSCreateAndAdd {
	
	public static void main(String[] args) throws Exception {
		
        AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(         
        	SNSCreateAndAdd.class.getResourceAsStream("AwsCredentials.properties"))); 
 
        // Create a Topic 
        System.out.println("Creating SNS Topic."); 
        CreateTopicRequest createTopicRequest = new CreateTopicRequest().withName("LorenTestTopic");  

        // Retrieve Amazon Resource Name 
        String SNSTopicArn = sns.createTopic(createTopicRequest).getTopicArn(); 
        
      // Subscribe to SNSTopic 
        SubscribeRequest subReq = 
        		new SubscribeRequest(SNSTopicArn, "email","lloren@gmail.com");
        SubscribeResult subRes = sns.subscribe(subReq); 
        SubscribeRequest subReq2 =
        		new SubscribeRequest(SNSTopicArn, "email","etrange_desire@yahoo.com");
        SubscribeResult subRes2 = sns.subscribe(subReq2); 

      System.out.println(SNSTopicArn);
	}
}
