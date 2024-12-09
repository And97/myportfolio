import type { Order, OrderDetail } from '@/types/order.type'
import { get, post } from './api.service'

export const makeOrder = async () => {
  const response = await post('api/shop')
  if (!response.ok) {
    const body = await response.json()
    throw new Error(body.message ?? 'Si è verificato un errore durante la creazione dell\'ordine')
  }
}

export const getMyOrders = async (): Promise<Order[]> => {
  const response = await get('api/shop/myorder')
  const body = await response.json()
  if (response.ok) {
    return body
  } else {
    throw new Error(body.message ?? 'Si è verificato un errore durante il recupero degli ordini')
  }
}

export const getAllOrders = async (): Promise<Order[]> => {
  const response = await get('api/shop/all')
  const body = await response.json()
  if (response.ok) {
    return body
  } else {
    throw new Error(body.message ?? 'Si è verificato un errore durante il recupero degli ordini')
  }
}

export const getOrder = async (id: number): Promise<OrderDetail> => {
  const response = await get('api/shop/orderdetail?id=' + id)
  const body = await response.json()
  if (response.ok) {
    return body
  } else {
    throw new Error(body.message ?? 'Si è verificato un errore durante il recupero dell\'ordine')
  }
}

export const verifyHash = async (hash: string) => {
  const response = await post('api/shop/verify_hash?hash=' + hash)
  if (!response.ok) {
    throw new Error('L\'hash inserito non è valido')
  }
}
