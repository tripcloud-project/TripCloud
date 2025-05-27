<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
    @keydown.esc="$emit('cancel')"
    @keydown.enter="submit"
    tabindex="0"
  >
    <div class="bg-white p-6 rounded-xl w-full max-w-md shadow-lg">
      <h2 class="text-xl font-semibold mb-4">{{ title }}</h2>
      <input
        v-model="value"
        type="text"
        class="w-full p-2 border border-gray-300 rounded-md"
        placeholder="입력하세요"
        ref="inputRef"
      />
      <div class="mt-4 flex justify-end space-x-2">
        <button @click="$emit('cancel')" class="px-4 py-2 bg-gray-300 rounded-md">취소</button>
        <button @click="submit" class="px-4 py-2 bg-blue-600 text-white rounded-md">확인</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'

const props = defineProps({ title: String, modelValue: String })
const emit = defineEmits(['update:modelValue', 'submit', 'cancel'])

const value = ref(props.modelValue)
const inputRef = ref(null)

watch(() => props.modelValue, val => value.value = val)

function submit() {
  emit('submit', value.value)
}

onMounted(() => {
  inputRef.value?.focus()
})
</script>
