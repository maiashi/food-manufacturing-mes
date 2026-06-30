import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

interface UserInfo {
  userId: string
  loginId: string
  name: string
  factoryCode?: string
  roleNames: string[]
}

export const useAuthStore = defineStore(
  'auth',
  () => {
    const token = ref<string | null>(localStorage.getItem('mes_token'))
    const userInfo = ref<UserInfo | null>(JSON.parse(localStorage.getItem('mes_user') || 'null'))

    const isAuthenticated = computed(() => !!token.value)
    const currentFactory = computed(() => userInfo.value?.factoryCode)

    function setAuth(tokenValue: string, user: UserInfo) {
      token.value = tokenValue
      userInfo.value = user
      localStorage.setItem('mes_token', tokenValue)
      localStorage.setItem('mes_user', JSON.stringify(user))
    }

    function logout() {
      token.value = null
      userInfo.value = null
      localStorage.removeItem('mes_token')
      localStorage.removeItem('mes_user')
    }

    return { token, userInfo, isAuthenticated, currentFactory, setAuth, logout }
  },
  { persist: true },
)
