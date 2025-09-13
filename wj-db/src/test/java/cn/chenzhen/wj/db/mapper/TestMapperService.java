package cn.chenzhen.wj.db.mapper;

import cn.chenzhen.wj.db.MapperUtil;
import cn.chenzhen.wj.db.core.ConnectionManager;
import org.junit.jupiter.api.Test;

public class TestMapperService {
    static {
        ConnectionManager.registerConnectionConfig("jdbc:h2:./test", "root", "root");
    }
    @Test
    public void test(){
        UserMapper mapper = MapperUtil.getService(UserMapper.class);
        mapper.insert(new User("1212","1313",1));
        System.out.println(mapper.selectByName("1212"));
    }
    @Test
    public void test2(){
        UserMapper mapper = MapperUtil.getService(UserMapper.class);
        for (User user : mapper.selectByName2()) {
            System.out.println(user);
        }
        mapper.selectByName3().forEach(System.out::println);
    }
}
