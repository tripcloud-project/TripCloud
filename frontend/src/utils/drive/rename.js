// src/utils/rename.js

import api from '@/lib/api'

/**
 * 파일 이름 변경
 */
export async function renameFile(fileId, newFilename) {
  try {
    const response = await api.put(`/gallery/rename/${fileId}`, {
      filename: newFilename,
    })

    return response.data
  } catch (error) {
    console.error('파일 이름 변경 중 오류:', error)
    return { status: 'error', error }
  }
}

/**
 * 폴더(prefix) 이름 변경
 */
export async function renameDirectory(oldPrefix, newPrefix) {
  try {
    const response = await api.put(`/gallery/rename`, {
      oldPrefix,
      newPrefix,
    })

    return response.data
  } catch (error) {
    console.error('디렉토리 이름 변경 중 오류:', error)
    return { status: 'error', error }
  }
}

/**
 * 단일 파일 이름 변경 (prompt 포함)
 */
export async function renameSingleFile({ file, onRename }) {
  if (!file || file.type === 'folder') return

  const currentName = file.name
  const dotIndex = currentName.lastIndexOf('.')
  const nameOnly = currentName.slice(0, dotIndex)
  const extension = currentName.slice(dotIndex)

  const newNameOnly = prompt('새 이름을 입력하세요', nameOnly)
  if (newNameOnly && newNameOnly !== nameOnly) {
    const newFullName = newNameOnly + extension
    const result = await renameFile(file.id, newFullName)
    if (result.status === 'success' && typeof onRename === 'function') {
      await onRename()
    }
  }
}
