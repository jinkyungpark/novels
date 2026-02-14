// 지난번에는 container/modules 에 작성했던 부분을 한 번에 작업
// actionType 을 자동 생성
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { postLogin } from '../apis/memberApi';
import type { LoginFormParam, LoginResponse } from '../types/user';
import { getCookie, removeCookie, setCookie } from '../utils/cookieUtils';

// memberApi.js 의 로그인 포스트 호출
// createAsyncThunk('loginPostAsync', 비동기처리할 함수)
// 초기 상태 설정

const initialState: LoginResponse = {
  email: '',
  nickname: '',
  social: false,
  accessToken: '',
  roleNames: [],
};

// 쿠키에서 로그인 정보 로딩
const loadMemberCookie = () => {
  const memberInfo = getCookie('member');
  if (!memberInfo) return null;
  return memberInfo;
};

//
export const loginPostAsync = createAsyncThunk<LoginResponse, LoginFormParam>(
  'loginPostAsync',
  (param) => {
    return postLogin(param);
  },
);

// 리덕스 툴킷 사용하면 간단하게 작성할 수 있음
// 작성된 name 을 기준으로 액션 타입이 자동 생성됨
// auth/login
// auth/logout

// action.payload => 액션이 상태를 바꾸기 위해 들고 오는 값
export const loginSlice = createSlice({
  name: 'auth', // 네임스페이스
  initialState: loadMemberCookie() || initialState,
  reducers: {
    login: (state, action) => {
      console.log('login');
      state.email = action.payload.email;
    },
    logout: (state) => {
      console.log('logout');
      removeCookie('member');
      state.email = '';
    },
  },
  // Redux Toolkit에서 “이 slice 밖에서 만들어진 액션에도 반응하게 만드는 곳"
  // 비동기 액션 처리( loginPostAsync )에 대한 상태 관리
  extraReducers: (builder) => {
    builder
      // 로그인 성공 (서버 응답 도착)
      .addCase(loginPostAsync.fulfilled, (state, action) => {
        console.log('fulfilled');

        const payload = action.payload;
        state.email = payload.email;
        state.nickname = payload.nickname;
        state.social = payload.social;
        state.accessToken = payload.accessToken;
        state.roleNames = payload.roleNames;

        // 쿠키에 최소한의 로그인 정보만 저장
        if (payload) {
          setCookie(
            'member',
            JSON.stringify(payload),
            1, // 1일간 유효
          );
        }
      })
      // 로그인 API 요청 시작
      .addCase(loginPostAsync.pending, () => {
        console.log('pending');
      })
      // 로그인 실패
      .addCase(loginPostAsync.rejected, () => {
        console.log('rejected');
      });
  },
});

// loginSlice 내부에 선언된 함수들을 외부에 노출
// 애플리케이션 상태 변화에 사용할 액션 내보내기
export const { login, logout } = loginSlice.actions;
export default loginSlice.reducer;
