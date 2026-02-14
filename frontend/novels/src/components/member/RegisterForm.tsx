import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const initState = {
  email: '',
  pw: '',
  nickname: '',
};

const RegisterForm = () => {
  // 회원가입 이동
  const navigate = useNavigate();

  //화면단 데이터
  const [registerParam, setRegisterParam] = useState(initState);
  const { email, pw, nickname } = registerParam;

  const handleChange = (e) => {
    setRegisterParam({
      ...registerParam,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <form className="mt-6 flex flex-col gap-2.5">
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
      <input
        type="password"
        onChange={handleChange}
        placeholder="닉네임"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        name="nickname"
        value={nickname}
      />
      <div className="p-2 text-center">
        <button
          type="submit"
          className="mx-1 my-6 rounded-[3px] bg-sky-500 px-4.5 py-3 text-[1.2em] text-white hover:bg-sky-800"
        >
          회원가입
        </button>
      </div>
    </form>
  );
};

export default RegisterForm;
