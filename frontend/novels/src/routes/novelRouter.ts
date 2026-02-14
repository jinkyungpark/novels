import AddBook from '../pages/novels/AddNovel';
import BookDetails from '../pages/novels/NovelDetails';
import EditBook from '../pages/novels/EditNovel';
import AddNovel from '../pages/novels/AddNovel';

const novelRouter = () => {
  return [
    {
      path: 'add',
      Component: AddNovel,
    },
    {
      path: 'edit/:id',
      Component: EditBook,
    },
    {
      path: ':id',
      Component: BookDetails,
    },
  ];
};

export default novelRouter;
