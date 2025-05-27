<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->

<template>
  <div class="flex flex-col items-center justify-center bg-soft-white">
    <!-- Header Section -->
    <div class="flex flex-col items-center mb-8">
      <div class="w-16 h-16 mb-4 flex items-center justify-center bg-sage-green rounded-full">
        <i class="fas fa-cloud text-white text-2xl"></i>
      </div>
      <h1 class="text-3xl font-bold text-deep-sage mb-2">TripCloud</h1>
      <p class="text-sage-green text-lg">Secure Cloud Photo Storage</p>
    </div>

    <!-- Sign Up Form Container -->
    <div class="bg-white p-8 rounded-xl shadow-md w-full max-w-md border border-sage-green">
      <h2 class="text-2xl font-semibold text-deep-sage mb-6 text-center">회원가입</h2>

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
            v-model="name"
            class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sage-green focus:border-transparent"
            placeholder="이름을 입력해 주세요"
            :class="{ 'border-red-300 focus:ring-red-300': nameError }"
          />
        </div>
        <p v-if="nameError" class="mt-1 text-sm text-red-500">{{ nameError }}</p>
      </div>

      <!-- Email Input -->
      <div class="mb-5">
        <label for="email" class="block text-gray-700 mb-2 font-medium">이메일</label>
        <div class="relative flex">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <i class="fas fa-envelope text-sage-green"></i>
          </div>
          <input
            type="email"
            id="email"
            v-model="email"
            class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sage-green focus:border-transparent"
            placeholder="이메일을 입력해 주세요"
            :class="{
              'border-red-300 focus:ring-red-300': emailError,
            }"
            :disabled="emailVerified"
          />
          <button
            @click="verifyEmail"
            class="ml-2 px-4 bg-sage-green transition duration-300 hover:bg-deep-sage rounded-lg font-medium flex items-center whitespace-nowrap"
            :class="{
              '': !emailVerified,
              'border border-brown': emailVerified,
            }"
            :disabled="emailVerified"
          >
            <span>중복 확인</span>
          </button>
        </div>
        <p v-if="emailError" class="mt-1 text-sm text-red-500">{{ emailError }}</p>
        <p v-if="emailVerified" class="mt-1 text-sm text-green-600">사용 가능한 이메일입니다.</p>
      </div>

      <!-- Password Input -->
      <div class="mb-5">
        <label for="password" class="block text-gray-700 mb-2 font-medium"
          >비밀번호 (8자리 이상)</label
        >
        <div class="relative">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <i class="fas fa-lock text-sage-green"></i>
          </div>
          <input
            :type="showPassword ? 'text' : 'password'"
            id="password"
            v-model="password"
            class="w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sage-green focus:border-transparent"
            placeholder="비밀번호를 입력해 주세요"
            :class="{ 'border-red-300 focus:ring-red-300': passwordError }"
          />
        </div>
      </div>

      <!-- Confirm Password Input -->
      <div class="mb-5">
        <label for="confirmPassword" class="block text-gray-700 mb-2 font-medium"
          >비밀번호 확인</label
        >
        <div class="relative">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <i class="fas fa-lock text-sage-green"></i>
          </div>
          <input
            :type="showConfirmPassword ? 'text' : 'password'"
            id="confirmPassword"
            v-model="confirmPassword"
            class="w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sage-green focus:border-transparent"
            placeholder="비밀번호를 입력해 주세요"
          />
        </div>
        <p v-if="confirmPasswordError" class="mt-1 text-sm text-red-500">
          {{ confirmPasswordError }}
        </p>
      </div>

      <p v-if="termsError" class="mt-1 mb-4 text-sm text-red-500">{{ termsError }}</p>

      <!-- Sign Up Button -->
      <button
        @click="handleSignUp"
        class="w-full bg-sage-green py-3 rounded-lg font-medium transition duration-300 hover:bg-deep-sage flex justify-center items-center cursor-pointer !rounded-button whitespace-nowrap"
        :disabled="isLoading || !emailVerified"
      >
        <span v-if="!isLoading">회원가입</span>
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
          <span>가입 중...</span>
        </div>
      </button>

      <!-- Sign In Link -->
      <div class="text-center mt-6">
        <a
          @click="goToSignIn"
          class="block w-full bg-light-brown py-3 rounded-lg font-medium mt-4 transition duration-300 border border-brown hover:bg-brown hover:border-[#a8937f] cursor-pointer whitespace-nowrap"
        >
          로그인하러가기
        </a>
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

<script lang="ts" setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/lib/api'

// Form state
const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const agreeToTerms = ref(false)
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const isLoading = ref(false)
const emailVerified = ref(false)

// Error states
const nameError = ref('')
const emailError = ref('')
const passwordError = ref('')
const confirmPasswordError = ref('')
const termsError = ref('')

const router = useRouter()

// Validate email
const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

// Verify email
const verifyEmail = async () => {
  // Reset error
  emailError.value = ''
  emailVerified.value = false

  // Validate email format
  if (!email.value) {
    emailError.value = '이메일을 입력해주세요.'
    return
  }

  if (!validateEmail(email.value)) {
    emailError.value = '유효하지 않은 이메일입니다.'
    return
  }

  // Show verifying state
  const response = await api.get('/members/checkEmail', {
    params: {
      email: email.value,
    },
  })

  if (response.data.result.isDuplicated) {
    emailError.value = '사용할 수 없는 이메일입니다.'
    return
  }
  emailVerified.value = true
}

// Handle sign up
const handleSignUp = async () => {
  // Reset errors
  nameError.value = ''
  emailError.value = ''
  passwordError.value = ''
  confirmPasswordError.value = ''
  termsError.value = ''

  // Validate form
  let isValid = true

  if (!name.value) {
    nameError.value = '이름을 입력해주세요.'
    isValid = false
  }

  if (!email.value) {
    emailError.value = '이메일을 입력해주세요.'
    isValid = false
  } else if (!validateEmail(email.value)) {
    emailError.value = '유효하지 않은 이메일입니다.'
    isValid = false
  }

  if (!emailVerified.value) {
    emailError.value = '이메일 중복 검사를 해주세요.'
    isValid = false
  }

  if (!password.value) {
    passwordError.value = '비밀번호를 입력해주세요.'
    isValid = false
  } else if (password.value.length < 8) {
    passwordError.value = '비밀번호는 8글자 이상이어야 합니다.'
    isValid = false
  }

  if (password.value !== confirmPassword.value) {
    confirmPasswordError.value = '비밀번호가 일치하지 않습니다.'
    isValid = false
  }

  if (isValid) {
    // Show loading state
    isLoading.value = true

    const response = await api.post('/members', {
      email: email.value,
      password: password.value,
      name: name.value,
    })

    if (response.data.status === 'success') {
      router.push('/login')
    } else {
      alert(response.data.message || '회원가입에 실패했습니다. 다시 시도해주세요.')
    }
  }
}

// Navigate to sign in page
const goToSignIn = () => {
  router.push('/login')
}
</script>

<style scoped>
/* Custom checkbox styling */
input[type='checkbox']:checked + div svg {
  display: block;
}
</style>
