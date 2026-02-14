import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import useLogin from '../../hooks/useLogin';
import type { RootState } from '../../reducers/store';


const NavBar = () => {

  // useSelector() 로그인 상태 감지
  // Redux store의 전역 상태 중 configureStore에서 auth라는 key로 등록된 slice를 가져온다
  const authState = useSelector((state: RootState) => state.auth);

  const { doLogout, moveToPath } = useLogin();

  // const dispatch = useDispatch();
  const logoutState = () => {
    //로그아웃 클릭 시 상태 변경
    // dispatch(logout());
    doLogout();
    alert("로그아웃되었습니다.");
    moveToPath("/");
  };


  return (
    <nav className="flex h-14 items-center justify-between bg-sky-600 px-5 text-white">
      <div className="text-xl font-bold">Book Manager</div>
      <ul className="m-0 flex list-none gap-4 p-0">
        <li>
          <Link to="/" className="underline-offset-1 hover:underline">
            Home
          </Link>
        </li>
        {authState.email ?
          <>
            <li>
              <Link
                to="/novels/add"
                className="underline-offset-1 hover:underline"
              >
                Add Book
              </Link>
            </li>
            <li>
              <button className="underline-offset-1 hover:underline" onClick={logoutState}>
                Logout
              </button>
            </li>
          </>
          :
          <li>
            <Link
              to="/member/login"
              className="underline-offset-1 hover:underline"
            >
              Login
            </Link>
          </li>
        }
      </ul>
    </nav>
  );
};

export default NavBar;
