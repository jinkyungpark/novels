import { Outlet } from 'react-router-dom';
import BasicLayout from '../../layout/BasicLayout';

const MemberPage = () => {
  return (
    <BasicLayout>
      <h1 className="text-[32px]">Register or Login</h1>
      <Outlet />
    </BasicLayout>
  );
};

export default MemberPage;
