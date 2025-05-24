import api from '@/lib/api'

export async function restoreFiles({ prefixList, fileIdList }) {
  try {
    const response = await api.put('/gallery/trash/restore', {
      prefixList, fileIdList 
    })

    return response.data
  } catch (error) {
    console.error('파일 복원원 중 오류 발생:', error)
    return { status: 'error', error }
  }
}