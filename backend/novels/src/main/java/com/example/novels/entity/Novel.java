package com.example.novels.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString(exclude = "genre")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Novel extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "novel_id")
	private Long id;

	@Column(nullable = false)
	private String title; // 도서 제목

	@Column(nullable = false)
	private String author; // 저자

	private LocalDate publishedDate; // 출판일

	@Column(nullable = false)
	private boolean available;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "GENRE_ID")
	private Genre genre;

	public void changeAvailable(boolean available) {
		this.available = available;
	}

	public void changeGenre(Genre genre) {
		this.genre = genre;
	}
}
