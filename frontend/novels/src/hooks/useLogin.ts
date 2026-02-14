import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { loginPostAsync, logout } from '../reducers/loginSlice';
import type { AppDispatch, RootState } from '../reducers/store';
import type { LoginFormParam } from '../types/user';

const useLogin = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();

  //로그인 상태
  const loginState = useSelector((state: RootState) => state.auth);
  //로그인 여부
  const isLogin = loginState.email ? true : false;

  //여기서 await 필요없다고 나와도 LoginForm.jsx 에서 then() 사용해야 해서 필요
  const doLogin = async (params: LoginFormParam) => {
    const data = await dispatch(loginPostAsync(params)).unwrap();
    return data;
  };

  //로그아웃할 때 호출될 함수
  const doLogout = () => dispatch(logout());

  const moveToPath = (path: string) =>
    navigate({ pathname: path }, { replace: true });
  const moveToLogin = () =>
    navigate({ pathname: '/member/login' }, { replace: true });

  return { loginState, isLogin, doLogin, doLogout, moveToPath, moveToLogin };
};

export default useLogin;
