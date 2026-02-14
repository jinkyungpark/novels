export const initialNovel = {
  id: '',
  title: '',
  author: '',
  publishedDate: '',
  available: false,
  genre: 0,
  genreName: '',
  rating: 0,
  email: '',
};

export type Novel = {
  id: string;
  title: string;
  author: string;
  publishedDate: string;
  available: boolean;
  genre: number;
  genreName: string;
  rating: number;
  email: string | null;
};

export type NovelPut = {
  id: string;
  available: boolean;
};

export type NovelUpSert = {
  novel: Novel;
  onSubmit: (formData: Novel) => void;
  onCancel: (id: string) => void;
};

export type NovelPost = Omit<Novel, 'id'>;

export type NovelListProps = {
  dtoList: Novel[];
  toggle: (id: string, currentAvailable: boolean) => void;
  handlePageClick: (e: { selected: number }) => void;
  pageCount: number;
  currentPage: number;
};

// 페이지 나누기 타입
export type PageRequetDTO = {
  page: number;
  size: number;
  keyword: string;
  genre: number;
};
export type PageResult<T> = {
  dtoList: T[];
  pageNumList: number[];
  pageRequestDTO: PageRequetDTO;
  prev: false;
  next: false;
  totalCount: number;
  prevPage: number;
  nextPage: number;
  totalPage: number;
  current: number;
};

export const initialPageState: PageResult<Novel> = {
  dtoList: [],
  pageNumList: [],
  pageRequestDTO: { page: 0, size: 20, keyword: '', genre: 0 },
  prev: false,
  next: false,
  totalCount: 0,
  prevPage: 0,
  nextPage: 0,
  totalPage: 0,
  current: 0,
};

export type PageParam = {
  page: number;
  size: number;
  genre?: number;
  keyword?: string;
};
