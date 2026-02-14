import { Cookies } from 'react-cookie';

const cookies = new Cookies();

//쿠키 만료 시간 설정
export const setCookie = (name: string, value: string, days: number) => {
  const expires = new Date();
  expires.setUTCDate(expires.getUTCDate() + days);
  return cookies.set(name, value, { path: '/', expires: expires });
};

//쿠키 가져오기
export const getCookie = (name: string) => {
  return cookies.get(name);
};

//쿠키 제거
export const removeCookie = (name: string, path = '/') => {
  cookies.remove(name, { path });
};
