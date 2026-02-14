

import { useNavigate, useParams } from 'react-router-dom';
import { putBook } from '../../apis/bookApi';
import Error from '../../components/common/Error';
import Loading from '../../components/common/Loading';
import NovelForm from '../../components/novels/NovelForm';
import { useNovel } from '../../hooks/useNovel';
import BasicLayout from '../../layout/BasicLayout';
import type { Novel } from '../../types/novel';
import MoveToLoginReturn from '../../components/member/MoveToLoginReturn';
import useLogin from '../../hooks/useLogin';

const EditNovel = () => {
  const { id } = useParams<{ id: string }>();

  const navigate = useNavigate();

  // 데이터 가져오기
  const { serverData, loading, error } = useNovel(id);

  // 로그인이 필요한 페이지
  const { isLogin } = useLogin();

  if (!isLogin) {
    return <MoveToLoginReturn />;
  }

  if (loading) return <Loading />;
  if (error) return <Error />;
  // 수정을 눌렀을 때 데이터 보내기(put)
  const handleSubmit = async (formData: Novel) => {
    // 데이터가 넘어올 때 genreName 은 바뀐 상태는 아님(gid 만 필요)
    console.log('폼 데이터 전송 ', formData);

    try {
      const id = await putBook(formData);
      console.log('수정 후 ', id);
      navigate(`/novels/${id}`);
    } catch (e) {
      console.error('Error updating book:', e);
      <Error />;
    }
  };

  const handleCancel = (id: string) => {
    // 이전 페이지로 이동
    navigate(`../${id}`);
  };

  return (
    <BasicLayout>
      <h1 className="text-[32px]">Edit Book</h1>
      <NovelForm
        novel={serverData}
        onSubmit={handleSubmit}
        onCancel={handleCancel}
      />
    </BasicLayout>
  );
};

export default EditNovel;
