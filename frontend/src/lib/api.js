import axios from 'axios'
import { useAuthStore } from '@/stores/useAuthStore'
import router from '@/router/index'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use(
  async (config) => {
    console.log('[요청 발신]: ', config.method, config.url, config.data)
    const authStore = useAuthStore()
    if (authStore.token) {
      config.headers['Authorization'] = `${authStore.token}`
    }
    return config
  },
  (error) => {
    console.log('[요청 실패]: ', error)
    return Promise.reject(error)
  },
)
api.interceptors.response.use(
  (response) => {
    console.log('[응답 수신]: ', response.status, response.data)
    return response
  },
  async (error) => {
    const originalRequest = error.config
    const authStore = useAuthStore()

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise(function (resolve, reject) {
          failedQueue.push({
            resolve: (token) => {
              originalRequest.headers['Authorization'] = `${token}`
              resolve(api(originalRequest))
            },
            reject: (err) => reject(err),
          })
        })
      }

      originalRequest._retry = true
      isRefreshing = true

      try {
        // ✅ 쿠키에 저장된 refreshToken으로 서버에서 새 토큰 발급
        const response = await axios.get(
          `${import.meta.env.VITE_API_BASE_URL}/auth/refresh-token`,
          {
            withCredentials: true,
          },
        )

        const newAccessToken = response.headers['authorization']
        authStore.setToken(newAccessToken)

        processQueue(null, newAccessToken)

        originalRequest.headers['Authorization'] = `${newAccessToken}`
        return api(originalRequest)
      } catch (err) {
        processQueue(err, null)
        alert('로그인이 필요합니다.')
        // authStore.logout()
        router.push('/login')
        return Promise.reject(err)
      } finally {
        isRefreshing = false
      }
    }

    console.log('[오류 수신]: ', error)
    return Promise.reject(error)
  },
)

// 응답 인터셉터
let isRefreshing = false
let failedQueue = []

const processQueue = (error, token) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error)
    } else {
      prom.resolve(token)
    }
  })
  failedQueue = []
}

export default api
