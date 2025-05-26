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

    <!-- Login Form Container -->
    <div class="bg-white p-8 rounded-xl shadow-md w-full max-w-md border border-sage-green">
      <h2 class="text-2xl font-semibold text-deep-sage mb-6 text-center">로그인</h2>

      <!-- Email Input -->
      <div class="mb-5">
        <label for="email" class="block text-gray-700 mb-2 font-medium">이메일</label>
        <div class="relative">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <i class="fas fa-envelope text-sage-green"></i>
          </div>
          <input
            type="email"
            id="email"
            v-model="email"
            class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sage-green focus:border-transparent"
            placeholder="Enter your email"
            :class="{ 'border-red-300 focus:ring-red-300': emailError }"
          />
        </div>
        <p v-if="emailError" class="mt-1 text-sm text-red-500">{{ emailError }}</p>
      </div>

      <!-- Password Input -->
      <div class="mb-5">
        <label for="password" class="block text-gray-700 mb-2 font-medium">비밀번호</label>
        <div class="relative">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <i class="fas fa-lock text-sage-green"></i>
          </div>
          <input
            :type="showPassword ? 'text' : 'password'"
            id="password"
            v-model="password"
            class="w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sage-green focus:border-transparent"
            placeholder="Enter your password"
            :class="{ 'border-red-300 focus:ring-red-300': passwordError }"
          />
        </div>
        <p v-if="passwordError" class="mt-1 text-sm text-red-500">{{ passwordError }}</p>
      </div>

      <div class="flex items-center mb-6">
        <label class="inline-flex items-center cursor-pointer">
          <input
            type="checkbox"
            v-model="rememberMe"
            class="form-checkbox h-5 w-5 text-sage-green rounded border-gray-300 transition duration-300"
          />
          <span class="ml-2 text-gray-700">Remember me</span>
        </label>
      </div>

      <!-- Login Button -->
      <button
        @click="handleLogin"
        class="w-full bg-sage-green py-3 rounded-lg font-medium transition duration-300 border border-deep-sage hover:bg-deep-sage hover:border-green-900 flex justify-center items-center cursor-pointer whitespace-nowrap"
        :disabled="isLoading"
      >
        <span v-if="!isLoading">로그인</span>
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
          <span>Signing in...</span>
        </div>
      </button>

      <!-- Sign Up Button -->
      <button
        @click="goToSignUp"
        class="w-full bg-light-brown py-3 rounded-lg font-medium mt-4 transition duration-300 border border-brown hover:bg-brown hover:border-[#a8937f] cursor-pointer whitespace-nowrap"
      >
        회원가입
      </button>
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
import { ref } from 'vue'
import api from '@/lib/api'
import { useAuthStore } from '@/stores/useAuthStore'
import { useRouter } from 'vue-router'

// Form state
const email = ref('')
const password = ref('')
const rememberMe = ref(false)
const showPassword = ref(false)
const isLoading = ref(false)

// Error states
const emailError = ref('')
const passwordError = ref('')

const authStore = useAuthStore()
const router = useRouter()

// Validate email
const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

// Handle login
const handleLogin = () => {
  // Reset errors
  emailError.value = ''
  passwordError.value = ''

  // Validate form
  let isValid = true

  if (!email.value) {
    emailError.value = '이메일을 입력해주세요.'
    isValid = false
  } else if (!validateEmail(email.value)) {
    emailError.value = '유효하지 않은 이메일 형식입니다.'
    isValid = false
  }

  if (!password.value) {
    passwordError.value = '비밀번호를 입력해주세요.'
    isValid = false
  }

  if (isValid) {
    // Show loading state
    isLoading.value = true
    // Simulate API call
    setTimeout(async () => {
      isLoading.value = false

      // If remember me is checked, save email to localStorage
      if (rememberMe.value) {
        localStorage.setItem('rememberedEmail', email.value)
      } else {
        localStorage.removeItem('rememberedEmail')
      }

      try {
        const response = await api.post('/auth/login', {
          email: email.value,
          password: password.value,
        })
        const token = response.headers['authorization']
        authStore.setToken(token)
        router.push('/')
      } catch (error) {
        console.log('Login failed: ', error)
        alert('이메일와 비밀번호를 확인해주세요.')
      } finally {
        isLoading.value = false
      }
    }, 2000)
  }
}

// Navigate to sign up page
const goToSignUp = () => {
  router.push('/register')
}

// Check for remembered email on component mount
const checkRememberedEmail = () => {
  const rememberedEmail = localStorage.getItem('rememberedEmail')
  if (rememberedEmail) {
    email.value = rememberedEmail
    rememberMe.value = true
  }
}

// Call the function to check for remembered email
checkRememberedEmail()
</script>

<style scoped>
/* Custom checkbox styling */
input[type='checkbox']:checked + div svg {
  display: block;
}
</style>
