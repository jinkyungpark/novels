import axios, {
  AxiosError,
  type AxiosResponse,
  type InternalAxiosRequestConfig,
} from 'axios';
import { getCookie, setCookie } from './cookieUtils';

const jwtAxios = axios.create();

const host = 'http://localhost:8080';

// refresh Token 이용한 자동갱신
// beforeRes() 응답 데이터가 ERROR_ACCESS_TOKEN 과 같이 Access Token 관련된 메시지인 경우 실행
const refreshJWT = async (accessToken: string, refreshToken: string) => {
  const header = { headers: { Authorization: `Bearer ${accessToken}` } };
  const res = await axios.get(
    `${host}/api/member/refresh?refreshToken=${refreshToken}`,
    header,
  );

  console.log('-----------------');
  console.log(res.data);
  return res.data;
};

//before request
const beforeReq = (config: InternalAxiosRequestConfig) => {
  console.log('before request.......');
  // 쿠키 값 서버로 전송
  const memberInfo = getCookie('member');
  if (!memberInfo) {
    console.log('Member Not Found');

    return Promise.reject({
      response: {
        data: { error: 'REQUIRE_LOGIN' },
      },
    });
  }

  const { accessToken } = memberInfo;
  config.headers.Authorization = `Bearer ${accessToken}`;
  return config;
};

//fail request
const requestFail = (err: AxiosError) => {
  console.log('request  err.......');
  return Promise.reject(err);
};

//before return response
const beforeRes = async (
  res: AxiosResponse<unknown>,
): Promise<AxiosResponse<unknown>> => {
  console.log('before return response.......');
  console.log(res);

  const data = res.data as { error?: string };

  if (data?.error === 'ERROR_ACCESS_TOKEN') {
    const memberCookieValue = getCookie('member');

    const result = await refreshJWT(
      memberCookieValue.accessToken,
      memberCookieValue.refreshToken,
    );
    console.log('refreshJWT RESULT', result);
    memberCookieValue.accessToken = result.accessToken;
    memberCookieValue.refreshToken = result.refreshToken;
    setCookie('member', JSON.stringify(memberCookieValue), 1);

    //원래의 호출
    const originalRequest = res.config;
    originalRequest.headers.Authorization = `Bearer ${result.accessToken}`;
    return await axios(originalRequest);
  }

  return res;
};

//fail response
const responseFail = (err: AxiosError) => {
  console.log('response fail  err.......');
  return Promise.reject(err);
};

jwtAxios.interceptors.request.use(beforeReq, requestFail);
jwtAxios.interceptors.response.use(beforeRes, responseFail);
export default jwtAxios;
