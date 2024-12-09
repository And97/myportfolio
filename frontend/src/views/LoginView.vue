<script setup lang="ts">
import { ref } from 'vue'
import { login } from '../services/auth.service'
import { useToast } from 'vue-toast-notification'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { getUser } from '@/services/user.service'
import { getCart } from '@/services/cart.service'
import { useCartStore } from '@/stores/cart'

const toast = useToast()
const { setUser } = useAuthStore()
const { setCartItems } = useCartStore()
const router = useRouter()

const username = ref('')
const password = ref('')

const onSubmit = async () => {
  try {
    await login(username.value, password.value)
    setUser(await getUser())
    setCartItems((await getCart()).images)
    router.push('/')
  } catch (error: any) {
    toast.error(error.message)
  }
}
</script>
<template>
  <div class="text-center">
    <img src="@/assets/logo.png" alt="logo" style="max-width: 100px" />
  </div>
  <form @submit.prevent="onSubmit" class="centered-form mt-5">
    <label for="username" class="form-label">Username</label>
    <div class="input-group mb-3">
      <input
        v-model="username"
        required
        type="text"
        class="form-control"
        id="username"
        aria-describedby="username"
      />
    </div>
    <label for="password" class="form-label">Password</label>
    <div class="input-group mb-3">
      <input
        v-model="password"
        required
        type="password"
        class="form-control"
        id="password"
        aria-describedby="password"
      />
    </div>
    <button type="submit" class="btn btn-primary">Accedi</button>
  </form>
</template>
