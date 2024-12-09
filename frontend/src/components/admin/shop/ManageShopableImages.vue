<script lang="ts" setup>
import type { ShopableImage } from '@/types/shopable-image.type'
import { onMounted, ref } from 'vue'
import Swal from 'sweetalert2'
import { deleteShopableImage, getAllShopableImages } from '@/services/shopable-image.service'
import { useToast } from 'vue-toast-notification'
import ThumbImage from '@/components/image/ThumbImage.vue'
import ShopableImageUpload from '@/components/shopable-image/ShopableImageUpload.vue'
import { OnClickOutside } from '@vueuse/components'

const toast = useToast()
const key = ref(0)
const images = ref<ShopableImage[]>([])

const showUploadModal = ref(false)
const selectedShopableImage = ref<ShopableImage | undefined>(undefined)

const updateImage = (id: number) => {
  selectedShopableImage.value = images.value.find((image) => image.id === id)
  showUploadModal.value = true
}

const deleteImage = async (id: number) => {
  Swal.fire({
    title: 'Sicuro di voler continuare?',
    text: 'Non potrai tornare indietro!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Continua',
    cancelButtonText: 'Annulla'
  }).then(async (result) => {
    if (result.isConfirmed) {
      await deleteShopableImage(id)
      await loadAllShopableImages()
      toast.success('Immagine eliminata con successo')
    }
  })
}

const onImageUpload = () => {
  loadAllShopableImages()
  showUploadModal.value = false
  selectedShopableImage.value = undefined
}

const uploadNewImage = () => {
  selectedShopableImage.value = undefined
  showUploadModal.value = true
}

const loadAllShopableImages = async () => {
  images.value = await getAllShopableImages()
  key.value++
}

onMounted(() => {
  loadAllShopableImages()
})
</script>
<template>
  <h2 class="mb-3 d-flex mt-4">
    Immagini
    <button class="btn btn-primary ms-auto" @click="uploadNewImage()" v-if="images.length > 0">
      <i class="bi bi-plus"></i>
    </button>
  </h2>
  <div :key="key">
    <div v-if="images.length > 0" class="row mt-5">
      <div v-for="shopable in images" :key="shopable.id" class="col-md-4 px-4">
        <div class="card">
          <div class="image-container">
            <ThumbImage :id="shopable.id" :label="shopable.label" :shopable="true" class="card-img-top" />
          </div>
          <div class="card-body">
            <h5 class="card-title">{{ shopable.label }} &euro; {{ shopable.price }}</h5>
            <button class="btn btn-primary mx-1" @click="updateImage(shopable.id)">
              <i class="bi bi-pencil-fill"></i>
            </button>
            <button class="btn btn-danger mx-1" @click="deleteImage(shopable.id)">
              <i class="bi bi-trash-fill"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="d-flex align-items-center flex-column pt-5">
      <i class="bi bi-images h1"></i>
      <h3>Nessuna immagine trovata</h3>
      <button class="btn btn-primary mt-3" @click="uploadNewImage()">
        Carica la tua prima immagine
      </button>
    </div>
  </div>
  <div class="upload-modal" :class="{ 'd-none': !showUploadModal }">
    <OnClickOutside @trigger="showUploadModal = false">
      <div class="upload-modal-body">
        <ShopableImageUpload
          :shopable-image="selectedShopableImage"
          @image-uploaded="onImageUpload()"
        />
      </div>
    </OnClickOutside>
  </div>
</template>

<style scoped>
.upload-modal {
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  left: 0;
  top: 0;
  z-index: 10000000;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
.upload-modal-body {
  background-color: white;
  padding: 20px 40px;
  border-radius: 10px;
  cursor: default;
}
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
