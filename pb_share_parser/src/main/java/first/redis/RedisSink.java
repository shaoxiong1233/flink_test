package first.redis;

import first.pojo.AccessLog;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;

public class RedisSink extends RichSinkFunction<AccessLog> {
    private Jedis jedis = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        String host = "192.168.1.17";
        int port = 6379;
        JedisPool pool = new JedisPool(host, port);
        jedis = pool.getResource();
    }

    @Override
    public void invoke(AccessLog value, Context context) {
        HashMap<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(value.getTime()));
        map.put("num", String.valueOf(value.getNum()));
        map.put("qwe", String.valueOf(value.getApi()));
        String key = String.valueOf(value.getTime());
        jedis.hmset(value.getIp(), map); // 保存的格式是key:时间戳，value: 全部的内容
        jedis.expire(key, 50); // 生存周期 5s (5s后在redis数据库中删除)
    }

    @Override
    public void close() throws Exception {
        super.close();
        jedis.close();
        if (jedis != null) {
            jedis.close();
        }
    }
}
