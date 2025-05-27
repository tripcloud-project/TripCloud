import api from '@/lib/api'

/**
 * 검색 요청을 수행합니다.
 * @param {string} type - 검색 타입 ('file', 'hashtag', 'description')
 * @param {string} query - 검색어
 */
export async function searchFiles(type, query) {
  try {
    let params = {}

    switch (type) {
      case 'file':
        params.keyword = query
        break
      case 'hashtag':
        params.hashtag = query
        break
      case 'description':
        params.description = query
        break
      default:
        throw new Error('알 수 없는 검색 타입')
    }

    const response = await api.get('/gallery/search', {
      params,
    })

    return response.data
  } catch (error) {
    console.error('파일 검색 중 오류 발생:', error)
    return { status: 'error', error }
  }
}
