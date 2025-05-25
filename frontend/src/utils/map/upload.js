import api from '@/lib/api'

/**
 * 파일/폴더 업로드 API 요청
 * @param {FormData} formData - 업로드할 파일들 + prefix
 * @returns {object} response.data 또는 { status: 'error' }
 */
export async function uploadToServer(formData) {
  try {
    const response = await api.post('/gallery/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    return response.data
  } catch (err) {
    console.error('업로드 실패:', err)
    return { status: 'error', error: err }
  }
}
