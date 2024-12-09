<script setup lang="ts">
import { verifyHash } from '@/services/shop.service'
import { ref } from 'vue'
import { useToast } from 'vue-toast-notification'

const toast = useToast()

const hash = ref('')

const checkHash = async () => {
  try {
    await verifyHash(hash.value)
    toast.success('L\'hash inserito è valido')
  } catch (error: any) {
    toast.error(error.message)
  }
}
</script>
<template>
  <div class="mt-4">
    <h2>Verifica hash</h2>
    <label for="hash" class="form-label">Hash</label>
    <div class="input-group mb-3">
      <input
        v-model="hash"
        required
        type="text"
        class="form-control"
        id="hash"
        aria-describedby="hash"
      />
    </div>
    <button type="button" @click="checkHash" class="btn btn-primary">Verifica</button>
  </div>
</template>
