/**
 * 시/도-시/군/구 형태의 디렉토리 리스트를 평탄화하는 함수 (대한민국 루트 포함)
 * @param {Array} directories - 백엔드에서 받은 디렉토리 배열
 * @returns {Array} 평탄화된 디렉토리 배열
 */
function flattenDirectoryTree(directories) {
  const result = []

  // 0단계: 루트 "대한민국"
  result.push({
    id: '/',
    name: '대한민국',
    level: 0,
    parent: null,
    hasChildren: directories.length > 0
  })

  for (const dir of directories) {
    const { sido, sigungu } = dir
    const sidoId = `/${sido}/`

    // 1단계: 시도
    result.push({
      id: sidoId,
      name: sido,
      level: 1,
      parent: '/',
      hasChildren: sigungu.length > 0
    })

    // 2단계: 시군구
    for (const name of sigungu) {
      result.push({
        id: `${sidoId}${name}/`,
        name: name,
        level: 2,
        parent: sidoId,
        hasChildren: false
      })
    }
  }

  return result
}

export default flattenDirectoryTree
