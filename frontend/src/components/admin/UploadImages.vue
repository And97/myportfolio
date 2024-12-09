<script setup lang="ts">
import { inject, onMounted, ref } from 'vue'
import UploadImage from './UploadImage.vue'
import type { Work } from '@/types/work.type'
import { getAllWorks } from '@/services/work.service'
import type { LocalImage } from '@/types/image.type'
import { generateImageThumb, uploadImages } from '@/services/image.service'
import { useToast } from 'vue-toast-notification'
import type { Emitter, EventType } from 'mitt'


const imagesPath = import.meta.env.VITE_IMAGES_BASE_PATH
const imagesThumbPath = imagesPath + 'thumb/'

const emitter: Emitter<Record<EventType, unknown>> | undefined = inject('emitter')
const toast = useToast()

const works = ref<Work[]>([])
const selectedWork = ref<number | null>(null)
const images = ref<LocalImage[]>([])

const onImageUpload = (label: string, imageName: string) => {
  images.value.push({
    label,
    url: imagesPath + imageName,
    thumbnailURL: imagesThumbPath + imageName
  })
}

const onKeyUp = (index: number, e:any) =>{
  images.value[index].url=e.target.value
  const myArray = e.target.value.split("/");
  const imageName = myArray[myArray.length-1];
  myArray[myArray.length-1]="thumb";
  myArray[myArray.length]=imageName;
  images.value[index].thumbnailURL=myArray.join('/')
  console.log( images.value[index].thumbnailURL);
  
}

const deleteImage = (index: number) => {
  images.value.splice(index, 1)
}

const saveImages = async () => {
  if (images.value.length > 0 && selectedWork.value) {
    await uploadImages(selectedWork.value, images.value)
    await generateImageThumb(selectedWork.value)
    images.value = []
    toast.success('Immagini caricata con successo')
    emitter?.emit('work-images-saved')
  }
}

const loadWorks = async () => {
  works.value = await getAllWorks()
}

onMounted(async () => {
  loadWorks()
})

emitter?.on('work-saved', loadWorks)
</script>

<template>
  <h2 class="mt-4">Aggiungi immagini</h2>
  <div class="mt-4">
    <div v-if="works.length > 0">
      <UploadImage @image-uploaded="onImageUpload" />
      <div v-if="images.length > 0">
        <table class="table table-striped mt-5">
          <thead>
            <tr>
              <th scope="col">Etichetta</th>
              <th scope="col">URL</th>
              <th scope="col">URL Thumbnail</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(image, index) in images" :key="index">
              <td>{{ image.label }}</td>
              <td><input type="text" :value="image.url" @keyup="(e)=>onKeyUp(index,e)" ></td>
              <td>{{ image.thumbnailURL }}</td>
              <td>
                <button @click="deleteImage(index)" class="btn btn-danger">
                  <i class="bi bi-trash"></i>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <div class="d-flex align-items-center justify-content-between">
          <select class="form-select" v-model="selectedWork">
            <option v-for="work in works" :value="work.id" v-bind:key="work.id">
              {{ work.title }}
            </option>
          </select>
          <button class="btn btn-primary" @click="saveImages">Salva</button>
        </div>
      </div>
    </div>
    <div v-else class="d-flex align-items-center flex-column pt-5">
      <i class="bi bi-images h1"></i>
      <h3>Nessun lavoro trovato</h3>
    </div>
  </div>
</template>
