package com.ssafy.project.domain.auth.mapper;

import com.ssafy.project.domain.auth.dto.request.LoginRequest;
import com.ssafy.project.domain.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface AuthMapper {

    @Select("""
                SELECT 
                    member_id,
                    email,
                    password,
                    role,
                    name,
                    profile_image,
                    used_storage,
                    created_at,
                    modified_at,
                    is_deleted,
                    main_badge_id,
                    max_stroage
                FROM 
                    Member
                WHERE 
                    email = #{email}
            """)
    Member selectByEmailAndPassword(LoginRequest loginRequest);

}
