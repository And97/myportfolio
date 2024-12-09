<script setup lang="ts">
import { updatePassword } from '@/services/auth.service'
import { getUser, updateUser } from '@/services/user.service'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useToast } from 'vue-toast-notification'

const toast = useToast()
const authStore = useAuthStore()

const { setUser } = authStore
const { user } = storeToRefs(authStore)

const name = ref<string>(user?.value?.name || '')
const surname = ref<string>(user?.value?.surname || '')
const email = ref<string>(user?.value?.email || '')

const oldPassword = ref<string>('')
const newPassword = ref<string>('')

const saveUserInfo = async () => {
  if (user.value) {
    try {
      await updateUser(user.value?.id, name.value, surname.value, email.value)
      setUser(await getUser())
    } catch (error: any) {
      toast.error(error.message)
    }
  }
}

const updatePwd = async () => {
  if (oldPassword.value && newPassword.value) {
    try {
      await updatePassword(oldPassword.value, newPassword.value)
      toast.success('Password aggiornata correttamente')
    } catch (error: any) {
      toast.error(error.message)
    }
  }
}
</script>
<template>
  <h1>Profilo</h1>
  <div class="mt-5 d-flex justify-content-around flex-wrap">
    <div class="mb-5">
      <h2>Info</h2>
      <label for="name" class="form-label">Nome</label>
      <div class="input-group mb-3">
        <input
          v-model="name"
          required
          type="text"
          class="form-control"
          id="name"
          aria-describedby="name"
        />
      </div>
      <label for="surname" class="form-label">Cognome</label>
      <div class="input-group mb-3">
        <input
          v-model="surname"
          required
          type="text"
          class="form-control"
          id="surname"
          aria-describedby="surname"
        />
      </div>
      <label for="email" class="form-label">Email</label>
      <div class="input-group mb-3">
        <input
          v-model="email"
          required
          type="email"
          class="form-control"
          id="email"
          aria-describedby="email"
        />
      </div>
      <button type="button" @click="saveUserInfo" class="btn btn-primary">Salva</button>
    </div>
    <div class="mb-5">
      <h2>Password</h2>
      <label for="old-password" class="form-label">Vecchia password</label>
      <div class="input-group mb-3">
        <input
          v-model="oldPassword"
          required
          type="password"
          class="form-control"
          id="old-password"
          aria-describedby="old-password"
        />
      </div>
      <label for="new-password" class="form-label">Nuova password</label>
      <div class="input-group mb-3">
        <input
          v-model="newPassword"
          required
          type="password"
          class="form-control"
          id="new-password"
          aria-describedby="new-password"
        />
      </div>
      <button type="button" @click="updatePwd" class="btn btn-primary">Salva</button>
    </div>
  </div>
</template>
