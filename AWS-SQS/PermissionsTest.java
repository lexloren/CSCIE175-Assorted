import java.io.IOException;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;


public class PermissionsTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		try {
			
		String myQueueUrl = "https://queue.amazonaws.com/034307772076/MyQueue";
		
		AmazonSQS sqs = new AmazonSQSClient(new PropertiesCredentials(
                SimpleQueueServiceSample.class.getResourceAsStream("AwsCredentials.properties")));
		
        System.out.println("Sending message 1.");
        sqs.sendMessage(new SendMessageRequest(myQueueUrl, "Test message."));
        
        System.out.println("Receiving messages:");
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		for (Message message : messages) {
			System.out.print(message.getMessageId());
			System.out.println(" : " + message.getBody());

		}
    } catch (AmazonServiceException ase) {
        System.out.println("Caught an AmazonServiceException, which means your request made it " +
                "to Amazon SQS, but was rejected with an error response for some reason.");
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    } catch (AmazonClientException ace) {
        System.out.println("Caught an AmazonClientException, which means the client encountered " +
                "a serious internal problem while trying to communicate with SQS, such as not " +
                "being able to access the network.");
        System.out.println("Error Message: " + ace.getMessage());
    }

	}
}
