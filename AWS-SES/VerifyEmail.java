import java.io.IOException;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.ListVerifiedEmailAddressesResult;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;

public class VerifyEmail {

    public static void main(String[] args) throws IOException {
    	

    	// Replace the String "address" with the email you wish to verify.

    	String address = "lloren@fas.harvard.edu";
    	
        /*
         * Important: Be sure to fill in your AWS access credentials in the
         * AwsCredentials.properties file before you try to run this sample.
         * http://aws.amazon.com/security-credentials
         */
        
    	PropertiesCredentials credentials = new PropertiesCredentials(
                AWSJavaMailSample.class
                        .getResourceAsStream("AwsCredentials.properties"));
        AmazonSimpleEmailService ses = new AmazonSimpleEmailServiceClient(credentials);
        
        ListVerifiedEmailAddressesResult verifiedEmails = ses.listVerifiedEmailAddresses();
       
        if (verifiedEmails.getVerifiedEmailAddresses().contains(address)) {
        	System.out.println("This address was already verified.");
        }

        ses.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(address));
        System.out.println("Please check the email address " + address + " to verify it");

    }
}