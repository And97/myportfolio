<script setup lang="ts">
import { getAllWorks, getWork } from '@/services/work.service'
import type { Work } from '@/types/work.type'
import type { Emitter, EventType } from 'mitt'
import { inject, onMounted, ref, watch } from 'vue'
import WorkDetail from './WorkDetail.vue'
import ThumbImage from '../image/ThumbImage.vue'
import { deleteImage, generateImageThumb } from '@/services/image.service'
import { useToast } from 'vue-toast-notification'
import EditImage from './EditImage.vue'
import { OnClickOutside } from '@vueuse/components'

const emitter: Emitter<Record<EventType, unknown>> | undefined = inject('emitter')
const toast = useToast()

const showUploadModal = ref(false)
const works = ref<Work[]>([])
const selectedWork = ref<Work | undefined>(undefined)
const selectedWorkId = ref<number | null>(null)
const selectedImageId = ref<number | null>(null)
const images = ref<number[]>([])
const forceUpdate = ref(0)

const loadWorks = async () => {
  works.value = await getAllWorks()
}

const updateImage = (id: number) => {
  selectedImageId.value = id
  showUploadModal.value = true
}

const destroyImage = async (id: number) => {
  await deleteImage(id)
  toast.success('Immagine cancellata con successo')
  images.value = images.value.filter((image) => image !== id)
}

onMounted(async () => {
  loadWorks()
})

const onImageUploaded = async () => {
  if (selectedWorkId.value) {
    await generateImageThumb(selectedWorkId.value)
    forceUpdate.value++
  }
  showUploadModal.value = false
}

watch(selectedWorkId, async (newValue) => {
  if (newValue) {
    selectedWork.value = works.value.find((work) => work.id === newValue)
    images.value = (await getWork(newValue)).images
  } else {
    selectedWork.value = undefined
    images.value = []
  }
})

emitter?.on('work-saved', loadWorks)
emitter?.on('work-deleted', () => {
  selectedWorkId.value = null
})
emitter?.on('work-images-saved', async () => {
  if (selectedWorkId.value) {
    images.value = (await getWork(selectedWorkId.value)).images
  }
})
</script>
<template>
  <h2 class="mt-4">Gestisci lavori</h2>
  <div class="mt-4">
    <select class="form-select" v-model="selectedWorkId" v-if="works.length > 0">
      <option v-for="work in works" :value="work.id" v-bind:key="work.id">
        {{ work.title }}
      </option>
    </select>
    <div v-else class="d-flex align-items-center flex-column pt-5">
      <i class="bi bi-images h1"></i>
      <h3>Nessun lavoro trovato</h3>
    </div>
  </div>
  <div v-if="selectedWorkId">
    <WorkDetail :work="selectedWork" />
  </div>
  <div v-if="images" class="row mt-4" :key="forceUpdate">
    <div v-for="image in images" :key="image" class="col-md-4 py-2 px-md-4 py-md-0">
      <div class="card">
        <div class="image-container">
          <ThumbImage :id="image" :shopable="false" class="card-img-top" />
        </div>
        <div class="card-body">
          <button class="btn btn-primary mx-1" @click="updateImage(image)">
            <i class="bi bi-pencil-fill"></i>
          </button>
          <button class="btn btn-danger mx-1" @click="destroyImage(image)">
            <i class="bi bi-trash-fill"></i>
          </button>
        </div>
      </div>
    </div>
    <div class="upload-modal" :class="{ 'd-none': !showUploadModal }" v-if="selectedImageId">
      <OnClickOutside @trigger="showUploadModal = false">
        <div class="upload-modal-body">
          <EditImage :imageId="selectedImageId" @imageUploaded="onImageUploaded" />
        </div>
      </OnClickOutside>
    </div>
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
