<script lang="ts" setup>
import { getCart, resetCart } from '@/services/cart.service'
import { useCartStore } from '@/stores/cart'
import { storeToRefs } from 'pinia'
import { onMounted } from 'vue'
import ShopableThumbImage from '@/components/image/ThumbImage.vue'
import { useToast } from 'vue-toast-notification'
import { makeOrder } from '@/services/shop.service'
import { RouterLink } from 'vue-router'

const cartStore = useCartStore()
const toast = useToast()

const { cartItems: images } = storeToRefs(cartStore)
const { setCartItems, resetCart: localResetCart } = cartStore

onMounted(async () => {
  setCartItems((await getCart()).images)
})

const buy = async () => {
  try {
    await makeOrder()
    localResetCart()
  } catch (error: any) {
    toast.error(error.message)
  }
}

const reset = async () => {
  try {
    await resetCart()
    localResetCart()
  } catch (error: any) {
    toast.error(error.message)
  }
}
</script>
<template>
  <section class="inner-banner py-5">
    <div class="w3l-breadcrumb py-lg-5">
      <div class="container pt-sm-5 pt-4 pb-sm-4">
        <h4 class="inner-text-title font-weight-bold pt-5">Carrello</h4>
        <ul class="breadcrumbs-custom-path pl-0">
          <li>
            <RouterLink to="/">Home</RouterLink>
          </li>

          <li class="active">&nbsp;> Carrello</li>
        </ul>
      </div>
    </div>
  </section>
  <div class="pt-5">
    <div v-if="images.length > 0">
      <div class="mb-4 d-flex justify-content-center">
        <button class="btn btn-primary mx-1" @click="buy()">Completa ordine</button>
        <button class="btn btn-secondary mx-1" @click="reset()">Svuota</button>
      </div>
      <div class="row">
        <div v-for="shopable in images" :key="shopable.id" class="col-md-4 px-4">
          <div class="card">
            <div class="image-container">
              <ShopableThumbImage :id="shopable.id" :label="shopable.label" class="card-img-top" />
            </div>
            <div class="card-body">
              <h5 class="card-title">{{ shopable.label }} &euro; {{ shopable.price }}</h5>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="d-flex align-items-center flex-column pt-5">
      <i class="bi bi-cart-x-fill h1"></i>
      <h3>Il tuo carrello è vuoto</h3>
      <RouterLink to="/orders">
        <button class="btn btn-primary mt-5">I miei ordini</button>
      </RouterLink>
    </div>
  </div>
</template>

<style scoped>
.image-container {
  height: 200px;
  text-align: center;
}
.image-container img {
  max-height: 200px;
  height: auto;
  max-width: fit-content;
}
</style>