import { useNavigate } from 'react-router-dom';
import { postBook } from '../../apis/bookApi';
import NovelForm from '../../components/novels/NovelForm';
import BasicLayout from '../../layout/BasicLayout';
import { initialNovel, type Novel } from '../../types/novel';
import Error from '../../components/common/Error';

const AddNovel = () => {
  const navigate = useNavigate();
  const handleSubmit = async (formData: Novel) => {
    // 데이터가 넘어올 때 genreName 은 바뀐 상태는 아님(gid 만 필요)
    console.log('신규 등록 ', formData);

    try {
      const id = await postBook(formData);
      console.log('신규 후 ', id);
      navigate('/');
    } catch (error) {
      <Error />;
    }
  };
  const handleCancel = async () => {
    history.back();
  };

  return (
    <BasicLayout>
      <h1 className="text-[32px]">Add New Book</h1>
      <NovelForm
        novel={initialNovel}
        onSubmit={handleSubmit}
        onCancel={handleCancel}
      />
    </BasicLayout>
  );
};

export default AddNovel;
