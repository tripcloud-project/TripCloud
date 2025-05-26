<template>
  <div class="bg-gray-50 flex flex-col h-full">
    <!-- 메인 컨텐츠 -->
    <main class="flex-grow container mx-auto px-6 py-8 h-full">
      <!-- 로딩 상태 -->
      <div v-if="loading" class="flex justify-center items-center h-96">
        <div class="text-center">
          <div
            class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"
          ></div>
          <p class="text-gray-600">지도를 불러오는 중...</p>
        </div>
      </div>

      <!-- 에러 상태 -->
      <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-lg p-6 text-center">
        <div class="text-red-600 mb-4">
          <svg class="w-12 h-12 mx-auto mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.728-.833-2.498 0L4.316 15.5c-.77.833.192 2.5 1.732 2.5z"
            />
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-red-800 mb-2">지도를 불러올 수 없습니다</h3>
        <p class="text-red-600 mb-4">{{ error }}</p>
        <button
          @click="retryLoad"
          class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors"
        >
          다시 시도
        </button>
      </div>

      <!-- 지도 컨테이너 -->
      <div
        v-else
        class="bg-white rounded-lg shadow-md overflow-hidden p-4 h-full bg-gradient-to-br from-blue-50 via-blue-100 to-blue-200"
      >
        <!-- 지도 -->
        <div class="relative">
          <!-- 뒤로가기 버튼 (지도 내부 좌상단) -->
          <button
            v-if="selectedSido !== '전국'"
            @click="goBack"
            class="absolute top-4 left-4 z-30 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors flex items-center space-x-2 shadow-lg"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M10 19l-7-7m0 0l7-7m-7 7h18"
              />
            </svg>
            <span>전국으로</span>
          </button>

          <!-- 호버된 지역 정보 -->
          <div
            v-if="hoveredRegion"
            class="absolute top-4 left-1/2 transform -translate-x-1/2 z-20 bg-black/80 text-white px-4 py-2 rounded-lg shadow-lg pointer-events-none"
          >
            {{ hoveredRegion }}
          </div>

          <!-- SVG 지도 -->
          <div
            class="w-full select-none cursor-pointer"
            style="height: 100%; max-height: 800px"
            @click="handleMapClick"
            @contextmenu="handleRightClick"
            @mouseover="handleMouseOver"
            @mouseout="handleMouseOut"
            v-html="selectedMap"
          />
        </div>
      </div>
    </main>

    <!-- 관광 명소 추천 모달 -->
    <div
      v-if="showTouristModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      @click="closeTouristModal"
    >
      <div
        class="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4 max-h-[80vh] overflow-hidden"
        @click.stop
      >
        <!-- 모달 헤더 -->
        <div class="flex items-center justify-between p-6 border-b">
          <h2 class="text-xl font-bold text-gray-800">{{ selectedRegionForTourism }} 관광 명소</h2>
          <button
            @click="closeTouristModal"
            class="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        <!-- 모달 내용 -->
        <div class="p-6 overflow-y-auto max-h-[60vh]">
          <!-- 로딩 상태 -->
          <div v-if="touristLoading" class="flex justify-center items-center py-12">
            <div class="text-center">
              <div
                class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mx-auto mb-4"
              ></div>
              <p class="text-gray-600">관광 명소를 불러오는 중...</p>
            </div>
          </div>

          <!-- 에러 상태 -->
          <div v-else-if="touristError" class="text-center py-12">
            <div class="text-red-600 mb-4">
              <svg
                class="w-12 h-12 mx-auto mb-2"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.728-.833-2.498 0L4.316 15.5c-.77.833.192 2.5 1.732 2.5z"
                />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-red-800 mb-2">정보를 불러올 수 없습니다</h3>
            <p class="text-red-600 mb-4">{{ touristError }}</p>
            <button
              @click="loadTouristSpots(selectedRegionForTourism)"
              class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors"
            >
              다시 시도
            </button>
          </div>

          <div v-else-if="touristSpots.length > 0" class="space-y-4">
            <a
              v-for="spot in touristSpots"
              :key="spot.id"
              :href="spot.placeURL"
              target="_blank"
              rel="noopener noreferrer"
              class="block bg-gray-50 rounded-lg p-4 hover:bg-gray-100 transition-colors"
            >
              <div class="flex items-start space-x-4">
                <div class="flex-shrink-0">
                  <div class="w-16 h-16 bg-blue-100 rounded-lg flex items-center justify-center">
                    <svg
                      class="w-8 h-8 text-blue-500"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                      />
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                      />
                    </svg>
                  </div>
                </div>
                <div class="flex-grow">
                  <h3 class="font-semibold text-gray-800 mb-1">{{ spot.name }}</h3>
                  <p class="text-gray-600 text-xs mb-1">{{ spot.address }}</p>
                  <p class="text-gray-600 text-xs mb-1">{{ spot.category }}</p>
                </div>
              </div>
            </a>
          </div>

          <!-- 데이터 없음 -->
          <div v-else class="text-center py-12">
            <div class="text-gray-400 mb-4">
              <svg
                class="w-16 h-16 mx-auto mb-4"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M19 11a7 7 0 01-7 7m0 0a7 7 0 01-7-7m7 7v4m0 0H8m4 0h4m-4-8a3 3 0 01-3-3V5a3 3 0 116 0v6a3 3 0 01-3 3z"
                />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-600 mb-2">관광 명소 정보가 없습니다</h3>
            <p class="text-gray-500">해당 지역의 관광 명소 정보를 찾을 수 없습니다.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { mapFiles } from '@/assets/map/mapImports'
import api from '@/lib/api'
import { useMapStore } from '@/stores/map.js'
import { storeToRefs } from 'pinia'
const mapStore = useMapStore()
const { selectedSido } = storeToRefs(mapStore)

// 함수 꺼내 쓰기
const { selectFolder } = mapStore

// 반응형 상태
const selectedMap = ref(null)
const regionImageMap = ref({})
const loading = ref(false)
const error = ref(null)
const hoveredRegion = ref(null)

// 관광 명소 모달 관련 상태
const showTouristModal = ref(false)
const selectedRegionForTourism = ref('')
const touristSpots = ref([])
const touristLoading = ref(false)
const touristError = ref(null)

// 지도 로드 함수 (개선됨)
async function loadMap(name) {
  if (loading.value) return

  loading.value = true
  error.value = null

  try {
    // 지도 파일 확인
    if (!mapFiles[`/src/assets/map/${name}.svg`]) {
      throw new Error(`지도 파일을 찾을 수 없습니다: ${name}.svg`)
    }

    // SVG 파일 로드
    let rawSvg = await mapFiles[`/src/assets/map/${name}.svg`]()

    // API 호출 파라미터 설정
    const params = selectedSido.value === '전국' ? {} : { sido: selectedSido.value }

    // 이미지 데이터 가져오기
    const response = await api.get('/gallery/photo/thumbnail', {
      ...(Object.keys(params).length > 0 && { params }),
    })

    // 지역별 이미지 맵 생성
    regionImageMap.value = response.data.result.reduce((map, item) => {
      if (!item.sigungu && item.presignedURL) {
        map[item.sido] = item.presignedURL
      } else if (item.sigungu && item.presignedURL) {
        map[item.sigungu] = item.presignedURL
      }
      return map
    }, {})

    // SVG 처리 및 스타일링
    selectedMap.value = processSvg(rawSvg, regionImageMap.value)
  } catch (err) {
    console.error('지도 로드 실패:', err)
    error.value = err.message || '지도를 불러오는 중 오류가 발생했습니다.'
  } finally {
    loading.value = false
  }
}

function normalizeId(id) {
  return id.replace(/\s+/g, '-')
}

function deNormalizedId(id) {
  return id.replace(/-/g, ' ')
}

const idMap = {}
function processSvg(rawSvg, imageMap) {
  // 0. ID 매핑 생성 (원본 id → 새 id)
  for (const key of Object.keys(imageMap)) {
    idMap[key] = normalizeId(key)
  }

  // 1. 기존 크기 및 스타일 제거
  let processedSvg = rawSvg.replace(/(width|height)="[^"]*"/g, '').replace(/style="[^"]*"/g, '')

  // 2. id 값과 참조값 치환
  Object.entries(idMap).forEach(([originalId, newId]) => {
    const escOriginalId = originalId.replace(/[-/\\^$*+?.()|[\]{}]/g, '\\$&') // 정규표현식 escape
    const idPattern = new RegExp(`id=["']${escOriginalId}["']`, 'g')
    const hrefPattern = new RegExp(`href=["']#${escOriginalId}["']`, 'g')
    const clipPathPattern = new RegExp(`clip-path=["']url\\(#${escOriginalId}\\)["']`, 'g')

    processedSvg = processedSvg
      .replace(idPattern, `id="${newId}"`)
      .replace(hrefPattern, `href="#${newId}"`)
      .replace(clipPathPattern, `clip-path="url(#${newId})"`)
  })

  // 3. SVG 태그에 스타일 추가
  processedSvg = processedSvg.replace(
    /<svg([^>]+)>/,
    `<svg$1 style="width: 100%; height: 100%; max-height: 1000px " preserveAspectRatio="xMidYMid meet">`,
  )

  // 4. 기본 스타일 추가
  processedSvg = processedSvg.replace(
    /<svg([^>]*)>/,
    `<svg$1>
    <defs>
      <linearGradient id="subtleGradient" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" style="stop-color:#f1f5f9;stop-opacity:1" />
        <stop offset="100%" style="stop-color:#e2e8f0;stop-opacity:1" />
      </linearGradient>

      <linearGradient id="accentGradient" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" style="stop-color:#0ea5e9;stop-opacity:0.8" />
        <stop offset="100%" style="stop-color:#0284c7;stop-opacity:0.9" />
      </linearGradient>
    </defs>
    <style>
      path, polygon, g {
        fill: url(#subtleGradient);
        stroke: #94a3b8 
        stroke-width: 1.5; /* 두께 증가 */
        transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
        filter: drop-shadow(2px 3px 4px rgba(148, 163, 184, 0.4)); /* 그림자 강화 */
      }

      path:hover, polygon:hover {
        fill: #8ba888;
        stroke: #5a7157;
        stroke-width: 2.5; /* 호버시 더 두껍게 */
        filter: drop-shadow(3px 5px 8px rgba(3, 105, 161, 0.5));
        transform: translateY(-1px);
      }

      .has-image {
        fill: url(#imagePattern);
        filter: drop-shadow(3px 4px 6px rgba(0,0,0,0.2));
      }
    </style>`,
  )

  // 5. 클립패스 정의 추가
  const clipPaths = Object.keys(imageMap)
    .map((regionId) => {
      const normalizedId = idMap[regionId]
      return `
          <clipPath id="clip-${normalizedId}">
            <use href="#${normalizedId}" />
          </clipPath>
        `
    })
    .join('')

  processedSvg = processedSvg.replace(/<svg([^>]*)>/, `<svg$1><defs>${clipPaths}</defs>`)

  // 6. 이미지 요소 추가 - SVG 문자열에서 직접 파싱
  const imageElements = Object.entries(imageMap)
    .map(([regionId, imageUrl]) => {
      const normalizedId = idMap[regionId]
      const bounds = calculateRegionBoundsFromString(processedSvg, normalizedId)

      if (bounds) {
        const imageSize = Math.max(bounds.width, bounds.height)
        const imageX = bounds.centerX - imageSize / 2
        const imageY = bounds.centerY - imageSize / 2

        return `
          <image
            href="${imageUrl}"
            x="${imageX}"
            y="${imageY}"
            width="${imageSize}"
            height="${imageSize}"
            clip-path="url(#clip-${normalizedId})"
            preserveAspectRatio="xMidYMid slice"
            style="pointer-events: none; opacity: 0.8;"
          />
        `
      }

      return ''
    })
    .join('\n')

  // 7. 이미지 요소를 SVG에 삽입
  processedSvg = processedSvg.replace('</svg>', `${imageElements}</svg>`)

  return processedSvg
}

// SVG 문자열에서 특정 요소의 경계박스를 계산하는 함수
function calculateRegionBoundsFromString(svgString, elementId) {
  // 해당 ID를 가진 요소 찾기
  const elementRegex = new RegExp(
    `<(path|polygon|circle|rect|ellipse)([^>]*id=["']${elementId}["'][^>]*)>`,
    'i',
  )
  const match = svgString.match(elementRegex)

  if (!match) return null
  const attributes = match[2]
  return calculatePathBounds(attributes)
}

// Path 요소의 경계박스 계산 (간단한 버전)
function calculatePathBounds(attributes) {
  const dMatch = attributes.match(/d=["']([^"']+)["']/)
  if (!dMatch) return null

  const pathData = dMatch[1]
  const coords = extractCoordsFromPath(pathData)

  if (coords.length === 0) return null

  const xCoords = coords.map((c) => c.x).filter((x) => !isNaN(x))
  const yCoords = coords.map((c) => c.y).filter((y) => !isNaN(y))

  const minX = Math.min(...xCoords)
  const maxX = Math.max(...xCoords)
  const minY = Math.min(...yCoords)
  const maxY = Math.max(...yCoords)

  return {
    x: minX,
    y: minY,
    width: maxX - minX,
    height: maxY - minY,
    centerX: (minX + maxX) / 2,
    centerY: (minY + maxY) / 2,
  }
}
function extractCoordsFromPath(pathData) {
  const coords = []
  const commands = pathData.match(/[MLHVCSQTAZ][^MLHVCSQTAZ]*/gi) || []

  let currentX = 0,
    currentY = 0

  commands.forEach((command) => {
    const type = command[0].toUpperCase()
    const args = command
      .slice(1)
      .trim()
      .split(/[\s,]+/)
      .map(parseFloat)
      .filter((n) => !isNaN(n))

    switch (type) {
      case 'M':
      case 'L':
        for (let i = 0; i < args.length; i += 2) {
          currentX = args[i]
          currentY = args[i + 1]
          coords.push({ x: currentX, y: currentY })
        }
        break
      case 'H':
        args.forEach((x) => {
          currentX = x
          coords.push({ x: currentX, y: currentY })
        })
        break
      case 'V':
        args.forEach((y) => {
          currentY = y
          coords.push({ x: currentX, y: currentY })
        })
        break
      case 'C':
        for (let i = 0; i < args.length; i += 6) {
          coords.push({ x: args[i], y: args[i + 1] })
          coords.push({ x: args[i + 2], y: args[i + 3] })
          currentX = args[i + 4]
          currentY = args[i + 5]
          coords.push({ x: currentX, y: currentY })
        }
        break
    }
  })

  return coords
}

// 관광 명소 데이터 로드 함수
async function loadTouristSpots() {
  touristLoading.value = true
  touristError.value = null
  touristSpots.value = []

  try {
    const params =
      selectedSido.value === '전국'
        ? { sido: hoveredRegion.value }
        : { sido: selectedSido.value, sigungu: hoveredRegion.value }
    const response = await api.get('/attraction', {
      params: params,
    })

    touristSpots.value = response.data.result
  } catch (err) {
    console.error('관광 명소 로드 실패:', err)
    touristError.value = err.message || '관광 명소 정보를 불러오는 중 오류가 발생했습니다.'
  } finally {
    touristLoading.value = false
  }
}

// 이벤트 핸들러들
const handleMapClick = (event) => {
  let id = event.target.id

  if (id) {
    id = id.replace(/-/g, ' ')
    if (selectedSido.value === '전국') {
      // 시도
      selectedSido.value = id
      selectFolder(`/${id}/`)
    } else {
      // 시군구
      selectFolder(`/${selectedSido.value}/${id}/`)
    }
  }
}

const handleRightClick = (event) => {
  event.preventDefault() // 기본 컨텍스트 메뉴 방지

  const id = event.target.id
  if (id) {
    selectedRegionForTourism.value = id
    showTouristModal.value = true
    loadTouristSpots(id)
  }
}

const handleMouseOver = (event) => {
  const id = event.target.id
  if (id) {
    hoveredRegion.value = deNormalizedId(id)
  }
}

const handleMouseOut = () => {
  hoveredRegion.value = null
}

// 관광 명소 모달 닫기
const closeTouristModal = () => {
  showTouristModal.value = false
  selectedRegionForTourism.value = ''
  touristSpots.value = []
  touristError.value = null
}

// 유틸리티 함수들
const goBack = () => {
  selectedSido.value = '전국'
  selectFolder('/')
}

const retryLoad = () => {
  loadMap(selectedSido.value)
}

// 와처
watch(selectedSido, (newValue) => {
  loadMap(newValue)
})

// 생명주기
onMounted(() => {
  loadMap(selectedSido.value)
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 커스텀 스크롤바 */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
