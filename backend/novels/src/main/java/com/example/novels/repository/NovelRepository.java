package com.example.novels.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.novels.entity.Novel;

public interface NovelRepository extends JpaRepository<Novel, Long>, SearchNovelRepository {

    // 평점이 없을 수도 있으니 외래 키
    @Query("select n, ge, avg(g.rating) from Novel n  join n.genre ge left join Grade g on g.novel = n where n.id = :id  group by n, ge")
    List<Object[]> getNovelById1(@Param("id") Long id);

}
