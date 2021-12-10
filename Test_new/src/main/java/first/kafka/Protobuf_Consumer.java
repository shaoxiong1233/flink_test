package first.kafka;

import com.twitter.chill.protobuf.ProtobufSerializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.skywalking.apm.network.language.agent.v3.JVMMetricCollection;

import java.util.Properties;

public class Protobuf_Consumer {

    public static void main(String[] args) throws Exception {
        /**
         * 实现步骤：
         * 1）初始化flink流处理的运行环境
         * 2）创建数据源
         * 3）处理数据
         * 4）打印输出
         * 5）启动作业
         */
        //TODO 1）初始化flink流处理的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置检查点
        env.enableCheckpointing(5000);
        //TODO 2）创建数据源
        Properties properties = new Properties();
        //封装kafka的连接地址
        properties.setProperty("bootstrap.servers", "192.168.1.17:9092");
        //指定消费者id
        properties.setProperty("group.id", "test");
        //设置动态监测分区或者主题的变化
        properties.setProperty("flink.partition-discovery.interval-millis", "30000");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        //注册registerTypeWithKryoSerializer
     /*   env.getConfig().registerTypeWithKryoSerializer(JVMMetricCollection.class, ProtobufSerializer.class);
        //启动消费
        FlinkKafkaConsumer<JVMMetricCollection> kafkaConsumer2 = new FlinkKafkaConsumer<>("test4",
                new KafkaJvmMetricDeserializer(), properties);
        DataStream<JVMMetricCollection> stream = env.addSource(kafkaConsumer2);


        //读取kafka数据的时候需要指定消费策略，如果不指定会使用auto.offset.reset设置
        kafkaConsumer2.setStartFromEarliest();
*/





        /**
         * TODO 可以使用以下两种方式指定消费策略：
         * 1：props.setProperty("auto.offset.reset", "earliest");
         * 2：kafkaConsumer.setStartFromEarliest();
         *
         * 如:
         *   kafkaConsumer.setStartFromEarliest();       // 从头开始消费
         *   kafkaConsumer.setStartFromTimestamp(System.currentTimeMillis()); // 从指定的时间戳开始消费
         *   kafkaConsumer.setStartFromGroupOffsets();   // 从group 中记录的offset开始消费
         *   kafkaConsumer.setStartFromLatest();         // 以及指定每个从某个topic的某个分区的某个offset开始消费
         *   Map<KafkaTopicPartition, Long> offsets = new HashMap<>();
         *   offsets.put(new KafkaTopicPartition(topic, 0), 0L);
         *   offsets.put(new KafkaTopicPartition(topic, 1), 0L);
         *   offsets.put(new KafkaTopicPartition(topic, 2), 0L);
         *   kafkaConsumer.setStartFromSpecificOffsets(offsets);
         *
         *   如上, 就指定了topic的分区0,1,2 都分别从offset 0 开始消费.
         */


        //TODO 3）处理数据


        //TODO 4）打印输出


        //TODO 5）启动作业
        env.execute();
    }
}
