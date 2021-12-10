package send;

import ca.uhn.hl7v2.hoh.api.*;
import ca.uhn.hl7v2.hoh.auth.SingleCredentialClientCallback;
import ca.uhn.hl7v2.hoh.hapi.api.MessageSendable;
import ca.uhn.hl7v2.hoh.hapi.client.HohClientSimple;
import ca.uhn.hl7v2.hoh.sockets.TlsSocketFactory;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v28.message.REF_I12;
import ca.uhn.hl7v2.model.v28.segment.IN1;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class SendPatient {
    public static void send(String url, int port, String username, String password) throws Exception {
        String uri = "/hl7/servlet";
        Parser parser = PipeParser.getInstanceWithNoValidation();

        String host;
        if(url.startsWith("https://")) {
            host = url.substring(8);
        }else if(url.startsWith("http://")){
            host = url.substring(7);
        }else{
            host = url;
        }

        // Create a client
        HohClientSimple client = new HohClientSimple(host, port, uri, parser);
        if (url.startsWith("https://")) {
            client.setSocketFactory(new TlsSocketFactory());
        }

        IAuthorizationClientCallback authCallback = new SingleCredentialClientCallback(username, password);
        client.setAuthorizationCallback(authCallback);

        REF_I12 refMsg = new REF_I12();

        refMsg.initQuickstart("REF", "I12", "processingId");

        // auth
//        String auth = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MzAzOTI0MTUsInVzZXIiOiJhcHAxIiwiaWF0IjoxNjI5Nzg3NjE1fQ.nXne1WPu0WBRV-6yAk6mejZONsOM49UtnVuFi2UoJhjLg7UXeMsxq62QCBhssRA1d-DBXLNIHs23Ckg6CbTr2g";
//        refMsg.getUAC().getUserAuthenticationCredential().getData().setValue(auth);

        // patient id
        refMsg.getPID().getSetIDPID().setValue("1");
        refMsg.getPID().getPatientIdentifierList(0).getIDNumber().setValue("aaabbb");

        // patient name
        refMsg.getPID().getPatientName(0).getGivenName().setValue("Xi");
        refMsg.getPID().getPatientName(0).getFamilyName().getSurname().setValue("Jia");

        //dob
        refMsg.getPID().getDateTimeOfBirth().setValue(new Date());

        // sex
        refMsg.getPID().getAdministrativeSex().getText().setValue("M");

        // country code
        // phone number
        refMsg.getPID().getPhoneNumberHome(0).getTelephoneNumber().setValue("(003)060-0974");

        // primary language
        refMsg.getPID().getPrimaryLanguage().getText().setValue("EL");

        // last updated
        refMsg.getPID().getLastUpdateDateTime().setValue(new Date());

        //insurance
        refMsg.getRF1().getReferralStatus().getText().setValue("referral status");
        IN1 in1 = refMsg.getINSURANCE().getIN1();
        in1.getInsuranceCompanyName(0).getOrganizationName().setValue("insurance company name");

// The MessageSendable provides the message to send
        ISendable sendable = new MessageSendable(refMsg);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter w = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
        sendable.writeMessage(w);
        String result = bos.toString();
        try {
            // sendAndReceive actually sends the message
            IReceivable<Message> receivable = client.sendAndReceiveMessage(sendable);

            // receivavle.getRawMessage() provides the response
            Message message = receivable.getMessage();
            System.out.println("Response was:\n" + message.encode());

            /*
             * Note that the client may be reused as many times as you like,
             * by calling sendAndReceiveMessage repeatedly
             */

        } catch (DecodeException | IOException | EncodeException e) {
            // Thrown if the response can't be read
            e.printStackTrace();
        } // Thrown if communication fails
        // Thrown if the message can't be encoded (generally a programming bug)

    }

    public static void send(String url, int port) throws Exception {
        Parser parser = PipeParser.getInstanceWithNoValidation();

        // Create a client
        HohClientSimple client = new HohClientSimple(url, port, "/", parser);
        if (url.startsWith("https://")) {
            client.setSocketFactory(new TlsSocketFactory());
        }

        REF_I12 refMsg = new REF_I12();

        refMsg.initQuickstart("REF", "I12", "processingId");

        // auth
//        String auth = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MzAzOTI0MTUsInVzZXIiOiJhcHAxIiwiaWF0IjoxNjI5Nzg3NjE1fQ.nXne1WPu0WBRV-6yAk6mejZONsOM49UtnVuFi2UoJhjLg7UXeMsxq62QCBhssRA1d-DBXLNIHs23Ckg6CbTr2g";
//        refMsg.getUAC().getUserAuthenticationCredential().getData().setValue(auth);

        // patient id
        refMsg.getPID().getSetIDPID().setValue("1");
        refMsg.getPID().getPatientIdentifierList(0).getIDNumber().setValue("aaabbb");

        // patient name
        refMsg.getPID().getPatientName(0).getGivenName().setValue("Xi");
        refMsg.getPID().getPatientName(0).getFamilyName().getSurname().setValue("Jia");

        //dob
        refMsg.getPID().getDateTimeOfBirth().setValue(new Date());

        // sex
        refMsg.getPID().getAdministrativeSex().getText().setValue("M");

        // country code
        // phone number
        refMsg.getPID().getPhoneNumberHome(0).getTelephoneNumber().setValue("(003)060-0974");

        // primary language
        refMsg.getPID().getPrimaryLanguage().getText().setValue("EL");

        // last updated
        refMsg.getPID().getLastUpdateDateTime().setValue(new Date());

        //insurance
        refMsg.getRF1().getReferralStatus().getText().setValue("referral status");
        IN1 in1 = refMsg.getINSURANCE().getIN1();
        in1.getInsuranceCompanyName(0).getOrganizationName().setValue("insurance company name");

// The MessageSendable provides the message to send
        ISendable sendable = new MessageSendable(refMsg);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter w = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
        sendable.writeMessage(w);
        String result = bos.toString();
        try {
            // sendAndReceive actually sends the message
            IReceivable<Message> receivable = client.sendAndReceiveMessage(sendable);

            // receivavle.getRawMessage() provides the response
            Message message = receivable.getMessage();
            System.out.println("Response was:\n" + message.encode());

            /*
             * Note that the client may be reused as many times as you like,
             * by calling sendAndReceiveMessage repeatedly
             */

        } catch (DecodeException | IOException | EncodeException e) {
            // Thrown if the response can't be read
            e.printStackTrace();
        } // Thrown if communication fails
        // Thrown if the message can't be encoded (generally a programming bug)

    }

//    public static void main(String[] args) throws Exception {
//        send("192.0.0.110", 2575);
//    }

}
