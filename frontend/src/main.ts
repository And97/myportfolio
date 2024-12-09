import './assets/main.css'
import './assets/starter.css'
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'
import 'bootstrap-icons/font/bootstrap-icons.min.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import ToastPlugin from 'vue-toast-notification'
import 'vue-toast-notification/dist/theme-bootstrap.css'
import App from './App.vue'
import router from './router'

import { getUser } from './services/user.service'
import { useAuthStore } from './stores/auth'

import VueSweetalert2 from 'vue-sweetalert2'
import 'sweetalert2/dist/sweetalert2.min.css'
import mitt from 'mitt'

const app = createApp(App)

app.provide('emitter', mitt())
app.use(ToastPlugin)
app.use(VueSweetalert2)
app.use(createPinia())

const { setUser, resetAuth } = useAuthStore()
try {
  const user = await getUser()
  if (user.id) {
    setUser(user)
  } else {
    resetAuth()
  }
} catch (err) {
  resetAuth()
  console.error(err)
}

app.use(router)

app.mount('#app')
