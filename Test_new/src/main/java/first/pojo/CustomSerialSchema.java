package first.pojo;


import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import org.springframework.beans.BeanUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.apache.flink.shaded.curator4.com.google.common.base.Preconditions.checkNotNull;

/**
 * 自定义序列化实现（Protobuf）
 */
public class CustomSerialSchema implements DeserializationSchema<AccessLog>, SerializationSchema<AccessLog> {

    private static final long serialVersionUID = 1L;

    private transient Charset charset;

    public CustomSerialSchema() {
        this(StandardCharsets.UTF_8);
    }

    public CustomSerialSchema(Charset charset) {
        this.charset = checkNotNull(charset);
    }

    public Charset getCharset() {
        return charset;
    }

    /**
     * 反序列化实现
     * @param message
     * @return
     */
    @Override
    public AccessLog deserialize(byte[] message) {
        AccessLog accessLog = null;
        try {
            AccessLogProto.AccessLog accessLogProto = AccessLogProto.AccessLog.parseFrom(message);
            accessLog = new AccessLog();
            BeanUtils.copyProperties(accessLogProto, accessLog);
            return accessLog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessLog;
    }

    @Override
    public boolean isEndOfStream(AccessLog nextElement) {
        return false;
    }

    /**
     * 序列化处理
     * @param element
     * @return
     */
    @Override
    public byte[] serialize(AccessLog element) {
        AccessLogProto.AccessLog.Builder builder = AccessLogProto.AccessLog.newBuilder();
        BeanUtils.copyProperties(element, builder);

        return builder.build().toByteArray();
    }

    /**
     * 定义消息类型
     * @return
     */
    @Override
    public TypeInformation<AccessLog> getProducedType() {
        return TypeInformation.of(AccessLog.class);
    }

}

