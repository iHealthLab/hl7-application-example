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
