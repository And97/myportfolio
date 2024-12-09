export const get = async (endpoint: string) => {
  return await request(endpoint, 'GET')
}

export const post = async (endpoint: string, data?: any) => {
  return await request(endpoint, 'POST', data)
}

export const put = async (endpoint: string, data?: any) => {
  return await request(endpoint, 'PUT', data)
}

export const patch = async (endpoint: string, data?: any) => {
  return await request(endpoint, 'PATCH', data)
}

export const destroy = async (endpoint: string) => {
  return await request(endpoint, 'DELETE')
}

const request = async (endpoint: string, method: string, data?: any) => {
  try {
    const params: RequestInit = {
      method,
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    }
    if (data) {
      params.body = JSON.stringify(data)
    }
    const response = await fetch(`${import.meta.env.VITE_API_URL}/${endpoint}`, params)
    if (!response.ok && response.status >= 500) {
      throw new Error('Si è verificato un errore, riprovare più tardi')
    }
    return response
  } catch (error) {
    throw new Error('Si è verificato un errore, riprovare più tardi')
  }
}
