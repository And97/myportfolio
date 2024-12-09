import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { ShopableImage } from '@/types/shopable-image.type'

export const useCartStore = defineStore('cart', () => {
  const cartItems = ref<ShopableImage[]>([])

  const setCartItems = (images: ShopableImage[]) => {
    cartItems.value = images
  }

  const addCartItem = (image: ShopableImage) => {
    if (!cartItems.value.map((i) => i.id).includes(image.id)) {
      cartItems.value.push(image)
    }
  }

  const resetCart = () => {
    cartItems.value = []
  }

  const cartItemsCount = computed(() => cartItems.value.length)

  return { cartItems, cartItemsCount, addCartItem, resetCart, setCartItems }
})
