import com.beust.jcommander.JCommander;
import send.*;
import com.beust.jcommander.Parameter;

public class DemoSend {

    @Parameter(names={"--host"}, required = true, description="The host url for HL7 server")
    String host;
    @Parameter(names={"--port"}, description="The port for HL7 server")
    int port = 443;
    @Parameter(names={"--messageType"}, required = true, description="The message type to send to server")
    String messageType;
    @Parameter(names={"--username", "-u"}, required = true, description="The username to verify identity")
    String username;
    @Parameter(names={"--password", "-p"}, required = true, description="The password for username to verify identity")
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
            default:
                System.out.println("Should specify a data");
                break;
        }
    }
}
