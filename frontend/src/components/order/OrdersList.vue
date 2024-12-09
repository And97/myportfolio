<script lang="ts" setup>
import { getOrder } from '@/services/shop.service'
import type { Order, OrderDetail } from '@/types/order.type'
import moment from 'moment'
import { ref, toRefs } from 'vue'
import ThumbImage from '../image/ThumbImage.vue'
import { OnClickOutside } from '@vueuse/components'
import { getShopableURL } from '@/services/shopable-image.service'

const props = defineProps<{
  orders: Order[]
  showHash?: boolean
}>()

const orderDetail = ref<OrderDetail | null>(null)

const { orders, showHash = false } = toRefs(props)

const showDetail = async (id: number) => {
  orderDetail.value = await getOrder(id)
}
</script>

<template>
  <div class="row">
    <div v-for="order in orders" :key="order.id" class="col-md-3">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title">Ordine #{{ order.id }}</h5>
          <h6 class="card-subtitle mb-2 text-muted">&euro;{{ order.price }}</h6>
          <p class="card-text">
            {{ moment(order.timestamp).format('DD/MM/YYYY HH:mm') }}
          </p>
          <p v-if="showHash">{{ order.hash }}</p>
          <a class="card-link" href="#" @click.prevent="showDetail(order.id)">Dettaglio</a>
        </div>
      </div>
    </div>
  </div>
  <div class="modal" :class="{ 'd-none': !orderDetail }" v-if="orderDetail">
    <OnClickOutside @trigger="orderDetail = null">
      <div class="modal-body row">
        <div v-for="i in orderDetail.purchaseImage" :key="i.id" class="col-md-3">
          <img :src="getShopableURL(i.id)" :alt="i.label" />
          <br><a :href="getShopableURL(i.id)" :download="i.label" class="btn btn-primary mt-2" target="_blank">
          Scarica
        </a>
        </div>
      </div>
    </OnClickOutside>
  </div>
</template>

<style scoped>
img {
  max-width: 300px;
  margin: auto;
}
.modal {
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  left: 0;
  top: 0;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
.modal-body {
  background-color: white;
  padding: 20px 40px;
  border-radius: 10px;
  cursor: default;
  width: 90vw;
}
</style>
