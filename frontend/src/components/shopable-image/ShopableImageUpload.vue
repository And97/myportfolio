<script lang="ts" setup>
import { ref, toRefs, watch } from 'vue'
import {
  createShopableImage,
  generateShopableImageThumb,
  updateShopableImage
} from '@/services/shopable-image.service'
import { useToast } from 'vue-toast-notification'
import type { ShopableImage } from '@/types/shopable-image.type'

const props = defineProps<{
  shopableImage?: ShopableImage
}>()

const { shopableImage } = toRefs(props)

const emit = defineEmits(['imageUploaded'])

const imagesPath = import.meta.env.VITE_SHOPABLEIMAGES_BASE_PATH
const imagesThumbPath = imagesPath + 'thumb/'

const toast = useToast()

const label = ref(shopableImage.value?.label || '')
const price = ref(shopableImage.value?.price || 0)
const image = ref<string | null>(null)

watch(shopableImage, (newValue) => {
  label.value = newValue?.label || ''
  price.value = newValue?.price || 0
})

const onFileChanged = (event: any) => {
  const target = event.target as HTMLInputElement
  if (target && target.files && target.files.length > 0) {
    image.value = target.files[0].name
  } else {
    image.value = null
  }
}

const onSubmit = async () => {
  if (!image.value || !label.value || !price.value) {
    return
  }
  try {
    if (shopableImage.value?.id) {
      await updateShopableImage(
        shopableImage.value?.id,
        label.value,
        imagesPath + image.value,
        imagesThumbPath + image.value,
        price.value
      )
    } else {
      await createShopableImage(
        label.value,
        imagesPath + image.value,
        imagesThumbPath + image.value,
        price.value
      )
    }
    await generateShopableImageThumb()
    toast.success('Immagine caricata con successo')
    emit('imageUploaded')
    image.value = null
    label.value = ''
    price.value = 0
  } catch (error: any) {
    toast.error(error.message)
  }
}
</script>
<template>
  <div>
    <h2>Carica una nuova immagine</h2>
    <form @submit.prevent="onSubmit" class="centered-form mt-5">
      <label for="label" class="form-label">Etichetta</label>
      <div class="input-group mb-3">
        <input
          v-model="label"
          required
          type="text"
          class="form-control"
          id="label"
          aria-describedby="label"
        />
      </div>
      <label for="price" class="form-label">Prezzo</label>
      <div class="input-group mb-3">
        <input
          v-model="price"
          required
          type="number"
          step=".01"
          class="form-control"
          id="price"
          aria-describedby="price"
        />
      </div>
      <div class="mb-3">
        <label for="formFile" class="form-label">
          Immagine
          <span v-if="image"> ({{ image }}) </span>
        </label>
        <input class="form-control" type="file" id="image" @change="onFileChanged($event)" />
      </div>
      <div class="text-center mt-5">
        <button type="submit" class="btn btn-primary">Carica</button>
      </div>
    </form>
  </div>
</template>
