export type User = {
  email: string;
  pw: string;
  nickname: string;
  social: boolean;
};

// 로그인 상태 타입
export interface LoginState {
  email: string;
  nickname?: string;
  error?: boolean;
}

export type LoginFormParam = {
  email: string;
  pw: string;
};

export type LoginResponse = {
  email: string;
  nickname: string;
  social: boolean;
  accessToken: string;
  roleNames: string[];
  password?: string;
};

// export type AuthState = {
//   email: string;
//   nickname: '';
//   social: false;
//   accessToken: '';
//   roleNames: string[];
// };
