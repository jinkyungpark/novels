import LoginForm from '../components/member/LoginForm';
import RegisterForm from '../components/member/RegisterForm';

const memberRouter = () => {
  return [
    {
      path: 'login',
      Component: LoginForm,
    },
    {
      path: 'register',
      Component: RegisterForm,
    },
    // {
    //   path: 'profile/:id',
    //   Component: Profile,
    // },
  ];
};

export default memberRouter;
