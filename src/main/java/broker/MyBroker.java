package broker;

import org.apache.activemq.broker.BrokerService;

// 原文出處 https://activemq.apache.org/how-do-i-embed-a-broker-inside-a-connection
// 以下還得加 jar 檔，org.slf4j.impl 開頭的，加了之後，確無法下載
// 然後執行時，錯誤訊息變了，不知道怎麼解決
// Error:java: Illegal char <:> at index 57: C:\Users\java_bruce\Desktop\Could not find org.slf4j.impl:log4j12:1.7.2.
// Searched in the following locations:
//  - https:\repo.maven.apache.org\maven2\org\slf4j\impl\log4j12\1.7.2\log4j12-1.7.2.pom
//  - https:\repo.maven.apache.org\maven2\org\slf4j\impl\log4j12\1.7.2\log4j12-1.7.2.jar

public class MyBroker {
    public static void main(String[] args) throws Exception {
        BrokerService service = new BrokerService();
        service.setUseJmx(true); // 預設就是 true
        service.addConnector("tcp://localhost:61616");
        service.start();
    }
}
