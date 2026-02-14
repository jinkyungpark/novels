import { useCallback, useEffect, useState } from 'react';
import { getList, putAvailable } from '../apis/bookApi';
import { initialPageState, type Novel, type PageResult } from '../types/novel';

// 커스텀 훅으로 작성한 이유는 재사용성을 위해서

export const useNovelList = (
  page: number,
  size: number,
  genre: number,
  keyword: string,
) => {
  const [serverData, setServerData] =
    useState<PageResult<Novel>>(initialPageState);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown>(null);

  const fetchData = useCallback(async () => {
    console.log('검색 ', genre, keyword);
    page = page + 1; // 서버와 안 맞기 때문에
    try {
      setLoading(true);
      const data = await getList({ page, size, genre, keyword });
      setServerData(data);
    } catch (e) {
      setError(e);
    } finally {
      setLoading(false);
    }
  }, [page, size, genre, keyword]);

  const toggleAvailable = useCallback(
    async (id: string, currentAvailable: boolean) => {
      const result = await putAvailable({
        id: id,
        available: !currentAvailable,
      });
      console.log('putAvailable ', result);
      fetchData();
    },
    [fetchData],
  );

  useEffect(() => {
    console.log('서버 요청');
    fetchData();
  }, [fetchData]);

  return { serverData, loading, error, toggleAvailable };
};
