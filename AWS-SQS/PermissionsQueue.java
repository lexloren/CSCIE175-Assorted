import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.SQSActions;
import com.amazonaws.auth.policy.conditions.IpAddressCondition;

public class PermissionsQueue {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

        AmazonSQS sqs = new AmazonSQSClient(new PropertiesCredentials(
                SimpleQueueServiceSample.class.getResourceAsStream("AwsCredentials.properties")));
        
        CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
        String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        
        GetQueueAttributesRequest getQARequest = new GetQueueAttributesRequest()
    		.withQueueUrl(myQueueUrl)
    		.withAttributeNames("QueueArn"); 
        GetQueueAttributesResult getQAResult =  sqs.getQueueAttributes(getQARequest);  
        Map<String,String> attributeMap = getQAResult.getAttributes();
        String queueArn = attributeMap.get("QueueArn"); 

		// Set up Policy
        IpAddressCondition deny = new IpAddressCondition("140.247.10.103/32");
        Policy policy = new Policy("restrictIpAddress").withStatements(
        	new Statement(Effect.Deny)
        	.withPrincipals(Principal.AllUsers)
        	.withActions(SQSActions.AllSqsActions)
        	.withResources(new Resource(queueArn))
        	.withConditions(deny)
        );

        Map<String,String> queueAttributes = new HashMap<String,String>();
        queueAttributes.put("Policy", policy.toJson());
        sqs.setQueueAttributes(new SetQueueAttributesRequest(myQueueUrl, queueAttributes));
        
        System.out.println("QueueUrl: " + myQueueUrl);
        System.out.println("ARN: " + queueArn);

	}

}
