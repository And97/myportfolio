<script lang="ts" setup>
import FullScreenLoadingSpinner from '@/components/FullScreenLoadingSpinner.vue'
import OrdersList from '@/components/order/OrdersList.vue'
import { getMyOrders } from '@/services/shop.service'
import type { Order } from '@/types/order.type'
import { onMounted, ref } from 'vue'

const orders = ref<Order[]>([])
const loading = ref<boolean>(true)

onMounted(async () => {
  orders.value = await getMyOrders()
  loading.value = false
})
</script>
<template>
  <FullScreenLoadingSpinner v-if="loading" />
  <div v-else>
    <div v-if="orders.length > 0">
      <h1 class="mb-5">I miei ordini</h1>
      <OrdersList :orders="orders" />
    </div>
    <div v-else class="d-flex align-items-center flex-column pt-5">
      <i class="bi bi-shop h1"></i>
      <h3>Nessun ordine trovato</h3>
      <RouterLink to="/shop">
        <button class="btn btn-primary mt-5">Effettua il tuo primo ordine</button>
      </RouterLink>
    </div>
  </div>
</template>
