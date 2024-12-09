import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { User } from '@/types/user.type'
import type { RoleEnum } from '@/types/role.type'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)

  const setUser = (authUser: User | null) => {
    user.value = authUser
  }

  const hasRole = (role: RoleEnum) => {
    return user.value?.role.map((r) => r.name).includes(role)
  }

  const resetAuth = () => {
    user.value = null
  }

  const isLoggedIn = computed(() => user.value !== null)

  return { user, isLoggedIn, setUser, hasRole, resetAuth }
})
