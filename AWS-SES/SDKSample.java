import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;

public class SDKSample {
	
    private static final String TO = "lloren@gmail.com";
    private static final String FROM = "lloren@fas.harvard.edu";
    private static final String SUBJECT = "Hello World!";
		
	public static void main(String[] args) throws AmazonClientException, IOException {
		
		SendEmailRequest request = new SendEmailRequest()
		    .withSource(FROM);
						
		List<String> toAddresses = new ArrayList<String>();
		toAddresses.add(TO);
		Destination dest = new Destination().withToAddresses(toAddresses);
		request.setDestination(dest);

		Content subjContent = new Content().withData(SUBJECT);
		Message msg = new Message().withSubject(subjContent);
        
        // Include a body in both text and HTML formats.
        Content textContent = new Content().withData("Hello - I hope you're having a good day.");
		Content htmlContent = new Content().withData("<h1>Hello - I hope you're having a good day.</h1>");
		Body body = new Body().withHtml(htmlContent).withText(textContent);
		msg.setBody(body);

		// Note that Amazon will text content as MIME type text/plain, and HTML content as
		// MIME type text/html.
		
		request.setMessage(msg);
			
    	PropertiesCredentials credentials = new PropertiesCredentials(
                AWSJavaMailSample.class
                        .getResourceAsStream("AwsCredentials.properties"));
	
        AmazonSimpleEmailServiceClient client = 
            new AmazonSimpleEmailServiceClient(credentials);
                    
        // Call Amazon SES to send the message 
		try {
			client.sendEmail(request);
		} catch (AmazonClientException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}					
	}
}