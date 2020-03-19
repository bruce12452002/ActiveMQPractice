※apache-activemq-5.15.11\bin
啟動有兩種方式：
1.activemq start
2.bin 下又分成 win32 和 win64，選自己對應的，然後點擊 activemq.bat 即可
啟動完可看到 INFO | ActiveMQ Jolokia REST API available at http://0.0.0.0:8161/api/jolokia/
所以 http://localhost:8161/ 進去，帳號密碼預設都是 admin

如果有多個 activeMQ，那還要指定多個 activemq.xml, activemq start -xbean:file:/path


※apache-activemq-5.15.11\conf
jetty.xml 有個 bean id 為 jettyPort，可改管理的 port
activemq.xml 有個 transportConnectors 標籤，name 為 openwire，可改 TCP port

activemq.xml 已設定 import resource="jetty.xml"
然後 jetty.xml 也設定了 securityLoginService 為 jetty-realm.properties
所以帳號密碼的資訊在 jetty-realm.properties

login.config 已設定了兩個檔案，users.properties 和 groups.properties
users.properties 所有使用者的帳密
groups.properties 群組下有什麼使用者
這個 login.config 預設未被使用到，所以改了也沒用

brokers(代理人)：多個 activeMQ 協同工作，必需要有 brokers 幫忙，相當於 activeMQ 的實體

※Queue、Topic
兩個都是介面，有共同的父介面 Destination
Queue 為一對一，假設有10個消息，三個消費者，那拿到的消息會是 4 3 3，還是一對一
Topic 為一對多


※事務、承認
connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

AUTO_ACKNOWLEDGE：自動承認
CLIENT_ACKNOWLEDGE：手動承認
DUPS_OK_ACKNOWLEDGE 部分承認，較少用，所以沒研究
SESSION_TRANSACTED：事務

第一個參數為要不要使用事務，如果為 false, 第二個參數就不能使用 Session.SESSION_TRANSACTED，否則報錯
如果為 true，不管第二個參數是什麼，都是自動承認的意思

使用手動承認時要配合 textMessage.acknowledge()
使用事務時要配合 session.commit()
只有承認了，MQ 才會變化
  假設 MQ 已有消息，而消費者並沒有承認，所以 MQ 並沒有承認，這時就會重覆消費


※持久化
表示重啟 MQ 時，資料還在不在，指的是 MQ server，不是 IDE
messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);