<script lang="ts" setup>
import { ref } from 'vue'

const emit = defineEmits(['imageUploaded'])

const label = ref('')
const imageName = ref<string | null>(null)

const onFileChanged = (event: any) => {
  const target = event.target as HTMLInputElement
  if (target && target.files && target.files.length > 0) {
    imageName.value = target.files[0].name
  } else {
    imageName.value = null
  }
}

const onSubmit = async () => {
  if (!imageName.value || !label.value) {
    return
  }
  emit('imageUploaded', label.value, imageName.value)
  imageName.value = ''
  label.value = ''
}
</script>
<template>
  <div>
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
          <span v-if="imageName"> ({{ imageName }}) </span>
        </label>
        <input class="form-control" type="file" id="image" @change="onFileChanged($event)" />
      </div>
      <div class="text-center mt-5">
        <button type="submit" class="btn btn-primary">Carica in bozze</button>
      </div>
    </form>
  </div>
</template>
