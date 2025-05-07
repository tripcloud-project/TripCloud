package com.ssafy.project.domain.auth.mapper;

import com.ssafy.project.domain.auth.dto.request.LoginRequestDto;
import com.ssafy.project.domain.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface AuthMapper {

    @Select("SELECT u.id, email, password, u.name, r.name AS 'role', created_at, updated_at, deleted_at\r\n"
            + "FROM users u \r\n"
            + "    JOIN roles r on u.role_id = r.id WHERE email=#{email} AND password=#{password}")
    Member selectByEmailAndPassword(LoginRequestDto loginRequest);
}
