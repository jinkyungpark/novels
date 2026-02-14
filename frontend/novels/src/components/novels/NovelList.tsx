import ReactPaginate from 'react-paginate';
import { useNavigate } from 'react-router-dom';
import type { Novel, NovelListProps } from '../../types/novel';
import { getBookEmoji, renderStars } from '../../utils/novelUtil';

const NovelList = ({
  dtoList,
  toggle,
  handlePageClick,
  pageCount,
  currentPage,
}: NovelListProps) => {
  const navigate = useNavigate();

  // 에러가 존재 시 이렇게도 alert 창으로 띄워볼 수도 있음
  // useEffect(() => {
  //   if (error) {
  //     alertError('Failed to load data.');
  //   }
  // }, [error]);

  return (
    <>
      <section className="p-0">
        {dtoList.map((novel: Novel) => (
          <article
            className="mb-2.5 flex items-center rounded-[5px] border-2 border-stone-200 bg-white p-4"
            key={novel.id}
          >
            <div className="pr-4 first:text-5xl">{getBookEmoji(novel.id)}</div>
            <div className="grow">
              <h3>{novel.title}</h3>
              <p>
                {novel.author} / {novel.genreName}
              </p>
              <p className="text-2xl text-sky-400">
                {renderStars(novel.rating)}
              </p>
            </div>
            <div className="flex flex-col text-[0.9em]">
              <button
                onClick={() => toggle(novel.id, novel.available)}
                className={`m-1 w-25 rounded-[3px] bg-sky-500 py-2.5 text-center text-white hover:bg-sky-700 ${novel.available ? '' : 'opacity-33'}`}
              >
                {novel.available ? 'Available' : 'Not Available'}
              </button>
              <a
                href=""
                onClick={() => navigate(`/novels/${novel.id}`)}
                className="m-1 w-25 rounded-[3px] bg-stone-100 px-1.5 py-3 text-center"
              >
                Details
              </a>
            </div>
          </article>
        ))}
      </section>
      <ReactPaginate
        breakLabel="..."
        nextLabel="next >"
        onPageChange={handlePageClick}
        pageRangeDisplayed={3}
        pageCount={pageCount}
        previousLabel="< previous"
        renderOnZeroPageCount={null}
        containerClassName="flex justify-center gap-2 mt-4"
        pageClassName="px-3 py-1 border rounded cursor-pointer"
        activeClassName="bg-orange-500 text-white"
        previousClassName="px-3 py-1 border rounded cursor-pointer"
        nextClassName="px-3 py-1 border rounded cursor-pointer"
        forcePage={currentPage}
      />
    </>
  );
};

export default NovelList;
