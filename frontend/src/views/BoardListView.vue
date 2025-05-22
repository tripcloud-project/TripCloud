<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->

<template>
  <div class="min-h-screen flex flex-col bg-soft-white">
    <!-- Header -->
    <header class="bg-white shadow-sm py-4 border-b border-sage-green/20">
      <div class="container mx-auto px-4 flex items-center justify-between">
        <div class="flex items-center">
          <div class="w-10 h-10 flex items-center justify-center bg-sage-green rounded-full mr-3">
            <i class="fas fa-leaf text-white text-lg"></i>
          </div>
          <h1 class="text-2xl font-bold text-deep-sage">TripCloud</h1>
        </div>
        <div class="flex items-center space-x-4">
          <button
            class="text-sage-green hover:text-deep-sage transition-colors cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-bell mr-1"></i>
            <span class="hidden md:inline">Notifications</span>
          </button>
          <button
            class="text-sage-green hover:text-deep-sage transition-colors cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-user-circle mr-1"></i>
            <span class="hidden md:inline">Profile</span>
          </button>
          <button
            class="bg-sage-green hover:bg-deep-sage text-white px-4 py-2 rounded-lg transition-colors cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-plus mr-1"></i>
            <span>New Post</span>
          </button>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-grow container mx-auto px-4 py-8">
      <!-- Page Title and Filters -->
      <div class="mb-8 flex flex-col md:flex-row md:items-center md:justify-between">
        <div>
          <h2 class="text-3xl font-bold text-deep-sage mb-2">Community Bulletin Board</h2>
          <p class="text-gray-600">Discover and share nature experiences with our community</p>
        </div>

        <div class="mt-4 md:mt-0 flex flex-col sm:flex-row gap-3">
          <div class="relative">
            <input
              type="text"
              placeholder="Search posts..."
              class="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-sage-green focus:border-transparent w-full sm:w-64"
              v-model="searchQuery"
            />
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <i class="fas fa-search text-gray-400"></i>
            </div>
          </div>

          <div class="relative">
            <div
              @click="toggleFilterDropdown"
              class="flex items-center justify-between px-4 py-2 border border-gray-300 rounded-lg bg-white cursor-pointer !rounded-button whitespace-nowrap"
            >
              <span>{{ selectedFilter }}</span>
              <i class="fas fa-chevron-down ml-2 text-gray-500"></i>
            </div>

            <div
              v-if="showFilterDropdown"
              class="absolute mt-1 w-full bg-white border border-gray-300 rounded-lg shadow-lg z-10"
            >
              <div
                v-for="filter in filters"
                :key="filter"
                @click="selectFilter(filter)"
                class="px-4 py-2 hover:bg-gray-100 cursor-pointer first:rounded-t-lg last:rounded-b-lg"
              >
                {{ filter }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Posts List -->
      <div class="bg-white rounded-xl shadow-md border border-sage-green/10 overflow-hidden">
        <!-- Posts Header -->
        <div
          class="bg-sage-green/10 px-6 py-3 border-b border-sage-green/20 grid grid-cols-12 gap-4"
        >
          <div class="col-span-1 font-medium text-deep-sage">ID</div>
          <div class="col-span-5 font-medium text-deep-sage">Title</div>
          <div class="col-span-2 font-medium text-deep-sage">Author</div>
          <div class="col-span-2 font-medium text-deep-sage">Comments</div>
          <div class="col-span-2 font-medium text-deep-sage">Posted</div>
        </div>

        <!-- Posts -->
        <div v-if="filteredPosts.length > 0">
          <div
            v-for="post in paginatedPosts"
            :key="post.id"
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
          Showing {{ startIndex + 1 }}-{{ Math.min(endIndex, filteredPosts.length) }} of
          {{ filteredPosts.length }} posts
        </div>

        <div class="flex items-center space-x-1">
          <button
            @click="goToPage(currentPage - 1)"
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
            <span v-else class="px-4 py-2 text-gray-500"> ... </span>
          </template>

          <button
            @click="goToPage(currentPage + 1)"
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
              <span class="text-lg font-bold text-deep-sage">NatureBoard</span>
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

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/lib/api'

// Filter dropdown state
const showFilterDropdown = ref(false)
const selectedFilter = ref('Latest Posts')
const filters = ['Latest Posts', 'Most Comments', 'Oldest Posts', 'Alphabetical']

const toggleFilterDropdown = () => {
  showFilterDropdown.value = !showFilterDropdown.value
}

const selectFilter = (filter) => {
  selectedFilter.value = filter
  showFilterDropdown.value = false

  // Reset to first page when changing filters
  currentPage.value = 1
}

// Close dropdown when clicking outside
onMounted(() => {
  document.addEventListener('click', (event) => {
    const target = event.target as HTMLElement
    if (!target.closest('.dropdown')) {
      showFilterDropdown.value = false
    }
  })
})

// Search functionality
const searchQuery = ref('')

// Pagination
const currentPage = ref(1)
const postsPerPage = 20

// Mock data for posts
const getPostList = async () => {
  const posts = []
  const authors = []
  const titles = []

  const response = await api.get('/posts', {
    params: {
      page: 1,
      size: 10,
    },
  })

  const content = response.data.result.content
  const hasNext = response.data.result.hasNext
  const nextPage = response.data.result.size

  console.log(content)

  // Current date for reference
  const now = new Date()

  for (let i = 1; i <= 100; i++) {
    // Random date within the last 30 days
    const randomDaysAgo = Math.floor(Math.random() * 30)
    const randomHoursAgo = Math.floor(Math.random() * 24)
    const randomMinutesAgo = Math.floor(Math.random() * 60)

    const postDate = new Date(now)
    postDate.setDate(postDate.getDate() - randomDaysAgo)
    postDate.setHours(postDate.getHours() - randomHoursAgo)
    postDate.setMinutes(postDate.getMinutes() - randomMinutesAgo)

    // Format the time display
    let timeDisplay
    if (randomDaysAgo > 0) {
      timeDisplay = `${randomDaysAgo}d ago`
    } else if (randomHoursAgo > 0) {
      timeDisplay = `${randomHoursAgo}h ago`
    } else {
      timeDisplay = `${randomMinutesAgo}m ago`
    }

    posts.push({
      id: i,
      title: titles[Math.floor(Math.random() * titles.length)],
      author: authors[Math.floor(Math.random() * authors.length)],
      comments: Math.floor(Math.random() * 50),
      time: timeDisplay,
      date: postDate, // Store actual date for sorting
    })
  }

  return posts
}

const allPosts = ref(getPostList())

// Filter and sort posts based on search query and selected filter
const filteredPosts = computed(() => {
  return allPosts
  // let result = [...allPosts.value]

  // // Apply search filter
  // if (searchQuery.value) {
  //   const query = searchQuery.value.toLowerCase()
  //   result = result.filter(
  //     (post) =>
  //       post.title.toLowerCase().includes(query) || post.author.toLowerCase().includes(query),
  //   )
  // }

  // // Apply sorting based on selected filter
  // switch (selectedFilter.value) {
  //   case 'Latest Posts':
  //     result.sort((a, b) => b.date.getTime() - a.date.getTime())
  //     break
  //   case 'Oldest Posts':
  //     result.sort((a, b) => a.date.getTime() - b.date.getTime())
  //     break
  //   case 'Most Comments':
  //     result.sort((a, b) => b.comments - a.comments)
  //     break
  //   case 'Alphabetical':
  //     result.sort((a, b) => a.title.localeCompare(b.title))
  //     break
  // }

  // return result
})

// Calculate total pages
const totalPages = computed(() => {
  return Math.ceil(filteredPosts.value.length / postsPerPage)
})

// Get current page posts
const startIndex = computed(() => (currentPage.value - 1) * postsPerPage)
const endIndex = computed(() => startIndex.value + postsPerPage)
const paginatedPosts = computed(() => {
  return filteredPosts.value.slice(startIndex.value, endIndex.value)
})

// Page navigation
const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

// Generate visible page numbers for pagination
const visiblePageNumbers = computed(() => {
  const totalPagesCount = totalPages.value
  const current = currentPage.value
  const delta = 1 // Number of pages to show before and after current page

  if (totalPagesCount <= 5) {
    // If we have 5 or fewer pages, show all
    return Array.from({ length: totalPagesCount }, (_, i) => i + 1)
  }

  let range = []

  // Always include first page
  range.push(1)

  // Calculate start and end of range around current page
  const rangeStart = Math.max(2, current - delta)
  const rangeEnd = Math.min(totalPagesCount - 1, current + delta)

  // Add ellipsis if there's a gap after first page
  if (rangeStart > 2) {
    range.push('...')
  }

  // Add pages around current page
  for (let i = rangeStart; i <= rangeEnd; i++) {
    range.push(i)
  }

  // Add ellipsis if there's a gap before last page
  if (rangeEnd < totalPagesCount - 1) {
    range.push('...')
  }

  // Always include last page
  if (totalPagesCount > 1) {
    range.push(totalPagesCount)
  }

  return range
})
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
