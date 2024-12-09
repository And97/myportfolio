<script lang="ts" setup>
import { getAllShopableImages } from '@/services/shopable-image.service'
import type { ShopableImage } from '@/types/shopable-image.type'
import { onMounted, ref } from 'vue'
import ShopableThumbImage from '@/components/image/ThumbImage.vue'
import { useCartStore } from '@/stores/cart'
import { storeToRefs } from 'pinia'
import { addImageToCart } from '@/services/cart.service'
import { useToast } from 'vue-toast-notification'
import { RouterLink } from 'vue-router'

const images = ref<ShopableImage[]>([])

const toast = useToast()
const cartStore = useCartStore()

const { cartItems, cartItemsCount } = storeToRefs(cartStore)
const { addCartItem } = cartStore

onMounted(async () => {
  images.value = await getAllShopableImages()
})

const addToCart = async (shopable: ShopableImage) => {
  try {
    await addImageToCart(shopable.id)
    addCartItem(shopable)
    toast.success('Immagine aggiunta al carrello')
  } catch (error: any) {
    toast.error(error.message)
  }
}

const cartContainsImage = (shopable: ShopableImage) => {
  return cartItems.value.map((i) => i.id).includes(shopable.id)
}
</script>
<template>
  <section class="inner-banner py-5">
    <div class="w3l-breadcrumb py-lg-5">
      <div class="container pt-sm-5 pt-4 pb-sm-4">
        <h4 class="inner-text-title font-weight-bold pt-5">Shop</h4>
        <ul class="breadcrumbs-custom-path pl-0">
          <li>
            <RouterLink to="/">Home</RouterLink>
          </li>

          <li class="active">&nbsp;> Shop</li>
        </ul>
      </div>
    </div>
  </section>
  <section class="w3l-gallery py-5" id="gallery">
    <div class="container py-md-5 py-4">
      <div class="title-heading-w3 text-center mx-auto mb-sm-5 mb-4" style="max-width: 700px">
        <h3 class="title-style">Shop</h3>
      </div>
      <div v-if="images.length > 0" class="row">
        <div
          v-for="shopable in images"
          :key="shopable.id"
          class="col-md-4 px-4"
          style="display: grid; grid-auto-rows: 1fr"
        >
          <div class="card position-relative">
            <div class="image-container">
              <ShopableThumbImage :id="shopable.id" :label="shopable.label" :shopable="true" class="card-img-top" />
            </div>
            <div class="card-body position-absolute price">
              <h5 class="card-title">{{ shopable.label }} &euro; {{ shopable.price }}</h5>
            </div>
          </div>
          <div class="text-center mt-2">
            <button
              v-if="!cartContainsImage(shopable)"
              class="btn btn-primary"
              @click="addToCart(shopable)"
            >
              <i class="bi bi-cart-plus-fill"></i> Aggiungi al carrello
            </button>
          </div>
        </div>
      </div>
      <div v-else class="d-flex align-items-center flex-column pt-5">
        <i class="bi bi-images h1"></i>
        <h3>Nessuna immagine trovata</h3>
      </div>
    </div>
  </section>
</template>

<style scoped>
.price {
  width: 100%;
  bottom: 0;
  text-align: right;
  background-color: rgba(255, 255, 255, 0.7);
}
.image-container {
  height: 200px;
}
.image-container img {
  max-height: 200px;
  height: auto;
  max-width: fit-content;
}
</style>
