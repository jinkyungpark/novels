package com.example.novels.ai;

import java.time.LocalDateTime;

import com.example.novels.entity.Novel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "novel_embedding")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NovelEmbedding {

    @Id
    private Long novelId; // Novel PK를 그대로 사용 (1:1)

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    private Novel novel;

    @Column(nullable = false)
    private int dim;

    // float32 바이너리로 저장 (용량/속도 유리)
    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] vector;

    private LocalDateTime updatedAt;

    public static NovelEmbedding of(Novel novel, int dim, byte[] vector) {
        NovelEmbedding e = new NovelEmbedding();
        e.novel = novel;
        e.dim = dim;
        e.vector = vector;
        e.updatedAt = LocalDateTime.now();
        return e;
    }

    public void updateVector(int dim, byte[] vector) {
        this.dim = dim;
        this.vector = vector;
        this.updatedAt = LocalDateTime.now();
    }
}
