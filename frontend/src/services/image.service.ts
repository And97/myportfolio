import type { Image, LocalImage } from '@/types/image.type'
import { destroy, get, post, put } from './api.service'

export const getImageThumbURL = (id: number) => {
  return import.meta.env.VITE_API_URL + '/api/image/thumb?id=' + id
}

export const getImageURL = (id: number) => {
  return import.meta.env.VITE_API_URL + '/api/image?id=' + id
}

export const uploadImages = async (workId: number, images: LocalImage[]) => {
  const response = await post('api/image?workId=' + workId, images)
  if (!response.ok) {
    const body = await response.json()
    throw new Error(body.message ?? 'Si è verificato un errore durante il salvataggio dell\'immagine')
  }
}

export const generateImageThumb = async (workId: number) => {
  const response = await post('api/thumbnail?id=' + workId)
  if (!response.ok) {
    const body = await response.json()
    throw new Error(body.message ?? 'Si è verificato un errore durante la creazione dell\'immagine thumbnail')
  }
}

export const updateImage = async (id: number, label: string, url: string, thumbnailURL: string) => {
  const response = await put('api/image?id=' + id, {
    id,
    label,
    url,
    thumbnailURL
  })
  if (!response.ok) {
    const body = await response.json()
    throw new Error(body.message ?? 'Si è verificato un errore durante l\'aggiornamento dell\'immagine')
  }
}

export const deleteImage = async (id: number) => {
  const response = await destroy('api/image?id=' + id)
  if (!response.ok) {
    const body = await response.json()
    throw new Error(body.message ?? 'Si è verificato un errore durante l\'eliminazione dell\'immagine')
  }
}

export const getImage = async (id: number): Promise<Image> => {
  const response = await get('api/image/detail?id=' + id)
  const body = await response.json()
  if (response.ok) {
    return body
  } else {
    throw new Error(body.message ?? 'Si è verificato un errore durante il recupero dell\'immagine')
  }
}