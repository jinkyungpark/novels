import { useCallback, useEffect, useState } from 'react';
import { getNovel } from '../apis/bookApi';
import { initialNovel, type Novel } from '../types/novel';

// 커스텀 훅으로 작성한 이유는 재사용성을 위해서

export const useNovel = (id?: string) => {
  const [serverData, setServerData] = useState<Novel>(initialNovel);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown>(null);

  const fetchData = useCallback(async () => {
    if (!id) return;

    try {
      setLoading(true);
      const data = await getNovel(id);
      setServerData(data);
    } catch (e) {
      setError(e);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    console.log('서버 요청');
    fetchData();
  }, [fetchData]);

  return { serverData, loading, error };
};
