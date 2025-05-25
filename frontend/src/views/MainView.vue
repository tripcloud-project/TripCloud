<template>
  <div class="min-h-screen bg-soft-white">
    <!-- Hero Section -->
    <section class="bg-gradient-to-br from-sage-green to-deep-sage text-white py-20">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <div class="mb-8">
          <div
            class="w-24 h-24 bg-white bg-opacity-20 rounded-full flex items-center justify-center mx-auto mb-6"
          >
            <i class="fas fa-cloud text-white text-4xl"></i>
          </div>
          <h1 class="text-5xl font-bold mb-4">TripCloud</h1>
          <p class="text-xl mb-8 opacity-90">여행의 모든 순간을 클라우드에 담다</p>
          <p class="text-lg opacity-80 max-w-2xl mx-auto">
            여행 사진부터 추억까지, 모든 여행 기록을 안전하게 보관하고 언제 어디서든 다시
            만나보세요. <br />당신의 여행 지도를 완성해 나가세요.
          </p>
        </div>
      </div>
    </section>

    <!-- Services Section -->
    <section class="py-20">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <h2 class="text-3xl font-bold text-deep-sage mb-4">TripCloud 서비스</h2>
          <p class="text-lg text-gray-600">여행의 새로운 경험을 시작하세요</p>
        </div>

        <div
          class="bg-white rounded-xl shadow-md border border-sage-green flex flex-col lg:flex-row overflow-hidden"
        >
          <!-- 이미지 영역 -->
          <div class="lg:w-1/2 h-64 lg:h-auto">
            <img alt="여행 사진 드라이브" class="w-full h-full object-cover" />
          </div>

          <!-- 내용 영역 -->
          <div class="lg:w-1/2 p-8 flex flex-col justify-center">
            <h3 class="text-2xl font-semibold text-deep-sage mb-4">여행 사진 드라이브</h3>
            <p class="text-gray-600 mb-6 leading-relaxed">
              소중한 여행 사진을 안전한 클라우드에 저장하고, 언제 어디서든 다운로드하여 추억을
              되살려보세요.
            </p>
            <button
              @click="goToPhotoDrive"
              class="w-full bg-sage-green text-white py-3 rounded-lg font-medium hover:bg-deep-sage transition duration-300"
            >
              사진 업로드하기
            </button>
          </div>
        </div>
        <div class="h-12"></div>
        <div
          class="bg-white rounded-xl shadow-md border border-sage-green flex flex-col lg:flex-row-reverse overflow-hidden"
        >
          <!-- 이미지 영역 -->
          <div class="lg:w-1/2 h-64 lg:h-auto">
            <img alt="나만의 여행 지도 만들기" class="w-full h-full object-cover" />
          </div>

          <!-- 내용 영역 -->
          <div class="lg:w-1/2 p-8 flex flex-col justify-center">
            <h3 class="text-2xl font-semibold text-deep-sage mb-4">나만의 여행 지도 만들기</h3>
            <p class="text-gray-600 mb-6 leading-relaxed">
              나만의 여행 지도를 자동으로 만들고, 아직 가지 못한 여행지를 추천받아보세요.
            </p>
            <button
              @click="goToTravleMap"
              class="w-full bg-sage-green text-white py-3 rounded-lg font-medium hover:bg-deep-sage transition duration-300"
            >
              지도 보러가기
            </button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToPhotoDrive = () => {
  router.push('/drive')
}

const goToTravleMap = () => {
  router.push('/drive/map')
}
</script>

<style scoped>
/* Custom gradient */
.bg-gradient-to-br {
  background: linear-gradient(135deg, #6b8e5a 0%, #4a6741 100%);
}

/* Hover animations */
.hover\:-translate-y-2:hover {
  transform: translateY(-0.5rem);
}

/* Smooth transitions */
.transition-all {
  transition: all 0.3s ease-in-out;
}

/* Stats animation */
@keyframes countUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.stats-animate {
  animation: countUp 0.6s ease-out;
}
</style>
