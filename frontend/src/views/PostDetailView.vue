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
          <h1 class="text-2xl font-bold text-deep-sage">NatureBoard</h1>
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
      <!-- Back Button -->
      <button
        @click="goBack"
        class="flex items-center text-sage-green hover:text-deep-sage mb-6 cursor-pointer !rounded-button whitespace-nowrap"
      >
        <i class="fas fa-arrow-left mr-2"></i>
        <span>목록으로 돌아가기</span>
      </button>
      <!-- Post Detail Card -->
      <div class="bg-white rounded-xl shadow-md border border-sage-green/10 overflow-hidden mb-8">
        <!-- Post Header -->
        <div class="p-6 border-b border-gray-200">
          <div class="flex justify-between items-start">
            <h1 class="text-2xl font-bold text-deep-sage mb-4">
              {{ post.title }}
            </h1>
            <button
              v-if="post.isMyPost"
              @click="deletePost"
              class="text-red-500 hover:text-red-700 cursor-pointer !rounded-button whitespace-nowrap"
            >
              <i class="fas fa-trash"></i>
            </button>
          </div>

          <div class="flex items-center mb-4">
            <div
              class="w-10 h-10 rounded-full bg-light-brown flex items-center justify-center mr-3"
            >
              <i class="fas fa-user text-white"></i>
            </div>
            <div>
              <div class="font-medium text-gray-800">{{ post.author }}</div>
              <div class="text-sm text-gray-500">
                {{ formatDate(post.createdAt) }}
              </div>
            </div>
          </div>

          <!-- Post Content -->
          <div class="prose max-w-none text-gray-700 mb-6">
            <p>{{ post.content }}</p>
          </div>

          <!-- Post Image (if available) -->
          <div v-if="post.imageUrl" class="mb-6 rounded-lg overflow-hidden">
            <img
              :src="post.imageUrl"
              :alt="post.title"
              class="w-full h-auto object-cover object-top"
            />
          </div>

          <!-- Post Actions -->
          <div class="flex items-center justify-between pt-4 border-t border-gray-100">
            <div class="flex items-center space-x-6">
              <button
                @click="toggleLike"
                class="flex items-center text-gray-600 hover:text-deep-sage cursor-pointer !rounded-button whitespace-nowrap"
              >
                <i
                  :class="[
                    post.isLiked ? 'fas text-red-500' : 'far text-gray-500',
                    'fa-heart text-xl mr-2',
                  ]"
                ></i>
                <span>{{ post.likeCount }} </span>
              </button>
            </div>
          </div>
        </div>

        <!-- Comments Section -->
        <div class="bg-gray-50 p-6">
          <h3 class="text-lg font-semibold text-deep-sage mb-4">
            Comments ({{ post.comments.length }})
          </h3>

          <!-- Comment List -->
          <div v-if="post.comments.length > 0" class="space-y-4 mb-6">
            <div
              v-for="comment in post.comments"
              :key="comment.commentId"
              class="bg-white p-4 rounded-lg border border-gray-200"
            >
              <div class="flex justify-between">
                <div class="flex items-center mb-2">
                  <div
                    class="w-8 h-8 rounded-full bg-light-brown flex items-center justify-center mr-2"
                  >
                    <i class="fas fa-user text-white text-xs"></i>
                  </div>
                  <div>
                    <div class="font-medium text-gray-800">
                      {{ comment.author }}
                    </div>
                    <div class="text-xs text-gray-500">
                      {{ formatDate(comment.createdAt) }}
                    </div>
                  </div>
                </div>
                <button
                  v-if="comment.isMyComment"
                  @click="deleteComment(comment.commentId)"
                  class="text-red-500 hover:text-red-700 cursor-pointer !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-times"></i>
                </button>
              </div>
              <p class="text-gray-700 mt-2">{{ comment.content }}</p>
            </div>
          </div>

          <!-- Add Comment Form -->
          <div class="bg-white p-4 rounded-lg border border-gray-200">
            <h4 class="text-md font-medium text-deep-sage mb-3">Add a comment</h4>
            <div class="flex items-start space-x-3">
              <div
                class="w-8 h-8 rounded-full bg-sage-green flex-shrink-0 flex items-center justify-center"
              >
                <i class="fas fa-user text-white text-xs"></i>
              </div>
              <div class="flex-grow">
                <textarea
                  v-model="newComment"
                  placeholder="Write your comment here..."
                  class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-sage-green focus:border-transparent resize-none"
                  rows="3"
                ></textarea>
                <div class="flex justify-end mt-2">
                  <button
                    @click="addComment"
                    :disabled="!newComment.trim()"
                    :class="{ 'opacity-50 cursor-not-allowed': !newComment.trim() }"
                    class="bg-sage-green hover:bg-deep-sage text-white px-4 py-2 rounded-lg transition-colors cursor-pointer !rounded-button whitespace-nowrap"
                  >
                    <i class="fas fa-paper-plane mr-1"></i>
                    <span>Post Comment</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
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

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/lib/api'

const route = useRoute()
const router = useRouter()
const postId = route.params.id

const post = ref({
  comments: [],
})

onMounted(async () => {
  post.value = await getPost()
})

const getPost = async () => {
  const response = await api.get(`/posts/${postId}`, {})
  const result = response.data.result
  return {
    author: result.author,
    title: result.title,
    content: result.content,
    createdAt: result.createdAt,
    likeCount: result.likeCount,
    isLiked: result.liked,
    comments: [...result.comments],
    isMyPost: result.myPost,
  }
}

// New comment input
const newComment = ref('')

// Format date to readable format
const formatDate = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diffTime = Math.abs(now.getTime() - date.getTime())
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))

  if (diffDays === 0) {
    // Today - show hours
    const hours = date.getHours()
    const minutes = date.getMinutes()
    return `Today at ${hours}:${minutes < 10 ? '0' + minutes : minutes}`
  } else if (diffDays === 1) {
    return 'Yesterday'
  } else if (diffDays < 7) {
    return `${diffDays} days ago`
  } else {
    // Format as date
    const options = {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    }
    return date.toLocaleDateString('en-US', options)
  }
}

// Toggle like status
const toggleLike = () => {
  api.post(`/posts/${postId}/like`, {})
  if (post.value.isLiked) {
    post.value.likeCount--
  } else {
    post.value.likeCount++
  }
  post.value.isLiked = !post.value.isLiked
}

// Add new comment
const addComment = () => {
  if (newComment.value.trim()) {
    api.post(`/posts/${postId}/comments`, {
      content: newComment.value.trim(),
    })
    window.location.reload()
  }
}

// Delete comment
const deleteComment = (commentId) => {
  if (confirm('댓글을 삭제하시겠습니까?')) {
    api.delete(`/posts/${postId}/comments/${commentId}`)
    window.location.reload()
  }
}

// Delete post
const deletePost = () => {
  if (confirm('게시글을 삭제하시겠습니까?')) {
    api.delete(`/posts/${postId}`)
    // In a real app, this would call an API to delete the post
    alert('삭제 완료!')
    goBack()
  }
}

// Go back to posts list
const goBack = () => {
  router.push('/board')
}
</script>

<style scoped>
/* Custom colors */
:root {
  --sage-green: #8ba888;
  --deep-sage: #5a7157;
  --sky-blue: #a8c5d6;
  --deep-sky-blue: #7fabc5;
  --light-brown: #d4c5b2;
  --brown: #b8a99a;
  --soft-white: #f8f9fa;
}

.bg-sage-green {
  background-color: var(--sage-green);
}

.hover\:bg-deep-sage:hover {
  background-color: var(--deep-sage);
}

.text-deep-sage {
  color: var(--deep-sage);
}

.text-sage-green {
  color: var(--sage-green);
}

.hover\:text-deep-sage:hover {
  color: var(--deep-sage);
}

.hover\:text-sage-green:hover {
  color: var(--sage-green);
}

.border-sage-green {
  border-color: var(--sage-green);
}

.bg-light-brown {
  background-color: var(--light-brown);
}

.bg-soft-white {
  background-color: var(--soft-white);
}

.text-sky-blue {
  color: var(--sky-blue);
}

.focus\:ring-sage-green:focus {
  --tw-ring-color: var(--sage-green);
}

/* Line clamp for truncating text */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* Prose styling for content */
.prose p {
  margin-bottom: 1rem;
  line-height: 1.7;
}

.prose p:last-child {
  margin-bottom: 0;
}
</style>
