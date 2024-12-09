<script setup lang="ts">
import { ref } from 'vue'
import { register } from '../services/auth.service'
import { useToast } from 'vue-toast-notification'
import { useRouter } from 'vue-router'

const toast = useToast()
const router = useRouter()

const name = ref('')
const surname = ref('')
const username = ref('')
const email = ref('')
const password = ref('')

const onSubmit = async () => {
  try {
    await register({
      name: name.value,
      surname: surname.value,
      username: username.value,
      email: email.value,
      password: password.value
    })
    toast.success('Registrazione completata')
    router.push('/login')
  } catch (error: any) {
    toast.error(error.message)
  }
}
</script>
<template>
  <div class="text-center">
    <img src="@/assets/logo.png" alt="logo" style="max-width: 100px" />
  </div>
  <form @submit.prevent="onSubmit" class="centered-form mt-5">
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
    <label for="username" class="form-label">Username</label>
    <div class="input-group mb-3">
      <input
        v-model="username"
        required
        type="text"
        class="form-control"
        id="username"
        aria-describedby="username"
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
    <label for="password" class="form-label">Password</label>
    <div class="input-group mb-3">
      <input
        v-model="password"
        required
        type="password"
        class="form-control"
        id="password"
        aria-describedby="password"
      />
    </div>
    <button type="submit" class="btn btn-primary">Registrati</button>
  </form>
</template>
