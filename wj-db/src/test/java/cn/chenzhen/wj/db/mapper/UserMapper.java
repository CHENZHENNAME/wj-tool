package cn.chenzhen.wj.db.mapper;

import cn.chenzhen.wj.db.annotation.Insert;
import cn.chenzhen.wj.db.annotation.Param;
import cn.chenzhen.wj.db.annotation.Select;

import java.util.List;

public interface UserMapper {
    @Insert("insert into test_user(name, sex, status) values(#{name},#{sex},#{status})")
    int insert(User user);


    @Select("select * from test_user where name = #{name}")
    User selectByName(@Param("name") String name);
    @Select("select * from test_user")
    User[] selectByName2();
    @Select("select * from test_user")
    List<User> selectByName3();
}
