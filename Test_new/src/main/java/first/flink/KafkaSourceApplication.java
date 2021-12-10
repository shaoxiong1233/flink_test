package first.flink;

import first.pojo.AccessLog;
import first.pojo.CustomSerialSchema;
import first.redis.RedisSink;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class KafkaSourceApplication {
    public static void main(String[] args) throws Exception {

        // 1. 创建运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2. 设置kafka服务连接信息
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.1.17:9092");
        properties.setProperty("group.id", "fink_group");

        // 3. 创建Kafka消费端
        FlinkKafkaConsumer kafkaProducer = new FlinkKafkaConsumer(
                "test5",                  // 目标 topic
                new CustomSerialSchema(),   // 自定义序列化
                properties);

        // 4. 读取Kafka数据源
        DataStreamSource<AccessLog> socketStr = env.addSource(kafkaProducer);

        socketStr.print().setParallelism(1);

        //处理数据
        /*socketStr.keyBy("ip").window(SlidingEventTimeWindows.of(Time.seconds(2), Time.seconds(2))).reduce(
                new ReduceFunction<AccessLog>() {

                    @Override
                    public AccessLog reduce(AccessLog accessLog, AccessLog t1) throws Exception {
                        return t1;
                    }

                }).addSink(new RedisSink());*/

        /*socketStr.print();
         socketStr.addSink(new RedisSink());
*/
         socketStr.keyBy("ip").window(TumblingProcessingTimeWindows.of(Time.seconds(5))).reduce(new ReduceFunction<AccessLog>() {


                 @Override
                 public AccessLog reduce(AccessLog accessLog, AccessLog t1) throws Exception {
                     return t1;
                 }

             }).addSink(new RedisSink());



        // 5. 执行任务
        env.execute("job");
    }


}
