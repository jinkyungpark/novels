package com.example.novels.ai;

import com.example.novels.entity.Novel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class NovelTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagType type; // MOOD, THEME, TROPE, SETTING, AUDIENCE

    @Column(nullable = false)
    private String value;

    private Double confidence; // 0~1 (선택)

    public void changeConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public void changeId(Long id) {
        this.id = id;
    }

    public void changeNovel(Novel novel) {
        this.novel = novel;
    }

    public void changeValue(String value) {
        this.value = value;
    }

    public void changeType(TagType type) {
        this.type = type;
    }

}