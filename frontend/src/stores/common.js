import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCommonStore = defineStore(
    'common',
    () => {
        const usedStorage = ref(0)
        const maxStorage = ref(0)
        const usagePercent = computed(() => {
            if (!maxStorage.value) return 0
            return (usedStorage.value / maxStorage.value) * 100
        })
        return {
            usedStorage,
            maxStorage,
            usagePercent,
        }
    })