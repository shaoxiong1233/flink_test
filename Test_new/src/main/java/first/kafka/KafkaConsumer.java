package first.kafka;

import com.alibaba.fastjson.JSONObject;
import com.twitter.chill.protobuf.ProtobufSerializer;
import first.pojo.Person;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.skywalking.apm.network.language.agent.v3.JVMMetricCollection;

import java.util.Properties;

public class KafkaConsumer {
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

        env.enableCheckpointing(5000);
        //TODO 2）创建数据源
        Properties properties = new Properties();
        //封装kafka的连接地址
        properties.setProperty("bootstrap.servers", "192.168.1.17:9092");
        //指定消费者id
        properties.setProperty("group.id", "test");
        //设置动态监测分区或者主题的变化
        properties.setProperty("flink.partition-discovery.interval-millis", "30000");

        //定义kafka的消费者实例
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>("test5", new SimpleStringSchema(), properties);

        env.addSource(kafkaConsumer).print();

/*

        //注册registerTypeWithKryoSerializer
        env.getConfig().registerTypeWithKryoSerializer(JVMMetricCollection.class, ProtobufSerializer.class);
*/


        //启动消费
       /* FlinkKafkaConsumer<JVMMetricCollection> kafkaConsumer2 = new FlinkKafkaConsumer<>("test4",
                new KafkaJvmMetricDeserializer(), properties);
        DataStream<JVMMetricCollection> stream = env.addSource(kafkaConsumer2);*/


        //读取kafka数据的时候需要指定消费策略，如果不指定会使用auto.offset.reset设置
        kafkaConsumer.setStartFromEarliest();

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
/*
        DataStreamSource<String> dataStreamSource = env.addSource(kafkaConsumer);

        //TODO 3）处理数据

        DataStream<Person> dataStream =dataStreamSource.map(value -> JSONObject.parseObject(value, Person.class));*/

         /*// Keyed Window
stream
       .keyBy(...)               <-  按照一个Key进行分组
       .window(...)              <-  将数据流中的元素分配到相应的窗口中
      [.trigger(...)]            <-  指定触发器Trigger（可选）
      [.evictor(...)]            <-  指定清除器Evictor(可选)
       .reduce/aggregate/process()      <-  窗口处理函数Window Function

// Non-Keyed Window
stream
       .windowAll(...)           <-  不分组，将数据流中的所有元素分配到相应的窗口中
      [.trigger(...)]            <-  指定触发器Trigger（可选）
      [.evictor(...)]            <-  指定清除器Evictor(可选)
       .reduce/aggregate/process()      <-  窗口处理函数Window Function*/



        //窗口主要有两种，一种基于时间（Time-based Window），一种基于数量（Count-based Window）。
        /*1、基于时间分为两种
            Event Time 事件时间
            Count-based Window 到达窗口时间
        * */

        /*2、窗口类型主要分为三种
        滚动窗口 TumblingEventTimeWindows
        滑动窗口 SlidingEventTimeWindows
        会话窗口 EventTimeSessionWindows
        * */

        /*3、窗口函数主要有三种
         * reduce 增量计算 将数据两两合一，返回值类型必须一致
         * aggregate 增量计算 只保留中间数据
         * process 全量计算
         * */

        //添加进redis 键为name ，key为json字符串
        /*dataStream.keyBy("name").timeWindow(Time.seconds(10)).reduce(
                new ReduceFunction<Person>() {
                    @Override
                    public  Person reduce(Person person, Person t1) throws Exception {
                        return new Person(person.getName(),(person.getAge()+t1.getAge()),(person.getCreateDate()));
                    }
                }).print();*/
                //addSink(new MyRedis());
        //TODO 4）打印输出
       //dataStream.print();

        //TODO 5）启动作业
        env.execute();
    }
}

