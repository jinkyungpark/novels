import AddNovel from '../pages/novels/AddNovel';
import EditBook from '../pages/novels/EditNovel';
import BookDetails from '../pages/novels/NovelDetails';

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
