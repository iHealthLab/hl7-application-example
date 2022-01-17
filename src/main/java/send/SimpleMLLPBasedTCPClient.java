package send;
import java.net.*;
import java.io.*;
import java.util.Arrays;


public class SimpleMLLPBasedTCPClient {

    private static final char END_OF_BLOCK = '\u001c';
    private static final char START_OF_BLOCK = '\u000b';
    private static final char CARRIAGE_RETURN = 13;

    public static void send() throws IOException {

        // Create a socket to connect to server running locally on port 1080
        Socket socket = new Socket("192.0.0.110", 30001);
        System.out.println("Connected to Server");

        StringBuffer testHL7MessageToTransmit = new StringBuffer();

        testHL7MessageToTransmit.append(START_OF_BLOCK)
                .append("MSH|^~\\&||RIH|||20211125115337.934+0800||ORU^R01^ORU_R01|140601|P|2.8")
                .append(CARRIAGE_RETURN)
                .append("PID|0||5f08b40d10aff00016612977")
                .append(CARRIAGE_RETURN)
                .append("OBX|1|ST|AS4-1002.3^BP DIASTOLIC|1|^80|^mol||||||||20211025115337.955+0800")
                .append(CARRIAGE_RETURN)
                .append(END_OF_BLOCK)
                .append(CARRIAGE_RETURN);

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        // Send the MLLP-wrapped HL7 message to the server
        System.out.println("message: " + testHL7MessageToTransmit);
        out.write(testHL7MessageToTransmit.toString().getBytes());

        byte[] byteBuffer = new byte[200];
        in.read(byteBuffer);
        System.out.println("Recieved: " + Arrays.toString(byteBuffer));

        System.out.println("Received from Server: " + new String(byteBuffer));

        // Close the socket and its streams
        socket.close();
    }
}
