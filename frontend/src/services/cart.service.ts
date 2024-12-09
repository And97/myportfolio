import type { Cart } from '@/types/cart.type'
import { destroy, get, post } from './api.service'

export const addImageToCart = async (imageId: number): Promise<Cart> => {
  const response = await post('api/cart?shopableImageId=' + imageId)
  const body = await response.json()
  if (response.ok) {
    return body
  } else {
    throw new Error(body.message ?? 'Si è verificato un errore durante l\'aggiunta dell\'immagine al carrello')
  }
}

export const getCart = async (): Promise<Cart> => {
  const response = await get('api/cart')
  const body = await response.json()
  if (response.ok) {
    return body
  } else {
    throw new Error(body.message ?? 'Si è verificato un errore durante il recupero del carrello')
  }
}

export const resetCart = async () => {
  const response = await destroy('api/cart/empty')
  if (!response.ok) {
    const body = await response.json()
    throw new Error(body.message ?? 'Si è verificato un errore durante lo svuotamento del carrello')
  }
}
