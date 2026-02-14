import axios from 'axios';
import type { Novel, NovelPut, PageParam } from '../types/novel';
import jwtAxios from '../utils/jwtUtil';

// export const API_SERVER_HOST = 'http://localhost:8080/api/novels';
export const API_SERVER_HOST = '';
export const NOVELS_API = `/api/novels`;

// 전체 리스트 books 조회
export const getList = async (pageParam: PageParam) => {
  const { page, size, genre, keyword } = pageParam;
  const res = await axios.get(`${API_SERVER_HOST}`, {
    params: { page: page, size: size, genre: genre, keyword: keyword },
  });

  console.log('도착 데이터 ', res.data);
  return res.data;
};

// 하나 조회
export const getNovel = async (id: string) => {
  console.log('id ', id);
  const res = await axios.get(`${API_SERVER_HOST}/${id}`);
  return res.data;
};

// Book 삽입
export const postBook = async (novelObj: Novel) => {
  console.log('삽입 데이터 ', novelObj);
  const res = await jwtAxios.post(`${API_SERVER_HOST}/add`, novelObj);
  return res.data;
};

// Book 수정
export const putBook = async (novelObj: Novel) => {
  const res = await jwtAxios.put(
    `${API_SERVER_HOST}/edit/${novelObj.id}`,
    novelObj,
  );
  return res.data;
};

// Book 수정 - available 만 수정
export const putAvailable = async (novelObj: NovelPut) => {
  const res = await jwtAxios.put(
    `${API_SERVER_HOST}/available/${novelObj.id}`,
    novelObj,
  );
  return res.data;
};

// Book 삭제
export const deleteOne = async (id: number) => {
  const res = await jwtAxios.delete(`${API_SERVER_HOST}/remove/${id}`);
  return res.data;
};
