import type { Image } from './image.type'

export interface ShopableImage extends Image {
  url: string
  price: number
}
