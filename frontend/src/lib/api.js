import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})



import { useAuthStore } from '@/stores/useAuthStore'
api.interceptors.request.use(
  async (config) => {
    console.log('[요청 발신]: ', config.method, config.url, config.data)
    const authStore = useAuthStore();
    console.log("authStore.token: ", authStore.token)


    if(authStore.token){
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
    console.log(response)
    console.log('[응답 수신 1]: ', response.status, response.data)
    return response
  },
  async (error) => {
    console.log('[오류 수신 1]: ', error)
    return Promise.reject(error)
  },
)




export default api
