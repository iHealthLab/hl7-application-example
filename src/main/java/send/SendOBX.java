package send;

import ca.uhn.hl7v2.hoh.api.*;
import ca.uhn.hl7v2.hoh.auth.SingleCredentialClientCallback;
import ca.uhn.hl7v2.hoh.hapi.api.MessageSendable;
import ca.uhn.hl7v2.hoh.hapi.client.HohClientSimple;
import ca.uhn.hl7v2.hoh.sockets.TlsSocketFactory;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v28.datatype.CWE;
import ca.uhn.hl7v2.model.v28.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v28.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v28.message.ORU_R01;
import ca.uhn.hl7v2.model.v28.segment.MSH;
import ca.uhn.hl7v2.model.v28.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

import java.io.IOException;
import java.util.Date;

public class SendOBX {

    public static void send(String url, int port, String username, String password) throws Exception {
        String host;
        if(url.startsWith("https://")) {
            host = url.substring(8);
        }else if(url.startsWith("http://")){
            host = url.substring(7);
        }else{
            host = url;
        }
        String uri = "/hl7/servlet";

// Create a parser
        Parser parser = PipeParser.getInstanceWithNoValidation();

// Create a client
        HohClientSimple client = new HohClientSimple(host, port, uri, parser);
        if (url.startsWith("https://")) {
            client.setSocketFactory(new TlsSocketFactory());
        }

    IAuthorizationClientCallback authCallback = new SingleCredentialClientCallback(username, password);
    client.setAuthorizationCallback(authCallback);

        ORU_R01 oru = new ORU_R01();
        oru.initQuickstart("ORU", "R01", "P");

//        String auth = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MzE1MDQ0ODQsInVzZXIiOiJpaGVhbHRoIiwiaWF0IjoxNjMwODk5Njg0fQ.rsM-A_RMCjRQq6w5CNg8VJzuyN0XQMvbKaQLsYfTbylF_1P1nG81NOeqyPcLNHLRQnrl2WCs5mon9VFzXSKz2Q";
//        //auth
//        oru.getUAC().getUserAuthenticationCredential().getData().setValue(auth);

        MSH msh = oru.getMSH();
        msh.getSendingFacility().getNamespaceID().setValue("RIH");
        oru.getPATIENT_RESULT().getPATIENT().getPID().getPatientIdentifierList(0).getIDNumber().setValue("aaa");

        /*
         * The OBR segment is contained within a group called ORDER_OBSERVATION,
         * which is itself in a group called PATIENT_RESULT. These groups are
         * reached using named accessors.
         */
        ORU_R01_ORDER_OBSERVATION orderObservation = oru.getPATIENT_RESULT().getORDER_OBSERVATION();

        // Populate the OBR

        /*
         * The OBX segment is in a repeating group called OBSERVATION. You can
         * use a named accessor which takes an index to access a specific
         * repetition. You can ask for an index which is equal to the
         * current number of repetitions,and a new repetition will be created.
         */
        ORU_R01_OBSERVATION observation = orderObservation.getOBSERVATION(0);

        OBX obx = observation.getOBX();
        obx.getSetIDOBX().setValue("1");
        obx.getValueType().setValue("ST");
        obx.getObservationIdentifier().getIdentifier().setValue("AS4-1002.3");
        obx.getObservationIdentifier().getText().setValue("BP DIASTOLIC");


        obx.getObservationSubID().setValue("1");

        CWE cwe = new CWE(oru);
        cwe.getText().setValue("90");
        obx.getObservationValue(0).setData(cwe);
        obx.getDateTimeOfTheObservation().setValue(new Date());
        // The first OBX has a value type of CE. So first, we populate OBX-2 with "CE"...

// The MessageSendable provides the message to send
        ISendable<Message> sendable = new MessageSendable(oru);

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
