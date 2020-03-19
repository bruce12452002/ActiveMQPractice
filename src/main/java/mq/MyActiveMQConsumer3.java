package mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class MyActiveMQConsumer3 {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                ActiveMQConnection.DEFAULT_BROKER_URL
        );

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // queue 名稱必需和 server 的 queue 名稱一樣，回傳值 Queue 也行，是 Destination 的子介面
        Destination queue = session.createQueue("mmqq");

        // -----以下和 Server 不一樣-----
        MessageConsumer consumer = session.createConsumer(queue);

        // consumer 有兩種方法，一種使用監聽，一種使用 receive，receive 又分阻塞和時間到就走
        consumer.setMessageListener(m -> {
            if (m instanceof TextMessage) {
                TextMessage msg = (TextMessage) m;
                try {
                    System.out.println("receive:" + msg.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            System.in.read(); // 使控制台的紅燈保持在啟動狀態，因為有可能關閉太快而沒收到
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        while (true) {
            TextMessage msg = (TextMessage) consumer.receive(500); // Message 為 TextMessage 父介面
            if (msg != null) {
                System.out.println("receive:" + msg.getText());
            } else {
                break;
            }
        }
                */
        consumer.close();
        session.close();
        connection.close();
    }
}
