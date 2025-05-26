<template>
  <div class="min-h-screen bg-soft-white">
    <!-- Hero Section -->
    <div class="bg-gradient-to-br from-sage-green to-sky-200 min-h-[600px] py-20">
      <div class="container mx-auto px-6">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-16 items-center">
          <!-- 텍스트 영역 -->
          <div>
            <h1 class="text-4xl md:text-5xl font-bold text-white mb-6 leading-snug">
              TripCloud와 함께<br />
              소중한 사진을 이 곳에서
            </h1>
            <p class="text-lg text-white/90 mb-8 leading-relaxed">
              <br />
              추억을 안전하게 보관하고 언제든 꺼내보세요.<br />
              나만의 지도를 만들고 사람들과 공유해보세요.
            </p>
          </div>

          <!-- 이미지 목업 -->
          <!-- 이미지 목업 -->
          <div
            class="relative bg-[#f6f1e7] rounded-3xl shadow-[0_20px_40px_rgba(0,0,0,0.08)] overflow-hidden hover:shadow-[0_25px_50px_rgba(0,0,0,0.1)] transition duration-300"
          >
            <img
              src="@/assets/image/mockup.png"
              alt="TripCloud Interface"
              class="w-full h-auto object-cover"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Features Section -->
    <section id="features" class="py-20 bg-gray-50">
      <div class="container mx-auto px-6">
        <div class="text-center mb-16">
          <h2 class="text-4xl font-bold text-gray-800 mb-4">여행의 추억을 안전하게 보관하세요.</h2>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
          <!-- Feature 1 -->
          <div class="flex flex-col items-center">
            <div class="mb-6">
              <img
                src="@/assets/image/cloud.png"
                alt="자동 백업 및 동기화"
                class="w-32 h-32 object-contain"
              />
            </div>
            <h3 class="text-xl font-medium text-gray-800 mb-2">언제 어디서나 접근</h3>
            <p class="text-gray-600">
              스마트폰, 태블릿, 컴퓨터에서 언제 어디서든 접근하고 사용하세요.
            </p>
          </div>

          <!-- Feature 2 -->
          <div class="flex flex-col items-center">
            <div class="mb-6">
              <img
                src="@/assets/image/AI.png"
                alt="온라인 어디서나 접근"
                class="w-32 h-32 object-contain"
              />
            </div>
            <h3 class="text-xl font-medium text-gray-800 mb-2">해시태그</h3>
            <p class="text-gray-600">AI를 이용해 자동으로 해시태그를 부착합니다.</p>
          </div>

          <!-- Feature 3 -->
          <div class="flex flex-col items-center">
            <div class="mb-6">
              <img
                src="@/assets/image/typing.png"
                alt="어떤 파일이든 보관"
                class="w-32 h-32 object-contain"
              />
            </div>
            <h3 class="text-xl font-medium text-gray-800 mb-2">추억을 공유해보세요.</h3>
            <p class="text-gray-600">게시판을 통해 여행의 추억을 공유해보세요.</p>
          </div>
        </div>

        <div class="mt-20 text-center">
          <h2 class="text-4xl font-bold text-gray-800 mb-4">나만의 지도를 만들어 보세요.</h2>
          <div class="max-w-4xl mx-auto relative">
            <img
              src="@/assets/image/지도.gif"
              alt="TripCloud 모바일 앱 스크린샷"
              class="w-full h-auto rounded-lg shadow-xl"
            />
          </div>
          <h3 class="text-2xl font-medium text-gray-800 mt-8 mb-2"></h3>
          <p class="text-gray-600 max-w-2xl mx-auto">추억이 담긴 지도를 만들어 드려요.</p>
        </div>
      </div>
    </section>

    <!-- Footer -->
    <footer class="bg-gray-800 text-white py-12">
      <div class="container mx-auto px-6">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-12">
          <div>
            <h3 class="text-xl font-bold mb-4">TripCloud</h3>
            <p class="text-gray-400 mb-6">Your travel memories, securely stored in the cloud.</p>
            <div class="flex space-x-4">
              <a
                href="#"
                class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
              >
                <i class="fab fa-facebook-f"></i>
              </a>
              <a
                href="#"
                class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
              >
                <i class="fab fa-twitter"></i>
              </a>
              <a
                href="#"
                class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
              >
                <i class="fab fa-instagram"></i>
              </a>
              <a
                href="#"
                class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
              >
                <i class="fab fa-linkedin-in"></i>
              </a>
            </div>
          </div>
          <div>
            <h3 class="text-lg font-semibold mb-4">Company</h3>
            <ul class="space-y-2">
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >About Us</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Careers</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Press</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Blog</a
                >
              </li>
              <li>
                <a
                  href="#contact"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Contact</a
                >
              </li>
            </ul>
          </div>
          <div>
            <h3 class="text-lg font-semibold mb-4">Resources</h3>
            <ul class="space-y-2">
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Help Center</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Travel Guides</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Photography Tips</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >API Documentation</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Status</a
                >
              </li>
            </ul>
          </div>
          <div>
            <h3 class="text-lg font-semibold mb-4">Legal</h3>
            <ul class="space-y-2">
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Terms of Service</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Privacy Policy</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Cookie Policy</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >GDPR Compliance</a
                >
              </li>
              <li>
                <a
                  href="#"
                  class="text-gray-400 hover:text-white transition-colors duration-300 cursor-pointer"
                  >Security</a
                >
              </li>
            </ul>
          </div>
        </div>
        <div
          class="border-t border-gray-700 mt-12 pt-8 flex flex-col md:flex-row justify-between items-center"
        >
          <p class="text-gray-400 mb-4 md:mb-0">© 2025 TripCloud. All rights reserved.</p>
          <div class="flex space-x-6">
            <i class="fab fa-cc-visa text-2xl text-gray-400"></i>
            <i class="fab fa-cc-mastercard text-2xl text-gray-400"></i>
            <i class="fab fa-cc-amex text-2xl text-gray-400"></i>
            <i class="fab fa-cc-paypal text-2xl text-gray-400"></i>
          </div>
        </div>
      </div>
    </footer>
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
