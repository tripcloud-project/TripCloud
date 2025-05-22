import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null,
  }),

  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },

    clearToken() {
      this.token = null
      localStorage.removeItem('token')
    },

    loadToken() {
      const savedToken = localStorage.getItem('token')
      if (savedToken) {
        this.token = savedToken
      }
    },
  },
})
