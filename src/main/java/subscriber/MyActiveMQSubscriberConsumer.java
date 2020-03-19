package subscriber;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MyActiveMQSubscriberConsumer {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                ActiveMQConnection.DEFAULT_BROKER_URL
        );

        Connection connection = factory.createConnection();
        // connection.start();
        connection.setClientID("bruce");

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // queue 名稱必需和 server 的 queue 名稱一樣，回傳值 Queue 也行，是 Destination 的子介面

        // -----以下和 Server 不一樣-----
        // queue 不用了
        Topic topic = session.createTopic("subscriber");
        TopicSubscriber subscriber = session.createDurableSubscriber(topic, "訂閱名稱xxx");
        //   MessageConsumer consumer = session.createConsumer(topic);
        connection.start(); // 啟動延到這裡在開始

        Message message = subscriber.receive();
        while (message != null) {
            TextMessage msg = (TextMessage) message;
            System.out.println("receive:" + msg.getText());
            message = subscriber.receive();
        }

        session.close();
        connection.close();
    }
}
