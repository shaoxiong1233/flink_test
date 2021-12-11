package first.flink;

import first.pojo.AccessLog;
import first.pojo.CustomSerialSchema;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

import java.util.Properties;

public class KafkaWriterApplication {

    public static void main(String[] args) throws Exception {

        // 1. 创建运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2. 读取Socket数据源
        DataStreamSource<String> socketStr = env.socketTextStream("localhost", 9912, "\n");
        //单线程打印输出
        socketStr.print().setParallelism(1);

        // 3. 转换处理流数据
        SingleOutputStreamOperator<AccessLog> outputStream = socketStr.map(new MapFunction<String, AccessLog>() {
            @Override
            public AccessLog map(String value) throws Exception {
                System.out.println(value);
                // 根据分隔符解析数据
                String[] arrValue = value.split(" ");
                // 将数据组装为对象
                AccessLog log = new AccessLog();
                log.setNum(1333);
                for (int i = 0; i < arrValue.length; i++) {

                    if (i == 0) {
                        log.setIp(arrValue[i]);
                    } else if (i == 1) {
                        log.setTime(arrValue[i]);
                    } else if (i == 2) {
                        log.setType(arrValue[i]);
                    } else if (i == 3) {
                        log.setApi(arrValue[i]);
                    }
                }

                return log;
            }
        });




        // 3. Kakfa的生产者配置
       Properties properties = new Properties();
       properties.setProperty("bootstrap.servers", "192.168.1.17:9092");
        FlinkKafkaProducer kafkaProducer = new FlinkKafkaProducer(
                "192.168.1.17:9092",            // broker 列表
                "test5",                  // 目标 topic
                new CustomSerialSchema()   // 序列化 方式
        );



        // 4. 添加kafka的写入器
        outputStream.addSink(kafkaProducer);





        // 5. 执行任务
        env.execute("job");
    }
}

