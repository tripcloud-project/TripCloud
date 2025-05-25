/**
 * 중첩된 디렉토리 트리를 평탄화된 배열로 변환하는 함수
 * @param {Object} tree - 백엔드에서 받은 트리 객체
 * @param {number} level - 계층 수준 (기본값: 0)
 * @param {string|null} parent - 부모 prefix (기본값: null)
 * @returns {Array} 평탄화된 디렉토리 배열
 */
function flattenDirectoryTree(tree, level = 0, parent = null) {
  const result = []

  // 최초 호출 시 루트 노드 추가
  if (level === 0 && parent === null) {
    result.push({
      id: '/',
      name: 'My Files',
      level: 0,
      parent: null,
      hasChildren: Object.keys(tree).length > 0,
    })
    const childItems = flattenDirectoryTree(tree, 1, '/')
    result.push(...childItems)
    return result
  }

  for (const name in tree) {
    if (Object.hasOwn(tree, name)) {
      const node = tree[name]
      const fullPath = '/' + node.prefix // 예: '/여행/상주/'
      const children = node.children || {}
      const hasChildren = Object.keys(children).length > 0

      result.push({
        id: fullPath,
        name,
        level,
        parent,
        hasChildren,
      })

      // 하위 디렉토리도 재귀적으로 추가
      if (hasChildren) {
        const childItems = flattenDirectoryTree(children, level + 1, fullPath)
        result.push(...childItems)
      }
    }
  }

  return result
}

export default flattenDirectoryTree
