package mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.stream.Stream;

public class MyActiveMQProducer {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setUserName(ActiveMQConnection.DEFAULT_USER);
        factory.setPassword(ActiveMQConnection.DEFAULT_PASSWORD);
        factory.setBrokerURL(ActiveMQConnection.DEFAULT_BROKER_URL);

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("mmqq"); // Destination 也行，是 Queue 的父介面

        MessageProducer producer = session.createProducer(queue);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        sendData(session, producer);

        session.commit();
    }

    private static void sendData(Session session, MessageProducer producer) {
        Stream.iterate(0, i -> ++i).limit(10).forEach(i -> {
            try {
                TextMessage textMessage = session.createTextMessage("xxx=" + i);
                producer.send(textMessage);
                System.out.println("send:" + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }
}
