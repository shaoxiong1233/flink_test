package first.kafka;

import com.alibaba.fastjson.JSON;
import first.pojo.Person;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaWriter_proto {

    //本地的kafka机器列表
    public static final String BROKER_LIST = "192.168.1.17:9092";
    //kafka的topic
    public static final String TOPIC_PERSON = "test3";
    //key序列化的方式，采用字符串的形式
    public static final String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    //value的序列化的方式
    public static final String VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";

    public static void writeToKafka() throws Exception{
        Properties props = new Properties();
        props.put("bootstrap.servers", BROKER_LIST);
        props.put("key.serializer", KEY_SERIALIZER);
        props.put("value.serializer", VALUE_SERIALIZER);



        KafkaProducer<String, String> producer = new KafkaProducer<>(props);



        //构建Person对象，在name为hqs后边加个随机数
        int randomInt = RandomUtils.nextInt(1, 1000);
        Person person = new Person();
        person.setName("hqs" + randomInt);
        person.setAge(randomInt);
        person.setCreateDate(new Date());
        //转换成JSON
        String personJson = JSON.toJSONString(person);


        //包装成kafka发送的记录
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC_PERSON, null,
                null, personJson);
        //发送到缓存
        producer.send(record);
        System.out.println("向kafka发送数据:" + personJson);
        //立即发送
        System.out.println(TOPIC_PERSON);

        producer.flush();

    }

    public static void main(String[] args) {
        while(true) {
            try {
                //每三秒写一条数据
                TimeUnit.SECONDS.sleep(1);
                writeToKafka();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
