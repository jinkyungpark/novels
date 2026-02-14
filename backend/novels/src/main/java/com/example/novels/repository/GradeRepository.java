package com.example.novels.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.novels.entity.Grade;
import com.example.novels.entity.Novel;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Modifying
    @Query("DELETE FROM Grade g WHERE g.novel = :novel")
    void deleteByNovel(Novel novel);
}
