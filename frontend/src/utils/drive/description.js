import api from '@/lib/api'

export async function descriptionSingleFile({ fileId, description }) {
  try {
    const response = await api.put(`/gallery/photo/${fileId}/description`, {
      description
    })
    return response.data
  } catch (error) {
    console.error('설명 수정 중 오류:', error)
    return { status: 'error', error }
  }
}
