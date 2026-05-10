const BASE = 'http://localhost:8080/api/feedback'

async function request(url, options) {
  const res = await fetch(url, options)
  const body = await res.json()
  if (!res.ok || body.code !== 0) {
    throw new Error(body?.message || '请求失败')
  }
  return body.data
}

export async function submitFeedback(payload) {
  return request(BASE, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
}

export async function getStats() {
  return request(`${BASE}/stats`)
}

export async function getLatest(limit = 10) {
  return request(`${BASE}/latest?limit=${limit}`)
}
