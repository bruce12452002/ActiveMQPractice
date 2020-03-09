package mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class MyActiveMQConsumer {
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

        while (true) {
            TextMessage msg = (TextMessage) consumer.receive(500); // Message 為 TextMessage 父介面
            if (msg != null) {
                System.out.println("receive:" + msg.getText());
            } else {
                break;
            }
        }
    }
}
