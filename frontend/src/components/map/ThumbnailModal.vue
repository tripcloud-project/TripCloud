<template>
  <div class="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-xl shadow-lg w-80">
      <div class="mb-5">
        <p class="mb-4 font-semibold text-center text-deep-sage text-lg">
          썸네일 지역을 선택하세요
        </p>

        <div class="flex flex-col items-center gap-3">
          <button
            v-for="opt in thumbnailCandidate.options"
            :key="opt.label"
            @click="selectedRegion = opt"
            :class="[
              'w-full px-4 py-2 rounded-lg border text-sm font-medium transition duration-200',
              selectedRegion?.label === opt.label
                ? 'bg-deep-sage text-white border-deep-sage'
                : 'bg-white text-black border-sage-green hover:bg-sage-green/10 hover:border-deep-sage hover:text-deep-sage',
            ]"
          >
            {{ opt.label }}
          </button>
        </div>
      </div>

      <div class="flex justify-end gap-2">
        <button
          @click="showThumbnailDialog = false"
          class="px-4 py-2 rounded-lg border border-sage-green text-sage-green hover:bg-sage-green/10 hover:text-deep-sage transition"
        >
          취소
        </button>
        <button
          @click="confirmRegion"
          :disabled="!selectedRegion"
          class="px-4 py-2 rounded-lg bg-deep-sage text-white hover:bg-sage-green transition disabled:opacity-50"
        >
          확인
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useMapStore } from '@/stores/map.js'
import { storeToRefs } from 'pinia'
const mapStore = useMapStore()
const { showThumbnailDialog, thumbnailCandidate, selectedRegion } = storeToRefs(mapStore)
const { setThumbnailFile } = mapStore

const confirmRegion = () => {
  if (selectedRegion.value) {
    const { type } = selectedRegion.value
    setThumbnailFile(thumbnailCandidate.value.id, type)
    showThumbnailDialog.value = false
  }
}
</script>
