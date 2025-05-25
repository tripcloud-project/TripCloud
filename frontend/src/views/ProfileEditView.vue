<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-soft-white">
    <!-- Header Section -->
    <div class="flex flex-col items-center mb-8">
      <div class="w-16 h-16 mb-4 flex items-center justify-center bg-sage-green rounded-full">
        <i class="fas fa-cloud text-white text-2xl"></i>
      </div>
      <h1 class="text-3xl font-bold text-deep-sage mb-2">TripCloud</h1>
      <p class="text-sage-green text-lg">프로필 수정</p>
    </div>

    <!-- Edit Form Container -->
    <div class="bg-white p-8 rounded-xl shadow-md w-full max-w-md border border-sage-green">
      <!-- Profile Image Section -->
      <div class="flex flex-col items-center mb-8">
        <div class="relative mb-4">
          <div class="w-24 h-24 rounded-full overflow-hidden border-4 border-sage-green">
            <img
              v-if="profileImagePreview || userInfo.profileImage"
              :src="profileImagePreview || userInfo.profileImage"
              alt="Profile"
              class="w-full h-full object-cover"
            />
            <div v-else class="w-full h-full bg-sage-green flex items-center justify-center">
              <i class="fas fa-user text-white text-3xl"></i>
            </div>
          </div>
          <button
            @click="triggerFileInput"
            class="absolute -bottom-2 -right-2 w-8 h-8 bg-deep-sage rounded-full flex items-center justify-center hover:bg-green-900 transition duration-300"
          >
            <i class="fas fa-camera text-white text-sm"></i>
          </button>
          <input
            ref="fileInput"
            type="file"
            accept="image/*"
            @change="handleFileChange"
            class="hidden"
          />
        </div>
        <p class="text-sm text-gray-500 text-center">프로필 사진을 클릭하여 변경</p>
      </div>

      <!-- Name Input -->
      <div class="mb-5">
        <label for="name" class="block text-gray-700 mb-2 font-medium">이름</label>
        <div class="relative">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <i class="fas fa-user text-sage-green"></i>
          </div>
          <input
            type="text"
            id="name"
            v-model="userInfo.name"
            class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sage-green focus:border-transparent"
            placeholder="이름을 입력해주세요"
            :class="{ 'border-red-300 focus:ring-red-300': nameError }"
          />
        </div>
        <p v-if="nameError" class="mt-1 text-sm text-red-500">{{ nameError }}</p>
      </div>

      <!-- New Password Input -->
      <div class="mb-5">
        <label for="newPassword" class="block text-gray-700 mb-2 font-medium">새 비밀번호</label>
        <div class="relative">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <i class="fas fa-key text-sage-green"></i>
          </div>
          <input
            :type="showNewPassword ? 'text' : 'password'"
            id="newPassword"
            v-model="newPassword"
            class="w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sage-green focus:border-transparent"
            placeholder="새 비밀번호를 입력해주세요 (선택사항)"
            autocomplete="new-password"
            :class="{ 'border-red-300 focus:ring-red-300': newPasswordError }"
          />
          <button
            type="button"
            @click="showNewPassword = !showNewPassword"
            class="absolute inset-y-0 right-0 pr-3 flex items-center"
          >
            <i
              :class="showNewPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"
              class="text-gray-400 hover:text-sage-green"
            ></i>
          </button>
        </div>
        <p v-if="newPasswordError" class="mt-1 text-sm text-red-500">{{ newPasswordError }}</p>
        <p class="mt-1 text-xs text-gray-500">비밀번호를 변경하지 않으려면 비워두세요</p>
      </div>

      <!-- Confirm New Password Input -->
      <div v-if="newPassword" class="mb-6">
        <label for="confirmPassword" class="block text-gray-700 mb-2 font-medium"
          >새 비밀번호 확인</label
        >
        <div class="relative">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <i class="fas fa-key text-sage-green"></i>
          </div>
          <input
            :type="showConfirmPassword ? 'text' : 'password'"
            id="confirmPassword"
            v-model="confirmPassword"
            class="w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sage-green focus:border-transparent"
            placeholder="새 비밀번호를 다시 입력해주세요"
            :class="{ 'border-red-300 focus:ring-red-300': confirmPasswordError }"
          />
          <button
            type="button"
            @click="showConfirmPassword = !showConfirmPassword"
            class="absolute inset-y-0 right-0 pr-3 flex items-center"
          >
            <i
              :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"
              class="text-gray-400 hover:text-sage-green"
            ></i>
          </button>
        </div>
        <p v-if="confirmPasswordError" class="mt-1 text-sm text-red-500">
          {{ confirmPasswordError }}
        </p>
      </div>

      <!-- Action Buttons -->
      <div class="space-y-3">
        <button
          @click="handleSave"
          class="w-full bg-sage-green py-3 rounded-lg font-medium transition duration-300 border border-deep-sage hover:bg-deep-sage hover:border-green-900 flex justify-center items-center cursor-pointer"
          :disabled="isLoading"
        >
          <span v-if="!isLoading">저장하기</span>
          <div v-else class="flex items-center">
            <svg
              class="animate-spin -ml-1 mr-2 h-5 w-5 text-white"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
            >
              <circle
                class="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                stroke-width="4"
              ></circle>
              <path
                class="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
              ></path>
            </svg>
            <span>저장 중...</span>
          </div>
        </button>

        <button
          @click="goBack"
          class="w-full bg-light-brown py-3 rounded-lg font-medium transition duration-300 border border-brown hover:bg-brown hover:border-[#a8937f] cursor-pointer"
        >
          취소
        </button>
      </div>
    </div>

    <!-- Footer -->
    <div class="mt-8 text-center text-gray-500 text-sm">
      <p>© 2025 TripCloud. All rights reserved.</p>
      <div class="flex justify-center mt-2 space-x-4">
        <a href="#" class="hover:text-sage-green transition duration-300 cursor-pointer"
          >Privacy Policy</a
        >
        <a href="#" class="hover:text-sage-green transition duration-300 cursor-pointer"
          >Terms of Service</a
        >
        <a href="#" class="hover:text-sage-green transition duration-300 cursor-pointer"
          >Contact Us</a
        >
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/lib/api'

const router = useRouter()

// Form state
const userInfo = ref({})

const currentPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const profileImageFile = ref(null)
const profileImagePreview = ref('')
const isLoading = ref(false)

// Password visibility
const showNewPassword = ref(false)
const showConfirmPassword = ref(false)

// Error states
const nameError = ref('')
const currentPasswordError = ref('')
const newPasswordError = ref('')
const confirmPasswordError = ref('')

// File input reference
const fileInput = ref(null)

// Methods
const triggerFileInput = () => {
  fileInput.value?.click()
}

async function imageHandler(file) {
  if (!file) return

  console.log(file)
  // 서버에 이미지 업로드
  const formData = new FormData()
  formData.append('image', file)

  try {
    const response = await api.post('/posts/image', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })

    profileImageFile.value = response.data.result
  } catch (error) {
    console.error('이미지 업로드 실패', error)
  }
}

const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    // Validate file size (5MB limit)
    if (file.size > 5 * 1024 * 1024) {
      alert('파일 크기가 5MB를 초과할 수 없습니다.')
      return
    }

    // Validate file type
    if (!file.type.startsWith('image/')) {
      alert('이미지 파일만 업로드 가능합니다.')
      return
    }
    imageHandler(file)

    // Create preview
    const reader = new FileReader()
    reader.onload = (e) => {
      profileImagePreview.value = e.target.result
    }
    reader.readAsDataURL(file)
  }
}

const validateForm = () => {
  // Reset errors
  nameError.value = ''
  currentPasswordError.value = ''
  newPasswordError.value = ''
  confirmPasswordError.value = ''

  let isValid = true

  // Validate name
  if (!userInfo.value.name.trim()) {
    nameError.value = '이름을 입력해주세요.'
    isValid = false
  } else if (userInfo.value.name.trim().length < 2) {
    nameError.value = '이름은 최소 2자 이상이어야 합니다.'
    isValid = false
  }

  // Validate new password if provided
  if (newPassword.value) {
    if (newPassword.value.length < 8) {
      newPasswordError.value = '새 비밀번호는 최소 8자 이상이어야 합니다.'
      isValid = false
    }

    if (newPassword.value !== confirmPassword.value) {
      confirmPasswordError.value = '비밀번호가 일치하지 않습니다.'
      isValid = false
    }
  }

  return isValid
}

const handleSave = async () => {
  if (!validateForm()) return

  isLoading.value = true

  try {
    // Create FormData for file upload
    const formData = new FormData()
    formData.append('name', userInfo.value.name.trim())
    formData.append('currentPassword', currentPassword.value)

    if (newPassword.value) {
      formData.append('newPassword', newPassword.value)
    }

    if (profileImageFile.value) {
      formData.append('profileImage', profileImageFile.value)
    }

    const payload = {
      name: userInfo.value.name.trim(),
      profileImage: profileImageFile.value,
      mainBadgeId: null,
    }

    if (newPassword.value?.trim()) {
      payload.password = newPassword.value.trim()
    }

    await api.put('/members/me', payload)

    alert('프로필이 성공적으로 수정되었습니다.')
    router.push('/profile')
  } catch (error) {
    console.error('Profile update failed:', error)

    if (error.response?.status === 400) {
      alert('현재 비밀번호가 올바르지 않습니다.')
    } else if (error.response?.status === 413) {
      alert('파일 크기가 너무 큽니다.')
    } else {
      alert('프로필 수정에 실패했습니다. 다시 시도해주세요.')
    }
  } finally {
    isLoading.value = false
  }
}

const goBack = () => {
  router.push('/profile')
}

// Fetch current user data
const fetchUserData = async () => {
  try {
    const response = await api.get('/members/me')
    userInfo.value = {
      name: response.data.result.name,
      profileImage: response.data.result.profileImage,
    }
  } catch (error) {
    console.error('Failed to fetch user data:', error)
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

/* File input styling */
input[type='file'] {
  display: none;
}
</style>
