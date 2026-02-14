import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useLogin from '../../hooks/useLogin';

//import useCustomLogin from './../../hooks/useCustomLogin';

const initState = {
  email: '',
  pw: '',
};

const LoginForm = () => {
  // 회원가입 이동
  const navigate = useNavigate();
  // const dispatch = useDispatch<AppDispatch>();

  //화면단 데이터
  const [loginParam, setLoginParam] = useState(initState);
  const { email, pw } = loginParam;

  // 커스텀로그인 가져오기
  const { doLogin, moveToPath } = useLogin();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setLoginParam({
      ...loginParam,
      [name]: value,
    });
  };

  //로그인 클릭 시 리듀서 호출(useDispatch() 사용해서 애플리케이션 상태 변경)
  //const dispatch = useDispatch();

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log('loginParam ', loginParam);
    // dispatch(login(loginParam));
    // loginSlice 비동기 호출
    // dispatch(loginPostAsync(loginParam))
    //   .unwrap() // 로그인 후 넘어오는 데이터 받아오기 할때 필요
    //   .then((data) => {
    //     console.log('after unwrap....');
    //     console.log(data);
    //     if (data.error) {
    //       alert('이메일과 비밀번호를 확인해 주세요');
    //     } else {
    //       alert('로그인 성공');
    //       navigate({ pathname: "/" }, { replace: true });
    //     }
    //   });

    try {
      const data = await doLogin(loginParam);
      console.log('after doLogin....', data);
      if (data?.accessToken) {
        alert('로그인 성공');
        moveToPath('/');
      }
    } catch (e) {
      console.error(e);
      alert('이메일과 비밀번호를 확인해 주세요');
    }
  }


  return (
    <form
      className="mt-6 flex flex-col gap-2.5"
      method="post"
      onSubmit={onSubmit}
    >
      <input
        name="email"
        placeholder="아이디"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        onChange={handleChange}
        value={email}
      />
      <input
        type="password"
        onChange={handleChange}
        placeholder="비밀번호"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        name="pw"
        value={pw}
      />
      <div className="p-2 text-center">
        <button
          type="submit"
          className="mx-1 my-6 rounded-[3px] bg-sky-500 px-4.5 py-3 text-[1.2em] text-white hover:bg-sky-800"
        >
          로그인
        </button>
        <button
          type="button"
          onClick={() => navigate('/member/register')}
          className="mx-1 my-6 rounded-[3px] bg-red-700 px-4.5 py-3 text-[1.2em] text-white hover:bg-red-900"
        >
          회원가입
        </button>
      </div>
    </form>
  )
};

export default LoginForm;
