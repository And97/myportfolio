export enum RoleEnum {
  user = 'ROLE_USER',
  admin = 'ROLE_ADMIN'
}

export interface Role {
  id: number
  name: RoleEnum
}
