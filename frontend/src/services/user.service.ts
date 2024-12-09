import { get, put } from './api.service'
import { type User, type UserWithWorks } from '../types/user.type'

export const getAllUsers = async (): Promise<UserWithWorks[]> => {
  const response = await get('api/user/all')
  return response.json()
}

export const getUser = async (): Promise<User> => {
  const response = await get('api/user')
  return response.json()
}

export const updateUser = async (
  id: number,
  name: string,
  surname: string,
  email: string
): Promise<User> => {
  const response = await put('api/user', {
    id,
    name,
    surname,
    email
  })
  const body = await response.json()
  if (response.ok) {
    return body
  } else {
    throw new Error(body.message ?? 'Si è verificato un errore durante il salvataggio dei dati dell\'utente')
  }
}
