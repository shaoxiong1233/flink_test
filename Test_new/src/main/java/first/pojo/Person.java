package first.pojo;


import java.io.Serializable;
import java.util.Date;


public class Person {
    private String name;
    private int age;
    private Date createDate;
    public Person(){}
    public Person(String name, int age, Date createDate){
        this.age=age;
        this.name=name;
        this.createDate=createDate;
    };
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", createDate=" + createDate +
                '}';
    }
}