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
            <i class="fas fa-arrow-left mr-1"></i>
            <span>Back to Board</span>
          </button>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-grow container mx-auto px-4 py-8">
      <div class="max-w-4xl mx-auto">
        <!-- Page Title -->
        <div class="mb-8">
          <h2 class="text-3xl font-bold text-deep-sage mb-2">Create New Post</h2>
          <p class="text-gray-600">Share your nature experience with the community</p>
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
                placeholder="Enter your post title"
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
                <span>Supports basic formatting</span>
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
                <span v-if="!isSubmitting">Create Post</span>
                <span v-else class="flex items-center">
                  <i class="fas fa-spinner fa-spin mr-2"></i>
                  Publishing...
                </span>
              </button>
            </div>
          </form>
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
            © 2025 NatureBoard. All rights reserved.
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
import { ref, computed, onMounted } from 'vue'

// Form state
const postTitle = ref('')
const selectedCategory = ref('')
const editor = ref('')
const characterCount = ref(0)
const isSubmitting = ref(false)

// Handle editor input
const handleEditorInput = () => {
  console.log(editor.value)
  const div = document.createElement('div')
  div.innerHTML = editor.value
  const text = div.textContent || div.innerText || ''
  characterCount.value = text.length || 0
}

// Form validation
const isFormValid = computed(() => {
  return (
    postTitle.value.trim() !== '' &&
    selectedCategory.value !== '' &&
    characterCount.value > 0 &&
    characterCount.value <= 1000
  )
})

// Handle form submission
const handleSubmit = async () => {
  if (!isFormValid.value) return

  isSubmitting.value = true
  try {
    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1500))

    // Reset form
    postTitle.value = ''
    selectedCategory.value = ''
    if (editor.value) {
      editor.value.innerHTML = ''
    }
    characterCount.value = 0

    // Show success message (you can implement your own notification system)
    alert('Post created successfully!')
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
