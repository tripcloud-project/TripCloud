import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { registerQuill } from './lib/quillSetup'
import { useAuthStore } from '@/stores/useAuthStore'

import '@vueup/vue-quill/dist/vue-quill.snow.css'
import '@/assets/tailwind.css'
import '@fortawesome/fontawesome-free/css/all.css'

import App from './App.vue'
import router from './router'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
registerQuill(app)

app.mount('#app')

const authStore = useAuthStore()
authStore.loadToken()
