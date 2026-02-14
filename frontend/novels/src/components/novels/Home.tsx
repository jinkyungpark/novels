import { useSearchParams } from 'react-router-dom';
import { useNovelList } from '../../hooks/useNovelList';
import BasicLayout from '../../layout/BasicLayout';
import { genres } from '../../utils/novelUtil';
import Error from '../common/Error';
import Loading from '../common/Loading';
import NovelList from './NovelList';

const Home = () => {
  // ? 로 처리되는 값 검색 파라미터
  const [params, setParams] = useSearchParams();
  // 파라메터 값을 변수에 담기
  const genre = Number(params.get("genre") ?? 0);
  const keyword = params.get("keyword") ?? "";
  const currentPage = Number(params.get("page") ?? 0);

  const { serverData, loading, error, toggleAvailable } = useNovelList(
    currentPage,
    10,
    genre,
    keyword,
  );

  // 페이지 나누기 페이지 수(검색해서 가져오기)
  const offset = currentPage * serverData.pageRequestDTO.size;
  const pageCount = Math.ceil(
    serverData.totalCount / serverData.pageRequestDTO.size,
  );

  console.log(`Loading items from ${pageCount} to ${offset}`);

  const handlePageClick = (event: { selected: number }) => {
    const next = new URLSearchParams(params);
    next.set("page", String(event.selected));
    setParams(next);
  };

  if (loading) return <Loading />;
  if (error) return <Error />;

  const handleSearch = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;

    const next = new URLSearchParams(params);
    next.set("page", "0");

    if (name === "keyword") next.set("keyword", value);
    if (name === "genre") next.set("genre", value ? value : "0");

    setParams(next);
  };

  return (
    <BasicLayout>
      <header className="mb-6 flex">
        <h1 className="grow text-[32px]">Book List</h1>
        <div>
          {/* leading-tight : 높이 같게 하려고 */}
          <input
            type="text"
            name="keyword"
            placeholder="Search by title or author"
            className="w-50 rounded-sm border-2 border-gray-300 p-2 text-[.9em] leading-tight outline-0"
            value={keyword}
            onChange={handleSearch}
          />
          <select
            name="genre"
            className="ml-2 rounded-sm border-2 border-gray-300 p-2 text-[.9em] leading-tight outline-0"
            onChange={handleSearch}
            value={genre}
          >
            <option value="">All Genres</option>
            {genres.map((g, idx) => (
              <option key={idx} value={idx + 1}>
                {g}
              </option>
            ))}
          </select>
        </div>
      </header>
      {loading ? (
        <Loading />
      ) : (
        <NovelList
          dtoList={serverData.dtoList}
          toggle={toggleAvailable}
          handlePageClick={handlePageClick}
          pageCount={pageCount}
          currentPage={currentPage}
        />
      )}
    </BasicLayout>
  );
};

export default Home;
