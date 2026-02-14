import { useState } from 'react';
import { type Novel, type NovelUpSert } from '../../types/novel';
import { genres } from '../../utils/novelUtil';

const NovelForm = ({ novel, onSubmit, onCancel }: NovelUpSert) => {
  const [formData, setFormData] = useState<Novel>(novel);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const target = e.target;
    const { name } = target;

    setFormData({
      ...formData,
      [name]:
        target instanceof HTMLInputElement && target.type === 'checkbox'
          ? target.checked
          : target.value,
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form className="mt-6 flex flex-col gap-3" onSubmit={handleSubmit}>
      <input
        name="title"
        placeholder="Title"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        value={formData.title || ''}
        onChange={handleChange}
      />
      <input
        name="author"
        placeholder="Author"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        onChange={handleChange}
        value={formData.author || ''}
      />
      <select
        name="genre"
        className="rounded-xs border-2 border-stone-300 p-2"
        value={formData.genre || ''}
        onChange={handleChange}
      >
        <option value="">Select Genre</option>
        {genres.map((genre, idx) => (
          <option key={idx} value={idx + 1}>
            {genre}
          </option>
        ))}
      </select>
      <input
        name="publishedDate"
        type="date"
        placeholder="Published Date"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        value={formData.publishedDate || ''}
        onChange={handleChange}
      />
      <input
        name="rating"
        type="number"
        placeholder="Rating (1-5)"
        min="0"
        max="5"
        value={formData.rating || 0}
        onChange={handleChange}
        className="rounded-xs border-2 border-stone-300 p-2"
      />
      <label>
        <input
          name="available"
          type="checkbox"
          className="mr-1.5 rounded-xs border-2 border-stone-300 p-2"
          onChange={handleChange}
          checked={formData.available}
        />
        Available
      </label>
      <div className="p-2 text-center">
        <button
          type="submit"
          className="mx-1 my-6 rounded-[3px] bg-sky-500 px-4.5 py-3 text-[1.2em] text-white hover:bg-sky-800"
        >
          {novel.id ? 'Update' : 'Add'}
        </button>
        <button
          type="button"
          className="mx-1 my-6 rounded-[3px] bg-red-700 px-4.5 py-3 text-[1.2em] text-white hover:bg-red-900"
          onClick={() => onCancel(novel.id)}
        >
          Cancel
        </button>
      </div>
    </form>
  );
};

export default NovelForm;
