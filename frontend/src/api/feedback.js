const BASE_URL = import.meta.env.VITE_API_BASE || '/api'

async function request(path, options = {}) {
  const response = await fetch(`${BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })
  const json = await response.json()
  if (!response.ok || json.code !== 0) throw new Error(json.message || '请求失败')
  return json.data
}

export const checkHealth = () => request('/health')
export const submitFeedback = (payload) => request('/feedback', { method: 'POST', body: JSON.stringify(payload) })
export const getStats = () => request('/feedback/stats')
export const getLatest = () => request('/feedback/latest')

export const getDashboard = () => request('/manage/dashboard')
export const getToilets = () => request('/manage/toilets')
export const createToilet = (payload) => request('/manage/toilets', { method: 'POST', body: JSON.stringify(payload) })
export const getRepairs = () => request('/manage/repairs')
export const createRepair = (payload) => request('/manage/repairs', { method: 'POST', body: JSON.stringify(payload) })
export const updateRepairStatus = (id, status) => request(`/manage/repairs/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status }) })
export const getConsumables = () => request('/manage/consumables')
