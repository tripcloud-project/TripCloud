<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
  <div class="flex flex-col bg-soft-white">
    <!-- Main Content -->
    <main class="flex-grow container mx-auto px-4 py-8">
      <div class="max-w-4xl mx-auto">
        <!-- Page Title -->
        <div class="mb-8">
          <h2 class="text-3xl font-bold text-deep-sage mb-2">글 작성하기</h2>
        </div>

        <!-- Post Form -->
        <div class="bg-white rounded-xl shadow-md border border-sage-green/10 p-6">
          <form @submit.prevent="handleSubmit">
            <!-- Title Input -->
            <div class="mb-6">
              <label for="title" class="block text-deep-sage font-medium mb-2">Post Title</label>
              <input
                type="text"
                id="title"
                v-model="postTitle"
                placeholder="제목을 입력해주세요."
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-sage-green focus:border-transparent"
                :maxlength="100"
                required
              />
              <div class="mt-1 text-right text-sm text-gray-500">
                {{ postTitle.length }}/100 characters
              </div>
            </div>

            <!-- Editor Container -->
            <div class="mb-6">
              <QuillEditor
                @input="handleEditorInput"
                v-model:content="editor"
                contentType="html"
                class="h-[500px]"
              />
              <div class="mt-1 text-sm text-gray-500 flex justify-between">
                <span>{{ characterCount }}/1000 characters</span>
              </div>
            </div>

            <!-- Submit Button -->
            <div class="flex justify-end">
              <button
                type="submit"
                :disabled="!isFormValid || isSubmitting"
                :class="{ 'opacity-50 cursor-not-allowed': !isFormValid || isSubmitting }"
                class="bg-sage-green hover:bg-deep-sage text-white px-8 py-3 rounded-lg transition-colors !rounded-button whitespace-nowrap"
              >
                <span v-if="!isSubmitting">업로드하기</span>
                <span v-else class="flex items-center">
                  <i class="fas fa-spinner fa-spin mr-2"></i>
                  업로드 중...
                </span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import api from '@/lib/api'
import { useRouter } from 'vue-router'

// Form state
const postTitle = ref('')
const editor = ref('')
const characterCount = ref(0)
const isSubmitting = ref(false)

const router = useRouter()

// Handle editor input
const handleEditorInput = () => {
  const div = document.createElement('div')
  div.innerHTML = editor.value
  const text = div.textContent.trim() || div.innerText || ''
  characterCount.value = text.length || 0
}

// Form validation
const isFormValid = computed(() => {
  return postTitle.value.trim() !== '' && characterCount.value > 0 && characterCount.value <= 1000
})

// Handle form submission
const handleSubmit = async () => {
  if (!isFormValid.value) return

  isSubmitting.value = true
  try {
    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1500))
    api.post('/posts', {
      title: postTitle.value,
      content: editor.value,
    })
    alert('게시글 업로드 완료')
    window.location.href = '/board'
  } catch (error) {
    console.error('Error creating post:', error)
    alert('Failed to create post. Please try again.')
  } finally {
    isSubmitting.value = false
  }
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

/* Editor styles */
[contenteditable='true']:empty:before {
  content: 'Share your story here...';
  color: #9ca3af;
}

[contenteditable='true'] {
  white-space: pre-wrap;
}

[contenteditable='true'] blockquote {
  border-left: 3px solid var(--sage-green);
  margin: 1em 0;
  padding-left: 1em;
  color: #4b5563;
}
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
