
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;


public class OversizedQueue {

    public static void main(String[] args) throws Exception {
       
        AmazonSQS sqs = new AmazonSQSClient(new PropertiesCredentials(
                OversizedQueue.class.getResourceAsStream("AwsCredentials.properties")));

        String longMessage = readFiletoString(new File("C:\\Users\\Lisa\\workspace\\72k.txt"));

        try {
            // Create a queue
            System.out.println("Creating a new SQS queue.");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest("LorenTestQueue");
            String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();

            // Send a message
            System.out.println("Sending a message to Queue.\n");;
            sqs.sendMessage(new SendMessageRequest(myQueueUrl, longMessage));

            System.out.println("Length sent: " + longMessage.length());
            
            // Receive messages
            System.out.println("Receiving messages from Queue.");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : messages) {
            	System.out.println("Length received: " + message.getBody().length());
            }
            System.out.println();

            // Delete a message
            System.out.println("Deleting a message.\n");
            String messageRecieptHandle = messages.get(0).getReceiptHandle();
            sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageRecieptHandle));

            // Delete a queue
            System.out.println("Deleting the test queue.\n");
            sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
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

    public static String readFiletoString(File f) throws IOException {
		Scanner s = new Scanner(f);
		StringBuilder sb = new StringBuilder();
    	while (s.hasNext()==true) {
    		sb.append(s.next());
    	}
		return sb.toString();
    }
}
