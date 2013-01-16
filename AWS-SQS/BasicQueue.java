import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class BasicQueue {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		AmazonSQS sqs = new AmazonSQSClient(new PropertiesCredentials(
                SimpleQueueServiceSample.class.getResourceAsStream("AwsCredentials.properties")));

		
		 //Build a map of attributes to set visibility timeout to 20 seconds
		 Map<String, String> attr = new HashMap<String,String>();

		 //Set Message visibility to 20 seconds.
		 attr.put("VisibilityTimeout", "20");

		//Build queue request
		 CreateQueueRequest createQueueRequest =
		     new CreateQueueRequest("OurQueue").withAttributes(attr).withQueueName("LorenTestQueue");
        
        String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		
        System.out.println("Sending message 1.");
        sqs.sendMessage(new SendMessageRequest(myQueueUrl, "First message."));
        
        System.out.println("Receiving messages:");
        List<Message> messages = checkPrintMessages(myQueueUrl, sqs);
        
        System.out.println("Deleting message.");
        String messageRecieptHandle = messages.get(0).getReceiptHandle();
        sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageRecieptHandle));
		
        System.out.println("Sending message 2.");
        sqs.sendMessage(new SendMessageRequest(myQueueUrl, "Second message."));
        
        System.out.println("Checking message 2 for the first time:");
        checkPrintMessages(myQueueUrl, sqs);
		
		System.out.println("Waiting 30 seconds....");
		Thread.sleep(30000);
		
        System.out.println("Checking message 2s:");
        checkPrintMessages(myQueueUrl, sqs);
        
        System.out.println("Sending more messages.");
        sqs.sendMessage(new SendMessageRequest(myQueueUrl, "Third message."));
        sqs.sendMessage(new SendMessageRequest(myQueueUrl, "Fourth message."));
        
        System.out.println("Fetching queue attributes:");
        GetQueueAttributesRequest getQARequest = new GetQueueAttributesRequest()
        	.withQueueUrl(myQueueUrl)
        	.withAttributeNames("All"); 
        GetQueueAttributesResult getQAResult =  sqs.getQueueAttributes(getQARequest);  
        Map<String,String> attributeMap = getQAResult.getAttributes();
        for(String key : attributeMap.keySet()) {
        	   System.out.print(key + " : ");
        	   System.out.println(attributeMap.get(key)); 
        	}
        
        System.out.println("Deleting queue.");
        sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));

	}
	
	public static List<Message> checkPrintMessages(String myQueueUrl, AmazonSQS sqs){
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		for (Message message : messages) {
			System.out.print(message.getMessageId());
			System.out.println(" : " + message.getBody());
		}
	return messages;
	}
}
