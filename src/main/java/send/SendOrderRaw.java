package send;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.hoh.api.*;
import ca.uhn.hl7v2.hoh.auth.SingleCredentialClientCallback;
import ca.uhn.hl7v2.hoh.hapi.client.HohClientSimple;
import ca.uhn.hl7v2.hoh.raw.api.RawSendable;
import ca.uhn.hl7v2.hoh.raw.client.HohRawClientSimple;
import ca.uhn.hl7v2.hoh.sockets.TlsSocketFactory;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;


import java.io.IOException;

public class SendOrderRaw {

    public static void send(String url, int port, String username, String password) throws Exception {
        String host;
        if(url.startsWith("https://")) {
            host = url.substring(8);
        }else if(url.startsWith("http://")){
            host = url.substring(7);
        }else{
            host = url;
        }
        String uri = "/hl7/rawServlet";

// Create a parser
        Parser parser = PipeParser.getInstanceWithNoValidation();

// Create a client
        HohRawClientSimple client = new HohRawClientSimple(host, port, uri);
        if (url.startsWith("https://")) {
            client.setSocketFactory(new TlsSocketFactory());
        }

// Optionally, if credentials should be sent, they
// can be provided using a credential callback
        IAuthorizationClientCallback authCallback = new SingleCredentialClientCallback(username, password);
        client.setAuthorizationCallback(authCallback);

// The ISendable defines the object that provides the actual
// message to send
        String message = "MSH|^~\\&||RIH|||20211025115337.934+0800||ORU^R01^ORU_R01|140601|P|2.8\r" +
                "PID|||aaa\r" +
                "OBR|\r" +
                "OBX|1|ST|AS4-1002.3^BP DIASTOLIC|1|^80|^mol||||||||20211025115337.955+0800\r";

        ISendable<String> sendable = new RawSendable(message);
        System.out.println("connected: " + client.isConnected());

        try {
            // sendAndReceive actually sends the message
            IReceivable<String> receivable = client.sendAndReceive(sendable);
            System.out.println("connected: " + client.isConnected());

            // receivavle.getRawMessage() provides the response
            System.out.println("Response was:\n" + receivable.getMessage());

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
//        String message = "MSH|^~\\&#|HIS|RIH|EKG|EKG|20211025115337.934+0800||ORU^R01^ORU_R01|140601|P|2.8\r" +
//                "PID|0001|00009874|00001122|A00977|SMITH^JOHN^M|MOM|19581119|F|NOTREAL^LINDA^M|C|564 SPRING ST^^NEEDHAM^MA^02494^US|0002|(818)565-1551|(425)828-3344|E|S|C|0000444444|252-00-4414||||SA|||SA||||NONE|V1|0001|I|D.ER^50A^M110^01|ER|P00055|11B^M011^02|070615^BATMAN^GEORGE^L|555888^NOTREAL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^NOTREAL^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|199904101200||||5555112333|||666097^NOTREAL^MANNY^P\r" +
//                "OBR|\r" +
//                "OBX|1|ST|AS4-1002.3^BP DIASTOLIC|1|^80|^mol||||||||20211025115337.955+0800\r";
        String message = "MSH|^~\\&||RIH|||20211125115337.934+0800||ORU^R01^ORU_R01|140601|P|2.3\r" +
                "EVN|R01|20210805160715\r" +
                "PID|||aaa\r" +
                "OBR|\r" +
                "OBX|1|ST|AS4-1002.3^BP DIASTOLIC|1|^80|^mol||||||||20211025115337.955+0800\r";

        HohRawClientSimple client = new HohRawClientSimple(url, port, "/");
//        client.setSocketFactory(new TlsSocketFactory());
//        client.setAutoClose(false);
        System.out.println("connected: " + client.isConnected());
        ISendable<String> sendable = new RawSendable(message);

        try {
            // sendAndReceive actually sends the message
            // sendAndReceive actually sends the message
            IReceivable<String> receivable = client.sendAndReceive(sendable);

            // receivavle.getRawMessage() provides the response
            System.out.println("Response was:\n" + receivable);

            // IReceivable also stores metadata about the message
//            String remoteHostIp = (String) receivable.getMetadata().get(MessageMetadataKeys.REMOTE_HOST_ADDRESS);
//            System.out.println("From:\n" + remoteHostIp);
        } catch (DecodeException | IOException | EncodeException e) {
            e.printStackTrace();
        }

    }

//    public static void main(String[] args) throws Exception {
//        send("192.0.0.110", 2575);
//    }
}
