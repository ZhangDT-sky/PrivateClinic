package org.code.privateclinic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.code.privateclinic.bean.User;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> selectUser();

    User selectUserById(Long id);

    User selectUserByUsername(String username);

    List<User> selectUserByRole(String role);

    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertUser(User user);

    int updateUser(User user);

    int deleteUserById(Long id);

    int deleteUserByIdPhysical(Long id);
}

