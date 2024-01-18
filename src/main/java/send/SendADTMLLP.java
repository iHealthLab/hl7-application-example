package send;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

import java.io.IOException;

public class SendADTMLLP {
    public static void send(String url, int port) throws HL7Exception, LLPException, IOException {
// Create a message to send
        HapiContext context = new DefaultHapiContext();
        String msg =
                "MSH|^~\\&|EPIC|||iHealth|20231215134836|539|ORM^O01|26|T|2.3\rPID|1||1234563^^^MRN^MRN||IHEALTH^LCMC^SCENARIO TWO||19640321|F||White|123 TESTING LANE^^SAINT BERNARD^LA^70085^USA^P^^SAINT BERN|SAINT BERN|(757)675-3599^P^CP^^^757^6753599~^NET^Internet^susan.porcuna@lcmchealth.org||en|M||100000360877|485-55-8844|||NON-HISPANIC||||||||N\rPV1||O|TIB460INTMED^^^TI||||^LEGE^TestAAA^JUDE^^^^^EPIC^^^^NPI|||||||||||||||||||||||||||||||||||||20231215134510||||||80010029\rIN1|1|600001|1086|BLUE CROSS|PO BOX 98029^^BATON ROUGE^LA^70898-9029|||854544||||20230101||||IHEALTH^LCMC^SCENARIO TWO|Self|19640321|123 TESTING LANE^^SAINT BERNARD^LA^70085^USA^^^SAINT BERN|||1||||||||||||||XUP45522155|||||||F\rGT1|1|100240783|IHEALTH^LCMC^SCENARIO TWO^^^^L||123 TESTING LANE^^SAINT BERNARD^LA^70085^USA^^^SAINT BERN|(757)675-3559^P^PH^^^757^6753559~(757)675-3599^P^CP^^^757^6753599||19640321|F|P/F|SLF|485-55-8844\rORC|NW|55668841^EPC||100000360877|||^^^20231215^^R||20231215134832|539^LEGE^CHRISTOPHER||1234455^LEGE^CHRISTOPHER^JUDE^^^^^EPIC^^^^NPI|CCSTB460^^^TI^^^^^TINO B460 INTERNAL MED|(504)897-7999^^^^^504^8977999||||PLTWP2011^PLT-WP2-011^^508020004^TINO B460 INTERNAL MED\rOBR|1|55668841^EPC||VC210003^LCMC IHEALTH BG ENROLLMENT||20231215|||||L|||||1234455^LEGE^CHRISTOPHER^JUDE^^^^^EPIC^^^^NPI|(504)897-7999^^^^^504^8977999|||||||Virtual Care|||^^^20231215^^R|||||||||20231215\rDG1|1|ICD-10-CM|E11.9^Type 2 diabetes mellitus without complications (CMS/HCC)^ICD-10-CM|Type 2 diabetes mellitus without complications (CMS/HCC)||180;ORD\rDG1|2|ICD-10-CM|Z79.4^Long term (current) use of insulin (CMS/HCC)^ICD-10-CM|Long term (current) use of insulin (CMS/HCC)||180;ORD\rOBX|1|ST|^Mobile Phone||757-675-1234||||||F|||20231215134832\rOBX|2|ST|^Preferred Language||English||||||F|||20231215134832\rOBX|3|ST|^iHealth Enrollment - Blood Glucose||Glucose Management||||||F|||20231215134832\rOBX|4|ST|^iHealth BG Device||Blood Glucose Device||||||F|||20231215134832\rOBX|5|ST|^iHealth BG Goals||Maintain or improve A1C by .5% over the next 3 months w/o hypoglycemia||||||F|||20231215134832\r";

        msg = msg.replace("\n", "\r");
        Parser p = context.getPipeParser();
        Message adt = p.parse(msg);

       Connection connection = context.newClient(url, port, false);

       Initiator initiator = connection.getInitiator();
       Message response = initiator.sendAndReceive(adt);

       String responseString = p.encode(response);
       System.out.println("Received response:\n" + responseString);
       connection.close();
    }
}
