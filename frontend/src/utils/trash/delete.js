import api from '@/lib/api'

export async function deleteFiles({ prefixList, fileIdList }) {
  try {
    const response = await api.delete('/gallery/trash/delete', {
      data: { prefixList, fileIdList },
    })

    return response.data
  } catch (error) {
    console.error('파일 삭제 중 오류 발생:', error)
    return { status: 'error', error }
  }
}
