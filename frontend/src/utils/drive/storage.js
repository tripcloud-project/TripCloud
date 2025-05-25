import api from '@/lib/api'

export async function getStorage() {
  try {
    const response = await api.get('/members/storage')
    return response.data
  } catch (error) {
    console.error('유저 드라이브 사용량 조회 중 오류 발생:', error)
    return { status: 'error', error }
  }
}
