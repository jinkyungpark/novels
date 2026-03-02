package com.example.novels.ai;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NovelTagRepository extends JpaRepository<NovelTag, Long> {

    // @Transactional
    @Modifying
    @Query("delete from NovelTag nt where nt.novel.id= :novelId")
    int deleteByNovelId(@Param("novelId") Long novelId);

    // List<NovelTag> findByNovelId(@Param("novelId") Long novelId);

    List<NovelTag> findByNovel_IdIn(List<Long> ids);

}
