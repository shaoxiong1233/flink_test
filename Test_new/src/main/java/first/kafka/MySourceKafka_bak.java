package first.kafka;


import com.alibaba.fastjson.JSONObject;
import first.pojo.Person;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;


import java.util.Properties;

public class MySourceKafka_bak {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.enableCheckpointing(5000); // 非常关键，一定要设置启动检查点！！
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.1.17:9092");
        props.put("zookeeper.connect", "192.168.1.17:2181");
        props.setProperty("group.id", "flink-group");
        props.put("group.id", "test");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("auto.offset.reset", "latest");

       /* FlinkKafkaConsumer010<String> consumer =
                new FlinkKafkaConsumer010<>("test", new SimpleStringSchema(), props);

        DataStream<Person> dataStream = env.addSource(consumer).map(value -> JSONObject.parseObject(value, Person.class));

        System.out.println(123);*/

      //  FlinkKafkaConsumer011<String> consumer011=new FlinkKafkaConsumer011<String>("test",new SimpleStringSchema(),props);
        FlinkKafkaConsumer<String> consumer=new FlinkKafkaConsumer<String>("test3",new SimpleStringSchema(),props);
       DataStream<Person> dataStream =env.addSource(consumer).map(value -> JSONObject.parseObject(value, Person.class));
        System.out.println(123);
      // dataStream.keyBy("name")
       //dataStream.print();
        /*DataStream<String> stream = env
                .addSource(consumer.assignTimestampsAndWatermarks(new CustomWatermarkEmitter()))
                .print();*/

        //窗口
       // dataStreamSource.printToErr();
       // dataStream.printToErr();
       /* //dataStream.keyBy("name").map(x-> {
            System.out.println("123");
            x.getName();
            return x.getName();
        } ).print();
*/


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


       /* dataStream.keyBy("name").window(SlidingEventTimeWindows.of(Time.seconds(30),Time.seconds(15))).reduce(
                new ReduceFunction<Person>() {
                    @Override
                    public  Person reduce(Person person, Person t1) throws Exception {
                        return new Person(person.getName(),(person.getAge()+t1.getAge()),(person.getCreateDate()));
                    }
                }).addSink(new MyRedis());*/
        /*dataStream.keyBy("name").window(SlidingEventTimeWindows.of(Time.seconds(30),Time.seconds(15))).reduce(
                new ReduceFunction<Person>() {
                    @Override
                    public  Person reduce(Person person, Person t1) throws Exception {
                        return new Person(person.getName(),(person.getAge()+t1.getAge()),(person.getCreateDate()));
                    }
                }).name("23").print();*/

        /*dataStream.keyBy("name").timeWindow(Time.seconds(10)).reduce(
                new ReduceFunction<Person>() {
                    @Override
                    public  Person reduce(Person person, Person t1) throws Exception {
                        return new Person(person.getName(),(person.getAge()+t1.getAge()),(person.getCreateDate()));
                    }
                }).print();*/
        env.execute("SourceJob");



    }
}
