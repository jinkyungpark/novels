import axios from 'axios';
import type { LoginFormParam } from '../types/user';

export const API_SERVER_HOST = 'http://localhost:8080/api/member';

// 전체 리스트 books 조회
export const postLogin = async (loginParam: LoginFormParam) => {
  console.log('로그인 데이터 ', loginParam);

  const header = {
    headers: {
      'Content-Type': 'x-www-form-urlencoded',
    },
  };

  const form = new FormData();
  form.append('username', loginParam.email);
  form.append('password', loginParam.pw);

  // SecurityConfig 에 설정
  const res = await axios.post(`${API_SERVER_HOST}/login`, form, header);
  return res.data;
};
