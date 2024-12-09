import type { ShopableImage } from './shopable-image.type'

export interface Cart {
  id: number
  images: ShopableImage[]
  userId: number
}
