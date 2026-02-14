package com.example.novels.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.novels.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
    // 조회 시 권한 목록까지 같이 가져나오기
    @EntityGraph(attributePaths = { "memberRoles" })
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Member getWithRoles(@Param("email") String email);
}
