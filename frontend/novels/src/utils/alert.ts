import Swal from 'sweetalert2';

// void 반환하여 Promise를 반환하지 않도록 함
// Promise 반환 시 호출한 쪽의 컴포넌트(NovelList)가 Promise를 반환하게 되어
// React 컴포넌트가 아닌 상태가 되어 오류 발생

export const alertError = (message: string) =>
  void Swal.fire({
    icon: 'error',
    title: 'Error',
    text: message,
    confirmButtonText: '확인',
  });

export const alertConfirm = (message: string) =>
  void Swal.fire({
    icon: 'question',
    title: '확인',
    text: message,
    showCancelButton: true,
    confirmButtonText: '확인',
    cancelButtonText: '취소',
  });
