import { createBrowserRouter } from 'react-router-dom';
import Home from '../components/novels/Home';
import novelRouter from './novelRouter';
import Error from '../components/common/Error';
import memberRouter from './memberRouter';
import MemberPage from '../pages/member/MemberPage';

const rootRouter = createBrowserRouter([
  {
    path: '/', // pr index:true 로 대체
    Component: Home,
  },
  {
    path: '/novels', // pr index:true 로 대체
    children: novelRouter(),
  },
  {
    path: '/member', // pr index:true 로 대체
    Component: MemberPage,
    children: memberRouter(),
  },
  {
    path: '*',
    Component: Error,
  },
]);

export default rootRouter;
