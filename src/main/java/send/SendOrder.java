package send;

import ca.uhn.hl7v2.hoh.api.*;
import ca.uhn.hl7v2.hoh.auth.SingleCredentialClientCallback;
import ca.uhn.hl7v2.hoh.hapi.api.MessageSendable;
import ca.uhn.hl7v2.hoh.hapi.client.HohClientSimple;
import ca.uhn.hl7v2.hoh.sockets.TlsSocketFactory;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v28.message.OMG_O19;
import ca.uhn.hl7v2.model.v28.message.REF_I12;
import ca.uhn.hl7v2.model.v28.segment.IN1;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class SendOrder {
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

        OMG_O19 omgMsg = new OMG_O19();
        omgMsg.initQuickstart("OMG","O19","");
        omgMsg.getPATIENT().getPID().getSetIDPID().setValue("1");
        omgMsg.getPATIENT().getPID().getPatientIdentifierList(0).getIDNumber().setValue("aaabbbccc");

        omgMsg.getORDER().getORC().getOrc1_OrderControl().setValue("NW");
        omgMsg.getORDER().getORC().getPlacerOrderNumber().getEntityIdentifier().setValue("RQ101");
        omgMsg.getORDER().getORC().getPlacerOrderNumber().getNamespaceID().setValue("ORSUPPLY");
        omgMsg.getORDER().getORC().getOrderStatus().setValue("N");
        omgMsg.getORDER().getORC().getOrderType().getText().setValue("type");
        omgMsg.getORDER().getORC().getDateTimeOfTransaction().setValue("20210917130000");

        omgMsg.getORDER().getTIMING().getTQ1().getSetIDTQ1().setValue("0");
        omgMsg.getORDER().getOBR().getSetIDOBR().setValue("2");
        omgMsg.getORDER().getOBSERVATION().getOBX().getSetIDOBX().setValue("222");

        // patient name
        omgMsg.getPATIENT().getPID().getPatientName(0).getGivenName().setValue("Xi");
        omgMsg.getPATIENT().getPID().getPatientName(0).getFamilyName().getSurname().setValue("Jia");

        //dob
        omgMsg.getPATIENT().getPID().getDateTimeOfBirth().setValue(new Date());

        // sex
        omgMsg.getPATIENT().getPID().getAdministrativeSex().getText().setValue("M");

        // country code
        // phone number
        omgMsg.getPATIENT().getPID().getPhoneNumberHome(0).getTelephoneNumber().setValue("(003)060-0974");

        // primary language
        omgMsg.getPATIENT().getPID().getPrimaryLanguage().getText().setValue("EL");

// The MessageSendable provides the message to send
        ISendable sendable = new MessageSendable(omgMsg);
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

}
