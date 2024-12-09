<script setup lang="ts">
import { RouterView } from 'vue-router'
import Header from './components/layout/LayoutHeader.vue'
import Footer from './components/layout/LayoutFooter.vue'
import { useAuthStore } from './stores/auth'
import { storeToRefs } from 'pinia'
import { onMounted } from 'vue'
import { getCart } from './services/cart.service'
import { useCartStore } from './stores/cart'

const authStore = useAuthStore()
const cartStore = useCartStore()

const { setCartItems } = cartStore
const { isLoggedIn } = storeToRefs(authStore)

onMounted(async () => {
  if (isLoggedIn.value) {
    const cart = await getCart()
    setCartItems(cart.images)
  }
})
</script>

<template>
  <Header />
  <main class="pb-5 mt-5 container">
    <RouterView />
  </main>
  <Footer />
</template>
