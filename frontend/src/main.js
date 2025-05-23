import { createApp } from 'vue'
import { createPinia } from 'pinia'
import '@/assets/tailwind.css'
import '@fortawesome/fontawesome-free/css/all.css'

import App from './App.vue'
import router from './router'

import { useAuthStore } from '@/stores/useAuthStore'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

app.mount('#app')

const authStore = useAuthStore()
authStore.loadToken()
