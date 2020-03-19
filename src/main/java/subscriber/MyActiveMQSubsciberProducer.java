package subscriber;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.stream.Stream;

public class MyActiveMQSubsciberProducer {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setUserName(ActiveMQConnection.DEFAULT_USER);
        factory.setPassword(ActiveMQConnection.DEFAULT_PASSWORD);
        factory.setBrokerURL(ActiveMQConnection.DEFAULT_BROKER_URL);

        Connection connection = factory.createConnection();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Queue 不用了
        Topic topic = session.createTopic("subscriber");// Destination 也行，是 Topic 的父介面
        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        connection.start(); // 啟動延到這裡在開始
        sendData(session, producer);

        session.commit();

        producer.close();
        session.close();
        connection.close();
    }

    private static void sendData(Session session, MessageProducer producer) {
        Stream.iterate(0, i -> ++i).limit(10).forEach(i -> {
            try {
                TextMessage textMessage = session.createTextMessage("xxx=" + i);

                // -----以下和 Server 不一樣-----
                producer.send(textMessage);
                System.out.println("send:" + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }
}
