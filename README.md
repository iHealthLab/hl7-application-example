# HL7 DEMO

## Usage
```shell
# build 
mvn package -DskipTests

# run
java -jar target/HL7DemoSend.jar --host https://*** -u ausername -s somepassword --messageType patient
# --messageType patient || --messageType obx || --messageType patientRaw
# --messageType order || --messageType orderRaw
```


//local：
//java -jar target/HL7DemoSend.jar --host localhost --port 9001 --messageType adt

//dev：
//java -jar target/HL7DemoSend.jar --host 20.0.0.27 --port 20002 --messageType adt

/prod
//java -jar target/HL7DemoSend.jar --host 40.0.0.203 --port 20002 --messageType adt

## Server

https://dev-hl7.ihealth-eng.com

https://hl7.ihealth-eng.com
