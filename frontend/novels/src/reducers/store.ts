import { configureStore } from '@reduxjs/toolkit';
import { loginSlice } from './loginSlice';

export const store = configureStore({
  reducer: {
    // 리듀서 접근 시 이름: 실제 접근되는 리듀서
    auth: loginSlice.reducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
