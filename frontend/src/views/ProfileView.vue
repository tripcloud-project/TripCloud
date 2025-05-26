<template>
  <div class="flex flex-col items-center justify-center bg-soft-white">
    <!-- Header Section -->
    <div class="flex flex-col items-center mb-8">
      <div class="w-16 h-16 mb-4 flex items-center justify-center bg-sage-green rounded-full">
        <i class="fas fa-cloud text-white text-2xl"></i>
      </div>
      <h1 class="text-3xl font-bold text-deep-sage mb-2">TripCloud</h1>
      <p class="text-sage-green text-lg">내 정보</p>
    </div>

    <!-- Profile Container -->
    <div class="bg-white p-8 rounded-xl shadow-md w-full max-w-2xl border border-sage-green">
      <!-- Profile Header -->
      <div class="flex flex-col items-center mb-8">
        <div class="relative mb-4">
          <div class="w-24 h-24 rounded-full overflow-hidden border-4 border-sage-green">
            <img
              v-if="userInfo.profileImage"
              :src="userInfo.profileImage"
              :alt="userInfo.name"
              class="w-full h-full object-cover"
            />
            <div v-else class="w-full h-full bg-sage-green flex items-center justify-center">
              <i class="fas fa-user text-white text-3xl"></i>
            </div>
          </div>
        </div>
        <h2 class="text-2xl font-semibold text-deep-sage">{{ userInfo.name }}</h2>
        <!-- <div v-if="userInfo.mainBadge" class="mt-2 px-3 py-1 bg-light-brown rounded-full">
          <span class="text-brown text-sm font-medium">{{ userInfo.mainBadge }}</span>
        </div> -->
      </div>

      <!-- User Information -->
      <div class="space-y-6">
        <!-- Email Section -->
        <div class="border-b border-gray-200 pb-4">
          <label class="block text-gray-700 mb-2 font-medium">이메일</label>
          <div class="flex items-center">
            <div class="flex items-center mr-3">
              <i class="fas fa-envelope text-sage-green"></i>
            </div>
            <span class="text-gray-800">{{ userInfo.email }}</span>
          </div>
        </div>

        <!-- Storage Section -->
        <div class="border-b border-gray-200 pb-4">
          <label class="block text-gray-700 mb-3 font-medium">스토리지 사용량</label>
          <div class="space-y-3">
            <div class="flex justify-between items-center text-sm">
              <span class="text-gray-600">사용중</span>
              <span class="text-gray-800 font-medium"
                >{{ formatStorage(userInfo.usedStorage) }} /
                {{ formatStorage(userInfo.maxStorage) }}</span
              >
            </div>
            <div class="w-full bg-gray-200 rounded-full h-3">
              <div
                class="h-3 rounded-full transition-all duration-300"
                :class="getStorageBarColor()"
                :style="{ width: storagePercentage + '%' }"
              ></div>
            </div>
            <div class="flex justify-between items-center text-sm">
              <span class="text-gray-600">{{ storagePercentage }}% 사용중</span>
              <span class="text-gray-600"
                >{{ formatStorage(userInfo.maxStorage - userInfo.usedStorage) }} 남음</span
              >
            </div>
          </div>
        </div>

        <!-- Badge Section -->
        <!-- <div v-if="userInfo.mainBadge">
          <label class="block text-gray-700 mb-2 font-medium">현재 칭호</label>
          <div class="flex items-center">
            <div class="flex items-center mr-3">
              <i class="fas fa-award text-sage-green"></i>
            </div>
            <span class="text-gray-800">{{ userInfo.mainBadge }}</span>
          </div>
        </div> -->
      </div>

      <!-- Action Buttons -->
      <div class="mt-8 space-y-3">
        <button
          @click="editProfile"
          class="w-full bg-sage-green py-3 rounded-lg font-medium transition duration-300 border border-deep-sage hover:bg-deep-sage hover:border-green-900 flex justify-center items-center cursor-pointer"
        >
          <i class="fas fa-edit mr-2"></i>
          프로필 수정
        </button>

        <button
          @click="withdraw"
          class="w-full bg-gray-100 text-gray-700 py-3 rounded-lg font-medium transition duration-300 border border-gray-300 hover:bg-gray-200 cursor-pointer"
        >
          <i class="fas fa-sign-out-alt mr-2"></i>
          회원 탈퇴
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/useAuthStore'
import { useRouter } from 'vue-router'
import api from '@/lib/api'

const authStore = useAuthStore()
const router = useRouter()

// User information (this would typically come from API)
const userInfo = ref({
  email: 'user@example.com',
  name: '김여행자',
  profileImage: '', // URL to profile image
  usedStorage: 2.5 * 1024 * 1024 * 1024, // 2.5GB in bytes
  maxStorage: 10 * 1024 * 1024 * 1024, // 10GB in bytes
  mainBadge: '여행 마스터',
})

// Computed properties
const storagePercentage = computed(() => {
  return Math.round((userInfo.value.usedStorage / userInfo.value.maxStorage) * 100)
})

// Methods
const formatStorage = (bytes) => {
  if (bytes === 0) return '0 B'

  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))

  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const getStorageBarColor = () => {
  const percentage = storagePercentage.value
  if (percentage >= 90) return 'bg-red-500'
  if (percentage >= 70) return 'bg-yellow-500'
  return 'bg-sage-green'
}

const editProfile = () => {
  router.push('/profile/edit')
}

// Fetch user data on component mount
const fetchUserData = async () => {
  try {
    const response = await api.get('/members/me')
    userInfo.value = response.data.result
  } catch (error) {
    console.error('Failed to fetch user data:', error)
  }
}

const withdraw = async () => {
  const confirmed = confirm('정말로 회원 탈퇴하시겠습니까? 탈퇴 후에는 계정을 복구할 수 없습니다.')

  if (!confirmed) {
    return // 사용자가 취소한 경우 아무 것도 하지 않음
  }

  try {
    await api.delete('/members/me')
    const authStore = useAuthStore()
    authStore.clearToken()
    alert('회원 탈퇴가 완료되었습니다.')
    router.push('/login')
  } catch (error) {
    console.error('회원 탈퇴 실패:', error)
    alert('회원 탈퇴에 실패했습니다. 다시 시도해주세요.')
  }
}

onMounted(() => {
  fetchUserData()
})
</script>

<style scoped>
/* Custom styles for smooth transitions */
.transition-all {
  transition: all 0.3s ease-in-out;
}

/* Profile image hover effect */
.relative:hover .absolute {
  transform: scale(1.1);
}
</style>
