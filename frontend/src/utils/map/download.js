import api from '@/lib/api'

export async function downloadFiles({ prefixList, fileIdList, currentPrefix }) {
  try {
    const response = await api.post(
      '/gallery/download',
      { prefixList, fileIdList, currentPrefix },
      { responseType: 'blob' },
    )

    const blob = await response.data
    const contentDisposition = response.headers.get('content-disposition')
    let filename = 'download'

    if (contentDisposition) {
      const match = contentDisposition.match(/filename\*=UTF-8''(.+)/)
      if (match && match[1]) {
        filename = decodeURIComponent(match[1])
      } else {
        const fallback = contentDisposition.match(/filename="([^"]+)"/)
        if (fallback && fallback[1]) {
          filename = fallback[1]
        }
      }
    }

    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = filename
    a.click()
    URL.revokeObjectURL(url)
  } catch (error) {
    console.error('Error during file download:', error)
  }
}
