<script setup lang="ts">
import FullScreenLoadingSpinner from '@/components/FullScreenLoadingSpinner.vue'
import ThumbImage from '@/components/image/ThumbImage.vue'
import { getMyWorks, getWork } from '@/services/work.service'
import type { Work } from '@/types/work.type'
import { onMounted, ref, watch } from 'vue'

const works = ref<Work[]>([])
const loading = ref<boolean>(true)
const selectedWork = ref<number | null>(null)
const images = ref<number[]>([])

onMounted(async () => {
  try {
    works.value = await getMyWorks()
  } catch (err) {
    console.error(err)
  }
  loading.value = false
})

watch(selectedWork, async (newWork) => {
  if (newWork) {
    images.value = (await getWork(newWork)).images
  }
})
</script>
<template>
  <section class="inner-banner py-5">
    <div class="w3l-breadcrumb py-lg-5">
      <div class="container pt-sm-5 pt-4 pb-sm-4">
        <h4 class="inner-text-title font-weight-bold pt-5">Galleria</h4>
        <ul class="breadcrumbs-custom-path pl-0">
          <li>
            <RouterLink to="/">Home</RouterLink>
          </li>

          <li class="active">&nbsp;> Galleria </li>
        </ul>
      </div>
    </div>
  </section>
  <FullScreenLoadingSpinner v-if="loading" />
  <div v-else>
    <section class="w3l-gallery py-5" id="gallery">
      <div class="container py-md-5 py-4">
        <div class="title-heading-w3 text-center mx-auto mb-sm-5 mb-4" style="max-width: 700px">
          <h3 class="title-style">Photo Gallery</h3>
          <p class="lead mt-2">
            Benvenuti nella mia galleria fotografica, un viaggio visivo attraverso emozioni, luoghi
            e momenti catturati con passione e professionalità. <br />
            Se vuoi vedere altri lavori non esitare a contattarmi
          </p>
          <br />
          <div v-if="works.length > 0">
            <select v-model="selectedWork" class="form-select" aria-label="Default select example">
              <option v-for="work in works" :value="work.id" v-bind:key="work.id">
                {{ work.title }}
              </option>
            </select>
          </div>
          <div v-else class="d-flex align-items-center flex-column pt-5">
            <i class="bi bi-images h1"></i>
            <h3>Nessun lavoro visibile</h3>
            Contatta l'amministratore
          </div>
        </div>
        <div v-if="images" class="row mt-5">
          <div v-for="image in images" :key="image" class="col-md-4 px-4">
            <div class="card">
              <div class="image-container">
                <ThumbImage :id="image" :shopable="false" class="card-img-top" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
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
