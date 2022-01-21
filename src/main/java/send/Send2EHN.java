package send;

import java.net.*;
import java.io.*;

public class Send2EHN {
    private static final char END_OF_BLOCK = '\u001c';
    private static final char START_OF_BLOCK = '\u000b';
    private static final char CARRIAGE_RETURN = 13;

    public static void send() throws IOException {

        // Create a socket to connect to server running locally on port 1080
        Socket socket = new Socket("20.0.0.27", 20003);
        System.out.println("Connected to Server");

        StringBuffer testHL7MessageToTransmit = new StringBuffer();

        testHL7MessageToTransmit.append(START_OF_BLOCK)
                .append("MSH|^~\\&|SLAB|LIS|SCM|SCHCC|20210805160715||ORU^R01|4242479|P|2.3")
                .append(CARRIAGE_RETURN)
                .append("EVN|R01|20210805160715")
                .append(CARRIAGE_RETURN)
                .append("PID|1|2033958^^^SCHCC|42072^^^LIS||TEST^ACE||19500101|M|ACE,,TEST||3828 DELMAS TERRACE^^CULVER  CITY^CA^90232^USA||(310) 836-7000||eng^En|M|000|500096745^^^SCHCC||||||||||||1")
                .append(CARRIAGE_RETURN)
                .append("PV1|1|O|CER^^^SCHCC|El|||DEF|||65||||Newbor||||O|500096745^^^SCHCC||||||||||||||||||||SCHCC|||||20180910114700")
                .append(CARRIAGE_RETURN)
                .append("ORC|RE|588508@COID^SLAB|||CM||1^^^20210805160002^^ROUTINE||20210805160002|501RLEONARDO||1720042864^Abraham^Reginald^G|CER")
                .append(CARRIAGE_RETURN)
                .append("OBR|1|588508@COID^SLAB||COID^COVID-19 Abbott Test|ROUTINE|20210805050000|20210805160002||0||N|||20210805160002|SB|1720042864^Abraham^Reginald^G||||||||IMM|F||1^^^20210805160002^^ROUTINE")
                .append(CARRIAGE_RETURN)
                .append("OBX|1|ST|IDSRCE^ID Source||Nasopharyngeal|||N|||F|||20210805160346|CCHLAB^Southern California Hospital at Culver City Lab^3828.. Delmas Terrace^^^CULVER CITY^CA^90232^^3108367000^3102024159^^William D. Power, MD^^05D0642522|501RLEONARDO")
                .append(CARRIAGE_RETURN)
                .append("OBX|2|ST|COVID^COVID-19 ID Now||Negative||Negative|N|||F|||20210805160326|CCHLAB^Southern California Hospital at Culver City Lab^3828.. Delmas Terrace^^^CULVER CITY^CA^90232^^3108367000^3102024159^^William D. Power, MD^^05D0642522|501RLEONARDO")
                .append(CARRIAGE_RETURN)
                .append("NTE|1|L|Negative results do not preclude 2019-nCoV infection and should not be used as the sole basis for treatment or other patient management decisions. Negative results must be combined with clinical observation, patient history, and epidemiological information.\\.br\\\\.br\\False negative results may occur if a specimen is improperly collected, transported or handled. False negative results may also occur if amplification inhibitors are present in the specimen or if inadequate levels of viruses are present in the specimen. The test cannot rule out diseases caused by other bacterial or viral pathogens.\\.br\\\\.br\\For a Negative result in which a strong clinical suspicion for COVID-19 exists, recollection of a nasopharyngeal swab in viral transport medium (VTM) for PCR testing is recommended.\\.br\\\\.br\\Testing was performed using the ID NOW Instrument by Abbott. This system is a rapid molecular in-vitro diagnostic test, utilizing an isothermal nucleic acid technology intended for the qualitative de")
                .append(CARRIAGE_RETURN)
                .append("NTE|2|L|tection of nucleic acid from the SARS-CoV-2 viral RNA in a direct nasopharyngeal swab. This molecular assay has not been cleared or approved by the Food and Drug Administration (FDA) but has been authorized by the FDA under an Emergency Use Authorization (EUA).\\.br\\\\.br\\Healthcare Provider and Patient fact sheets are available on the Abbott website: https://www.alere.com/en/home/product-details/id-now-covid-19.html")
                .append(CARRIAGE_RETURN)
                .append("OBX|3|ST|IDPSYM^ID Patient Symptoms||None Given|||N|||F|||20210805160337|CCHLAB^Southern California Hospital at Culver City Lab^3828.. Delmas Terrace^^^CULVER CITY^CA^90232^^3108367000^3102024159^^William D. Power, MD^^05D0642522|501RLEONARDO")
                .append(CARRIAGE_RETURN)
                .append(END_OF_BLOCK)
                .append(CARRIAGE_RETURN);

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        // Send the MLLP-wrapped HL7 message to the server
        out.write(testHL7MessageToTransmit.toString().getBytes());

        byte[] byteBuffer = new byte[200];
        in.read(byteBuffer);

        System.out.println("Received from Server: " + new String(byteBuffer));

        // Close the socket and its streams
        socket.close();
    }
}
