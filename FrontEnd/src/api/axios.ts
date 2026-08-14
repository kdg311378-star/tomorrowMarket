import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true, // 모든 요청에 자동으로 쿠키(HttpOnly 포함)를 포함합니다.
});

// 기존의 Authorization: Bearer <token> 추가 인터셉터는 HttpOnly 쿠키 전환으로 인해 삭제되었습니다.

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        // 401 Unauthorized 에러이고, 아직 재시도하지 않은 요청일 경우
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true; // 무한 루프 방지 플래그

            // 만약 새로고침 요청 자체가 401을 반환했다면 (리프레시 토큰도 만료됨), 무한 루프에 빠지지 않도록 처리
            if (originalRequest.url === '/auth/refresh') {
                return Promise.reject(error);
            }

            try {
                // 쿠키 기반이므로 파라미터나 바디 없이 요청만 보내면 백엔드에서 HttpOnly 쿠키(refreshToken)를 읽어들임
                await axios.post(`${import.meta.env.VITE_API_BASE_URL}/auth/refresh`, {}, {
                    withCredentials: true
                });

                // 토큰 갱신 성공 시, 아까 실패했던 원래 요청을 다시 시도
                return api(originalRequest);
            } catch (refreshError) {
                // 리프레시 토큰마저 만료되었거나 유효하지 않은 경우 -> 완전히 로그아웃 처리
                localStorage.removeItem("isAuthenticated");
                sessionStorage.removeItem("isAuthenticated");
                window.dispatchEvent(new Event("authChange"));
                
                // 로그인 페이지로 강제 이동
                window.location.href = '/login';
                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }
);

export default api;
