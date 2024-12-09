<script setup lang="ts">
import { inject, ref, toRefs, watch } from 'vue'
import { useToast } from 'vue-toast-notification'
import { createWork, deleteWork, updateWork } from '@/services/work.service'
import type { Emitter, EventType } from 'mitt'
import type { Work } from '@/types/work.type'
import moment from 'moment'

const toast = useToast()
const emitter: Emitter<Record<EventType, unknown>> | undefined = inject('emitter')

const props = defineProps<{
  work?: Work
}>()

const { work } = toRefs(props)

const title = ref(work.value?.title || '')
const company = ref(work.value?.company || '')
const completionDate = ref(moment(work.value?.completionDate).format('YYYY-MM-DD') || '')

watch(work, (newValue) => {
  title.value = newValue?.title || ''
  company.value = newValue?.company || ''
  completionDate.value = moment(work.value?.completionDate).format('YYYY-MM-DD') || ''
})

const onSubmit = async () => {
  try {
    if (work.value?.id) {
      await updateWork(work.value?.id, title.value, company.value, completionDate.value)
      toast.success('Lavoro aggiornato correttamente')
    } else {
      await createWork(title.value, company.value, completionDate.value)
      toast.success('Lavoro creato correttamente')
      title.value = ''
      company.value = ''
      completionDate.value = ''
    }
    emitter?.emit('work-saved')
  } catch (error: any) {
    toast.error(error.message)
  }
}

const destroyWork = async () => {
  if (work.value?.id) {
    try {
      await deleteWork(work.value?.id)
      emitter?.emit('work-saved')
      emitter?.emit('work-deleted')
      toast.success('Lavoro salvato con successo')
    } catch (error: any) {
      toast.error(error.message)
    }
  }
}
</script>
<template>
  <h2 class="mt-4" v-if="work?.id">Aggiorna lavoro</h2>
  <h2 class="mt-4" v-else>Aggiungi nuovo lavoro</h2>
  <form @submit.prevent="onSubmit" class="centered-form mt-5">
    <label for="title" class="form-label">Titolo</label>
    <div class="input-group mb-3">
      <input
        v-model="title"
        required
        type="text"
        class="form-control"
        id="title"
        aria-describedby="title"
      />
    </div>
    <label for="company" class="form-label">Azienda</label>
    <div class="input-group mb-3">
      <input
        v-model="company"
        required
        type="text"
        class="form-control"
        id="company"
        aria-describedby="company"
      />
    </div>
    <label for="completionDate" class="form-label">Data di completamento</label>
    <div class="input-group mb-3">
      <input
        v-model="completionDate"
        required
        type="date"
        class="form-control"
        id="completionDate"
        aria-describedby="completionDate"
      />
    </div>
    <button type="submit" class="btn btn-primary mx-1">Salva</button>
    <button type="button" class="btn btn-danger mx-1" @click="destroyWork" v-if="work?.id">
      Elimina
    </button>
  </form>
</template>
