<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->

<template>
  <div class="min-h-screen flex flex-col bg-soft-white">
    <!-- Main Content -->
    <main class="flex-grow container mx-auto px-4 py-8">
      <!-- Page Title and Filters -->
      <div class="mb-8 flex flex-col md:flex-row md:items-center md:justify-between">
        <div>
          <h2 class="text-3xl font-bold text-deep-sage mb-2">자유게시판</h2>
          <p class="text-gray-600">Discover and share nature experiences with our community</p>
        </div>

        <button
          class="bg-sage-green hover:bg-deep-sage text-white px-4 py-2 rounded-lg transition-colors cursor-pointer !rounded-button whitespace-nowrap"
        >
          <i class="fas fa-plus mr-1"></i>
          <span>New Post</span>
        </button>
      </div>

      <!-- Posts List -->
      <div class="bg-white rounded-xl shadow-md border border-sage-green/10 overflow-hidden">
        <!-- Posts Header -->
        <div
          class="bg-sage-green/10 px-6 py-3 border-b border-sage-green/20 grid grid-cols-12 gap-4"
        >
          <div class="col-span-1 font-medium text-deep-sage">번호</div>
          <div class="col-span-5 font-medium text-deep-sage">제목</div>
          <div class="col-span-2 font-medium text-deep-sage">글쓴이</div>
          <div class="col-span-2 font-medium text-deep-sage">댓글</div>
          <div class="col-span-2 font-medium text-deep-sage">날짜</div>
        </div>

        <!-- Posts -->
        <div v-if="allPosts.length > 0">
          <div
            v-for="post in allPosts"
            :key="post.id"
            @click="goToDetail(post.id)"
            class="px-6 py-4 border-b border-gray-200 grid grid-cols-12 gap-4 hover:bg-sage-green/5 transition-colors cursor-pointer"
          >
            <div class="col-span-1 text-gray-500">#{{ post.id }}</div>
            <div class="col-span-5 font-medium text-gray-800 truncate">{{ post.title }}</div>
            <div class="col-span-2 flex items-center">
              <div
                class="w-6 h-6 rounded-full bg-light-brown flex items-center justify-center mr-2"
              >
                <i class="fas fa-user text-xs text-white"></i>
              </div>
              <span class="text-gray-700 truncate">{{ post.author }}</span>
            </div>
            <div class="col-span-2 text-gray-600">
              <i class="fas fa-comment text-sky-blue mr-1"></i>
              {{ post.comments }}
            </div>
            <div class="col-span-2 text-gray-500 text-sm">{{ post.time }}</div>
          </div>
        </div>

        <!-- Empty State -->
        <div v-else class="px-6 py-12 text-center">
          <div
            class="w-16 h-16 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center"
          >
            <i class="fas fa-search text-gray-400 text-xl"></i>
          </div>
          <h3 class="text-lg font-medium text-gray-700 mb-1">No posts found</h3>
          <p class="text-gray-500">Try adjusting your search or filter criteria</p>
        </div>
      </div>

      <!-- Pagination -->
      <div class="mt-8 flex justify-between items-center">
        <div class="text-gray-600">
          Showing {{ startIndex + 1 }}-{{ Math.min(endIndex, totalCount) }} of
          {{ totalCount }} posts
        </div>

        <div class="flex items-center space-x-1">
          <button
            @click="goToPage(1)"
            :disabled="currentPage === 1"
            :class="{ 'opacity-50 cursor-not-allowed': currentPage === 1 }"
            class="px-3 py-2 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-chevron-left"></i>
          </button>

          <template v-for="page in visiblePageNumbers" :key="page">
            <button
              v-if="page !== '...'"
              @click="goToPage(page)"
              :class="{
                'bg-sage-green text-white border-sage-green': currentPage === page,
                'hover:bg-gray-100': currentPage !== page,
              }"
              class="px-4 py-2 rounded-lg border border-gray-300 cursor-pointer !rounded-button whitespace-nowrap"
            >
              {{ page }}
            </button>
          </template>

          <button
            @click="goToPage(totalPages)"
            :disabled="currentPage === totalPages"
            :class="{ 'opacity-50 cursor-not-allowed': currentPage === totalPages }"
            class="px-3 py-2 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </main>

    <!-- Footer -->
    <footer class="bg-white border-t border-sage-green/20 py-6">
      <div class="container mx-auto px-4">
        <div class="flex flex-col md:flex-row justify-between items-center">
          <div class="mb-4 md:mb-0">
            <div class="flex items-center">
              <div class="w-8 h-8 flex items-center justify-center bg-sage-green rounded-full mr-2">
                <i class="fas fa-leaf text-white text-sm"></i>
              </div>
              <span class="text-lg font-bold text-deep-sage">TripCloud</span>
            </div>
            <p class="text-gray-500 text-sm mt-2">Connect with nature enthusiasts worldwide</p>
          </div>

          <div class="flex space-x-6">
            <a
              href="#"
              class="text-gray-500 hover:text-sage-green transition-colors cursor-pointer"
            >
              <i class="fab fa-facebook-f"></i>
            </a>
            <a
              href="#"
              class="text-gray-500 hover:text-sage-green transition-colors cursor-pointer"
            >
              <i class="fab fa-twitter"></i>
            </a>
            <a
              href="#"
              class="text-gray-500 hover:text-sage-green transition-colors cursor-pointer"
            >
              <i class="fab fa-instagram"></i>
            </a>
            <a
              href="#"
              class="text-gray-500 hover:text-sage-green transition-colors cursor-pointer"
            >
              <i class="fab fa-pinterest"></i>
            </a>
          </div>
        </div>

        <div
          class="mt-6 pt-6 border-t border-gray-200 flex flex-col md:flex-row justify-between items-center"
        >
          <div class="text-gray-500 text-sm mb-4 md:mb-0">
            © 2025 TripCloud. All rights reserved.
          </div>

          <div class="flex flex-wrap justify-center gap-4">
            <a
              href="#"
              class="text-gray-500 hover:text-sage-green text-sm transition-colors cursor-pointer"
              >Privacy Policy</a
            >
            <a
              href="#"
              class="text-gray-500 hover:text-sage-green text-sm transition-colors cursor-pointer"
              >Terms of Service</a
            >
            <a
              href="#"
              class="text-gray-500 hover:text-sage-green text-sm transition-colors cursor-pointer"
              >Community Guidelines</a
            >
            <a
              href="#"
              class="text-gray-500 hover:text-sage-green text-sm transition-colors cursor-pointer"
              >Contact Us</a
            >
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/lib/api'

// Pagination
const currentPage = ref(1)
const totalCount = ref(0)
const postsPerPage = 10

const router = useRouter()

const getPostList = async () => {
  const posts = []

  const response = await api.get('/posts', {
    params: {
      page: currentPage.value,
      size: postsPerPage,
    },
  })

  const content = response.data.result.content
  totalCount.value = response.data.result.totalCount

  for (let i = 0; i < content.length; i++) {
    posts.push({
      id: content[i].postId,
      title: content[i].title,
      author: content[i].author,
      comments: content[i].commentCount,
      time: content[i].createdAt,
    })
  }

  return posts
}

const allPosts = ref([])

watch(
  currentPage,
  async () => {
    allPosts.value = await getPostList()
  },
  { immediate: true },
)

// Calculate total pages
const totalPages = computed(() => {
  return Math.ceil(totalCount.value / postsPerPage)
})

// Get current page posts
const startIndex = computed(() => (currentPage.value - 1) * postsPerPage)
const endIndex = computed(() => startIndex.value + postsPerPage)
watch([startIndex, endIndex], ([newStart, newEnd]) => {
  console.log('startIndex:', newStart, 'endIndex:', newEnd)
})

// Page navigation
const goToPage = (page) => {
  if (page >= 1 && page <= totalCount.value) {
    currentPage.value = page
  }
}

// Generate visible page numbers for pagination
const visiblePageNumbers = computed(() => {
  const totalPagesCount = totalPages.value
  const current = currentPage.value
  const delta = 2 // Number of pages to show before and after current page

  let range = []
  if (totalPagesCount <= 5) {
    for (let i = 1; i <= totalPagesCount; i++) {
      range.push(i)
    }
    return range
  }

  // Calculate start and end of range around current page
  let start = current - delta
  let end = current + delta

  if (start < 1) {
    start = 1
    end = 5
  }

  if (end > totalPagesCount) {
    end = totalPagesCount
    start = totalPagesCount - 4
  }

  for (let i = start; i <= end; i++) {
    range.push(i)
  }

  return range
})

const goToDetail = (id) => {
  router.push(`/posts/${id}`)
}
</script>

<style scoped>
.focus\:ring-sage-green:focus {
  --tw-ring-color: var(--sage-green);
}

/* Remove number input arrows */
input[type='number']::-webkit-inner-spin-button,
input[type='number']::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type='number'] {
  -moz-appearance: textfield;
}
</style>
