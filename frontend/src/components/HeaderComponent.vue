<template>
  <header class="bg-white border-b border-gray-200 shadow-sm top-0 z-50">
    <div class="mx-auto px-4 sm:px-6 lg:px-8 w-full">
      <div class="flex justify-between items-center h-16">
        <!-- 왼쪽: 로고 -->
        <div class="flex-1 flex items-center">
          <button
            @click="handleLogoClick"
            class="flex items-center space-x-2 text-sage-green hover:text-deep-sage transition-colors duration-200"
          >
            <div
              class="w-8 h-8 bg-sage-green text-white rounded-lg flex items-center justify-center font-bold text-sm"
            >
              M
            </div>
            <span class="text-xl font-bold text-gray-800">TripCloud</span>
          </button>
        </div>

        <!-- 가운데: 네비게이션 메뉴 -->
        <nav class="hidden md:flex items-center space-x-1">
          <button
            @click="handleDriveClick"
            class="flex items-center space-x-2 px-4 py-2 text-gray-600 hover:text-sage-green hover:bg-sage-green/10 rounded-lg transition-all duration-200 font-medium"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M7 4V2a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1v2h4a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V5a1 1 0 0 1 1-1h4zM9 3v1h6V3H9z"
              />
            </svg>
            <span>드라이브</span>
          </button>
          <button
            @click="handleBoardClick"
            class="flex items-center space-x-2 px-4 py-2 text-gray-600 hover:text-sage-green hover:bg-sage-green/10 rounded-lg transition-all duration-200 font-medium"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
              />
            </svg>
            <span>게시판</span>
          </button>
        </nav>

        <!-- 오른쪽: 사용자 메뉴 -->
        <div class="flex-1 flex justify-end items-center">
          <div v-if="isLoggedIn" class="relative flex items-center space-x-2">
            <button
              @click="handleMyPage"
              class="flex items-center space-x-2 px-3 py-2 text-gray-600 hover:text-sage-green hover:bg-sage-green/10 rounded-lg transition-all duration-200"
            >
              <img
                v-if="user.profileImage"
                :src="user.profileImage"
                alt="프로필"
                class="w-8 h-8 rounded-full object-cover border-2 border-sage-green"
              />
              <div v-else class="w-8 h-8 bg-gray-300 rounded-full flex items-center justify-center">
                <svg
                  class="w-4 h-4 text-gray-600"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                  />
                </svg>
              </div>
              <span class="hidden sm:block font-medium">{{ user.name }}</span>
            </button>

            <!-- 로그아웃 버튼 -->
            <button
              @click="handleLogout"
              class="flex items-center space-x-2 px-3 py-2 text-gray-600 hover:text-sage-green hover:bg-sage-green/10 rounded-lg transition-all duration-200"
            >
              <span class="hidden sm:block font-medium">로그아웃</span>
            </button>
          </div>
          <button
            v-else
            @click="handleLogin"
            class="flex items-center space-x-2 bg-sage-green hover:bg-deep-sage text-white px-4 py-2 rounded-lg transition-all duration-200 font-medium shadow-sm"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1"
              />
            </svg>
            <span>로그인</span>
          </button>
        </div>
      </div>

      <!-- 모바일 네비게이션 -->
      <div class="md:hidden border-t border-gray-200">
        <div class="flex justify-center space-x-1 py-2">
          <button
            @click="handleDriveClick"
            class="flex items-center space-x-2 px-4 py-2 text-gray-600 hover:text-sage-green hover:bg-sage-green/10 rounded-lg transition-all duration-200 font-medium"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M7 4V2a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1v2h4a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V5a1 1 0 0 1 1-1h4zM9 3v1h6V3H9z"
              />
            </svg>
            <span>드라이브</span>
          </button>
          <button
            @click="handleBoardClick"
            class="flex items-center space-x-2 px-4 py-2 text-gray-600 hover:text-sage-green hover:bg-sage-green/10 rounded-lg transition-all duration-200 font-medium"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
              />
            </svg>
            <span>게시판</span>
          </button>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore'
import api from '@/lib/api'

const router = useRouter()
const authStore = useAuthStore()

// 로그인 상태 및 사용자 정보
const isLoggedIn = computed(() => !!authStore.token)
const user = ref({})

// 페이지 이동 함수들
const handleLogoClick = () => {
  router.push('/')
  console.log('메인 페이지로 이동')
}

const handleDriveClick = () => {
  router.push('/drive')
  console.log('드라이브 페이지로 이동')
}

const handleBoardClick = () => {
  router.push('/board')
  console.log('게시판 페이지로 이동')
}

const handleLogin = () => {
  router.push('/login')
  console.log('로그인 페이지로 이동')
}

const handleMyPage = () => {
  router.push('/profile')
  console.log('마이페이지로 이동')
}

const handleLogout = () => {
  api.post('/auth/logout')
  authStore.clearToken()
  window.location.href = '/'
  console.log('로그아웃 완료, 메인 페이지로 이동')
}

onMounted(() => {
  authStore.loadToken()
})
</script>

<style scoped>
/* 커스텀 색상 정의 */
.text-sage-green {
  color: #8fbc8f;
}

.hover\:text-deep-sage:hover {
  color: #6b8e6b;
}

.bg-sage-green {
  background-color: #8fbc8f;
}

.hover\:bg-deep-sage:hover {
  background-color: #6b8e6b;
}

.border-sage-green {
  border-color: #8fbc8f;
}

.hover\:bg-sage-green\/10:hover {
  background-color: rgba(143, 188, 143, 0.1);
}
</style>
