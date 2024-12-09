import type { ShopableImage } from './shopable-image.type'

export interface Order {
  id: number
  timestamp: string
  username: string
  price: number
  userID: number
  hash: string | null
}

export interface OrderDetail {
  id: number
  hash: string | null
  piece: number
  purchaseImage: ShopableImage[]
  timestamp: string
  totalPrice: number
  username: string
}
