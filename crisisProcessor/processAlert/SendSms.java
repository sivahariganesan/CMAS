/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crisisProcessor.processAlert;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


/**
 *
 * @author HP
 */
public class SendSms 
{
    public static final String ACCOUNT_SID = "AC5a25bc9fb0ca9a45a22c9c7952429639"; 
    public static final String AUTH_TOKEN = "c3cc97d1fd48b915b1be88866b038be3"; 
    
    public void sendSms(String mobile,String crisis)
    {
        
        try
        {            
            TwilioRestClient client = new TwilioRestClient(SendSms.ACCOUNT_SID, SendSms.AUTH_TOKEN);
            // Build the parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            mobile="+91"+mobile;
            System.out.println("Mobile:"+mobile);
            params.add(new BasicNameValuePair("To", mobile));
            params.add(new BasicNameValuePair("From", "+15204282618"));
            params.add(new BasicNameValuePair("Body", crisis));
            
            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params); 
            System.out.println(message.getStatus());
        }
        catch (TwilioRestException ex) 
        {
            Logger.getLogger(SendSms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
