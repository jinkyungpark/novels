import React from 'react';
// import { deleteOne, getBook } from '../../api/bookApi';

import { useNavigate } from 'react-router-dom';
//import useCustomMove from '../../hooks/useCustomMove';
// import Loading from '../Loading';
import { deleteOne } from '../../apis/bookApi';
import { type Novel } from '../../types/novel';
import { alertError } from '../../utils/alert';
import { getBookEmoji, renderStars } from '../../utils/novelUtil';

// http://localhost:5173/books/271?page=6&size=10&genre=0&keyword=
// 경로가 이렇게 생성되는데  path="/books/:id"
// Route 경로를 이렇게 설정했기 때문에 271 을 가지고 올 수 있는 것
// 먼저 가져온 곳은 NovelDetails.jsx 임임

const NovelDetail = ({ novel }: { novel: Novel }) => {
  const navigate = useNavigate();

  const deleteRow = async (id: number) => {
    try {
      const result = await deleteOne(id);
      console.log('삭제 후 ', result);
      //삭제 후 home 으로 이동
      navigate('/');
    } catch (error) {
      console.error('Error deleting data:', error);
      alertError('Failed to delete data.');
    }
  };

  const handleDelete = (id: string) => {
    const confirmed = window.confirm('Delete this book?');
    if (!confirmed) return;
    deleteRow(Number(id));
  };

  return (
    <>
      <section className="mt-6 flex border-t border-neutral-200 p-5">
        <div className="first:grow">
          <h2 className="mb-2.5 text-2xl">{novel.title}</h2>
          <p className="my-1.25">
            <strong>Author : {novel.author}</strong>
          </p>
          <p className="my-1.25">
            <strong>Genre : {novel.genreName}</strong>
          </p>
          <p className="my-1.25">
            <strong>Published Date : {novel.publishedDate}</strong>
          </p>
          <p className="my-1.25">
            <strong>Rating : {renderStars(novel.rating)}</strong>
            <span className="ml-1 text-[1.1em] tracking-widest text-sky-500"></span>
          </p>
          <p className="my-1.25">
            <strong>
              Available : {novel.available ? 'Available' : 'Not Available'}
            </strong>
          </p>
        </div>
        <div className="text-[8.6em]">{getBookEmoji(novel.id)}</div>
      </section>
      <section className="text-center">
        <button
          className="mx-1 my-6 rounded-[5px] bg-sky-600 px-4 py-3 text-[1.2em] text-white hover:bg-sky-900"
          onClick={() => navigate(`../edit/${novel.id}`)}
        >
          Edit Book
        </button>
        <button
          className="mx-1 my-6 rounded-[5px] bg-red-600 px-4 py-3 text-[1.2em] text-white hover:bg-red-900"
          onClick={() => handleDelete(novel.id)}
        >
          Delete Book
        </button>
      </section>
    </>
  );
};

// props의 참조가 같다면 리렌더링 방지
export default React.memo(NovelDetail);
