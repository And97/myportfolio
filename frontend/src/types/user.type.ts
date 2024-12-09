import type { Role } from './role.type'
import type { Work } from './work.type'

export interface AuthUser {
  id: number
  email: string
  roles: string[]
  username: string
}

export interface User {
  id: number
  email: string
  name: string
  surname: string
  role: Role[]
}

export interface UserWithWorks extends User {
  visibleWorks: Work[]
}

export interface RegistrationUserData {
  name: string
  surname: string
  email: string
  username: string
  password: string
}
