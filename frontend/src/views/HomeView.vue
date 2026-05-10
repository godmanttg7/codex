<template>
  <main class="page">
    <h1>公厕反馈中心</h1>
    <p v-if="error" class="error">{{ error }}</p>
    <section class="card stats">
      <div>总反馈：{{ stats.totalCount }}</div>
      <div>卫生评分：{{ stats.avgCleanliness }}</div>
      <div>排队评分：{{ stats.avgQueue }}</div>
      <div>气味评分：{{ stats.avgOdor }}</div>
    </section>

    <section class="card">
      <h2>提交反馈</h2>
      <form @submit.prevent="onSubmit" class="form">
        <input v-model="form.toiletName" placeholder="公厕名称" required />
        <input v-model="form.area" placeholder="区域" required />
        <input v-model.number="form.cleanlinessScore" type="number" min="1" max="5" placeholder="卫生(1-5)" required />
        <input v-model.number="form.queueScore" type="number" min="1" max="5" placeholder="排队(1-5)" required />
        <input v-model.number="form.odorScore" type="number" min="1" max="5" placeholder="气味(1-5)" required />
        <input v-model="form.visitedAt" type="datetime-local" required />
        <textarea v-model="form.comment" placeholder="补充意见"></textarea>
        <button :disabled="loading">{{ loading ? '提交中...' : '提交' }}</button>
      </form>
    </section>

    <section class="card">
      <h2>最新反馈</h2>
      <ul>
        <li v-for="item in latest" :key="item.id">
          {{ item.toiletName }}（{{ item.area }}）- 卫生{{ item.cleanlinessScore }} / 排队{{ item.queueScore }} / 气味{{ item.odorScore }}
        </li>
      </ul>
    </section>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getLatest, getStats, submitFeedback } from '../api/feedback'

const stats = reactive({ totalCount: 0, avgCleanliness: 0, avgQueue: 0, avgOdor: 0 })
const latest = ref([])
const loading = ref(false)
const error = ref('')
const form = reactive({ toiletName: '', area: '', cleanlinessScore: 5, queueScore: 5, odorScore: 5, comment: '', visitedAt: '' })

async function load() {
  error.value = ''
  try {
    Object.assign(stats, await getStats())
    latest.value = await getLatest()
  } catch (e) {
    error.value = e.message
  }
}

async function onSubmit() {
  loading.value = true
  error.value = ''
  try {
    await submitFeedback({ ...form, visitedAt: `${form.visitedAt}:00` })
    await load()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
