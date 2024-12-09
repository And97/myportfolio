<script lang="ts" setup>
import { getAllUsers } from '@/services/user.service'
import { getAllWorks, saveVisibleWorks } from '@/services/work.service'
import type { UserWithWorks } from '@/types/user.type'
import type { Work } from '@/types/work.type'
import type { Emitter, EventType } from 'mitt'
import moment from 'moment'
import { inject, onMounted, ref, watch } from 'vue'
import { useToast } from 'vue-toast-notification'

const emitter: Emitter<Record<EventType, unknown>> | undefined = inject('emitter')
const toast = useToast()

const users = ref<UserWithWorks[]>([])
const selectedUser = ref<number | null>(null)
const works = ref<Work[]>([])

const loadWorks = async () => {
  works.value = await getAllWorks()
}

const permissions = ref<{ [key: number]: boolean }>({})

const savePermissions = async () => {
  if (selectedUser.value) {
    const visibleWorks = Object.entries(permissions.value)
      .filter(([_, value]) => value)
      .map(([key, _]) => parseInt(key))
    await saveVisibleWorks(selectedUser.value, visibleWorks)
    toast.success('Permessi salvati con successo')
  }
}

onMounted(async () => {
  loadWorks()
  users.value = await getAllUsers()
})

const userCanViewWork = (workId: number): boolean => {
  const userWorks = users.value.find((user) => user.id === selectedUser.value)?.visibleWorks || []
  return userWorks.find((userWork) => userWork.id === workId) !== undefined
}

watch(selectedUser, (newUser) => {
  if (newUser) {
    permissions.value = {}

    for (const work of works.value) {
      permissions.value[work.id] = userCanViewWork(work.id)
    }
  }
})

emitter?.on('work-saved', loadWorks)
</script>
<template>
  <h2 class="mt-4">Gestisci la visibilit&agrave; dei lavori</h2>
  <div v-if="users.length > 0 && works.length > 0">
    <div class="d-flex align-items-center">
      <select class="form-select me-5" v-model="selectedUser">
        <option v-for="user in users" :value="user.id" v-bind:key="user.id">
          {{ user.name }} {{ user.surname }}
        </option>
      </select>
      <button class="btn btn-primary" @click="savePermissions">Salva</button>
    </div>
    <table class="table table-striped mt-5" :key="selectedUser || 0" v-if="selectedUser">
      <thead>
        <tr>
          <th scope="col">Titolo</th>
          <th scope="col">Azienda</th>
          <th scope="col">Data di completamento</th>
          <th scope="col">Visibile</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="work in works" :key="work.id">
          <td>{{ work.title }}</td>
          <td>{{ work.company }}</td>
          <td>{{ moment(work.completionDate).format('DD/MM/YYYY') }}</td>
          <td>
            <input
              class="form-check-input"
              type="checkbox"
              value=""
              id="flexCheckChecked"
              v-model="permissions[work.id]"
            />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  <div v-else-if="users.length === 0" class="d-flex align-items-center flex-column pt-5">
    <i class="bi bi-people-fill h1"></i>
    <h3>Nessun utente trovato</h3>
  </div>
  <div v-else class="d-flex align-items-center flex-column pt-5 mt-4">
    <i class="bi bi-images h1"></i>
    <h3>Nessun lavoro trovato</h3>
  </div>
</template>
