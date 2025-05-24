/**
 * 시/도-시/군/구 형태의 디렉토리 리스트를 평탄화하는 함수
 * @param {Array} directories - 백엔드에서 받은 디렉토리 배열
 * @returns {Array} 평탄화된 디렉토리 배열
 */
function flattenDirectoryList(directories) {
  const result = []

  for (const dir of directories) {
    const { sido, sigungu } = dir

    // 1단계: sido
    result.push({
      id: sido,
      name: sido,
      level: 0,
      parent: null,
      hasChildren: sigungu.length > 0
    })

    // 2단계: sigungu
    for (const name of sigungu) {
      result.push({
        id: `${sido}/${name}`,
        name,
        level: 1,
        parent: sido,
        hasChildren: false
      })
    }
  }

  return result
}

export default flattenDirectoryList
