import { useParams } from 'react-router-dom';
import Error from '../../components/common/Error';
import NovelDetail from '../../components/novels/NovelDetail';
import { useNovel } from '../../hooks/useNovel';
import BasicLayout from '../../layout/BasicLayout';

const NovelDetails = () => {
  // 주소줄에 있는 값 가져오기
  const { id } = useParams<{ id: string }>();

  const { serverData, loading, error } = useNovel(id);
  //if (loading) return <Loading />;
  if (error) return <Error />;

  return (
    <BasicLayout>
      <h1 className="text-[32px]">Book Details</h1>
      <NovelDetail novel={serverData} />
    </BasicLayout>
  );
};

export default NovelDetails;
