package com.example.novels.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.Data;

/***
 * Page<Entity> 의 엔티티 객체들은 DTO 객체로 변환해서 자료구조로 담아 주기
 * 화면 출력에 필요한 페이지 정보 구성
 */

@Data
public class PageResultDTO<E> {

	// 화면에 보여줄 목록
	private List<E> dtoList;

	// 페이지 번호 목록
	private List<Integer> pageNumList;

	private PageRequestDTO pageRequestDTO;

	// 시작, 끝 페이지 번호
	private boolean prev, next;

	private int totalCount, prevPage, nextPage, totalPage, current;

	@Builder(builderMethodName = "withAll")
	public PageResultDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {

		this.dtoList = dtoList;
		this.pageRequestDTO = pageRequestDTO;
		this.totalCount = (int) totalCount;

		int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;
		int start = end - 9;

		int last = (int) (Math.ceil((totalCount) / (double) pageRequestDTO.getSize()));

		end = end > last ? last : end;
		this.prev = start > 1;

		this.next = totalCount > end * pageRequestDTO.getSize();

		// int 타입으로 1~10 생성 ==> List<Integer> list
		// boxed() : int ==> Integer
		this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

		if (prev) {
			this.prevPage = start - 1;
		}

		if (next) {
			this.nextPage = end + 1;
		}

		//
		totalPage = this.pageNumList.size();

		// 사용자가 요청한 현재 페이지
		this.current = pageRequestDTO.getPage();
	}
}
