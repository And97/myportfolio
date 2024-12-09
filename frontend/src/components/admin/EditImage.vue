<script lang="ts" setup>
import { onMounted, ref, toRefs, watch } from 'vue'
import { useToast } from 'vue-toast-notification'
import { getImage, updateImage } from '@/services/image.service'

const props = defineProps<{
  imageId: number
}>()

const { imageId } = toRefs(props)

const emit = defineEmits(['imageUploaded'])

const imagesPath = import.meta.env.VITE_IMAGES_BASE_PATH
const imagesThumbPath = imagesPath + 'thumb/'

const toast = useToast()

const label = ref('')
const imageUrl = ref('')
const image = ref<string | null>(null)

const onFileChanged = (event: any) => {
  const target = event.target as HTMLInputElement
  if (target && target.files && target.files.length > 0) {
    image.value = target.files[0].name
    imageUrl.value = imagesPath + image.value
  } else {
    image.value = null
  }
}

const setImage = async () => {
  const img = await getImage(imageId.value);
  image.value = null
  label.value = img.label
  imageUrl.value = img.url
}

onMounted(async () => {
  setImage()
})

watch(imageId, async () => {
  setImage()
})

const getThumbUrl = () =>{
  const myArray = imageUrl.value.split("/");
  const imageName = myArray[myArray.length-1];
  myArray[myArray.length-1]="thumb";
  myArray[myArray.length]=imageName;
  return myArray.join('/')
  
}

const onSubmit = async () => {
  if (!image.value || !label.value || !imageUrl.value) {
    return
  }
  try {
    await updateImage(
      imageId.value,
      label.value,
      imageUrl.value,
      getThumbUrl()
    )
    toast.success('Immagine caricata con successo')
    emit('imageUploaded')
    image.value = null
    label.value = ''
  } catch (error: any) {
    toast.error(error.message)
  }
}
</script>
<template>
  <div>
    <h2>Aggiorna l'immagine</h2>
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
      <div class="mb-3">
        <label for="formFile" class="form-label">
          Immagine
          <span v-if="image"> ({{ image }}) </span>
        </label>
        <input class="form-control" type="file" id="image" @change="onFileChanged($event)" />
      </div>
      
      <label for="imageUrl" class="form-label">Url</label>
      <div class="input-group mb-3">

        <input
          v-model="imageUrl"
          required
          type="text"
          class="form-control"
          id="imageUrl"
          aria-describedby="imageUrl"
        />
      </div>
      <div class="text-center mt-5">
        <button type="submit" class="btn btn-primary">Salva</button>
      </div>
    </form>
  </div>
</template>
