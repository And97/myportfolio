<script lang="ts" setup>
import OrdersList from '@/components/order/OrdersList.vue'
import { getAllOrders } from '@/services/shop.service'
import type { Order } from '@/types/order.type'
import { onMounted, ref } from 'vue'

const orders = ref<Order[]>([])

const loadOrders = async () => {
  orders.value = await getAllOrders()
}
onMounted(() => {
  loadOrders()
})
</script>
<template>
  <div class="mt-4">
    <div v-if="orders.length > 0">
      <h2 class="mb-5">Ordini</h2>
      <OrdersList :orders="orders" :show-hash="true" />
    </div>
    <div v-else class="d-flex align-items-center flex-column pt-5">
      <i class="bi bi-shop h1"></i>
      <h3>Nessun ordine trovato</h3>
    </div>
  </div>
</template>
