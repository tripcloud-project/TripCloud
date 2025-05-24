import api from '@/lib/api'

export async function setThumbnail(fileId, region) {
    try {
        const response = await api.put(
            `/gallery/photo/${fileId}/thumbnail`,
            {},
            { params: { region } }
        )

        return response.data
    } catch (error) {
        console.error('썸네일 변경 중 오류:', error)
        return { status: 'error', error }
    }
}
