package first.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccessLog implements Serializable {

    private String ip;

    private String time;


    private String type;

    private String api;

    private Integer num;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "AccessLog{" +
                "ip='" + ip + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", api='" + api + '\'' +
                ", num=" + num +
                '}';
    }
}
