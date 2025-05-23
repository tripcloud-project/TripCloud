import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useDriveStore = defineStore(
  'drive',
  () => {
    const prefix = ref('/') // 기본 경로

    const setPrefix = (newPrefix) => {
      prefix.value = newPrefix
    }

    return {
      prefix,
      setPrefix,
    }
  },
  {
    persist: { storage: sessionStorage }, // 새로고침해도 유지
  }
)
