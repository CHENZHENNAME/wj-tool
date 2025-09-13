package cn.chenzhen.wj.db.mapper;

public class User {
    private Integer id;
    private String name;
    private String sex;
    private Integer status;

    public User() {
    }

    public User(String name, String sex, Integer status) {
        this.name = name;
        this.sex = sex;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", status=" + status +
                '}';
    }
}
