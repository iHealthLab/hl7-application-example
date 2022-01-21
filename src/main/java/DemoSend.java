import com.beust.jcommander.JCommander;
import send.*;
import com.beust.jcommander.Parameter;

public class DemoSend {

    @Parameter(names={"--host"}, required = true, description="The host url for HL7 server")
    String host;
    @Parameter(names={"--port"}, description="The port for HL7 server")
    int port = 20002;
    @Parameter(names={"--messageType"}, required = true, description="The message type to send to server")
    String messageType;
    @Parameter(names={"--username", "-u"}, description="The username to verify identity")
    String username;
    @Parameter(names={"--password", "-p"}, description="The password for username to verify identity")
    String password;
    @Parameter(names = "--help", help = true)
    private boolean help;

    public static void main(String[] args) throws Exception {
        DemoSend demoSend = new DemoSend();
        JCommander cmd = JCommander.newBuilder()
                .addObject(demoSend)
                .build();
        cmd.parse(args);
        if (demoSend.help) {
            cmd.usage();
        } else {
            demoSend.run();
        }
    }
    public void run() throws Exception {
        switch (messageType) {
            case "patient":
                SendPatient.send(host, port, username, password);
                break;
            case "obx":
                SendOBX.send(host, port, username, password);
                break;
            case "patientRaw":
                SendPatientRaw.send(host, port, username, password);
                break;
            case "order":
                SendOrder.send(host, port, username, password);
                break;
            case "orderRaw":
                SendOrderRaw.send(host, port, username, password);
                break;
            case "hoh":
                SendOBX.send(host, port);
                break;
            case "mllp":
                SendOBX.sendMLLP(host, port);
                break;
            case "adt":
                SendADTMLLP.send(host, port);
                break;
            case "ehnRaw":
                SendOrderRaw.send(host, port);
                break;
            case "ehn2":
                Send2EHN.send();
                break;
            case "ehn1":
                SimpleMLLPBasedTCPClient.send();
                break;
            default:
                System.out.println("Should specify a data");
                break;
        }
    }
}
