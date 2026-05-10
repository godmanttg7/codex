<template>
  <main class="page">
    <h1>智慧公厕管理系统</h1>
    <p v-if="error" class="error">{{ error }}</p>
    <section class="card">
      <div>前端端口检测：<b :class="{ warn: frontendPort !== '5173' }">{{ frontendPort }}</b></div>
      <div>后端连通性：<b :class="{ warn: !backendUp }">{{ backendUp ? '正常' : '异常' }}</b>（服务端口：{{ backendPort }}）</div>
    </section>

    <section class="card stats">
      <div>公厕总数：{{ dashboard.toiletCount }}</div>
      <div>反馈总数：{{ dashboard.feedbackCount }}</div>
      <div>待处理工单：{{ dashboard.repairPendingCount }}</div>
      <div>低库存预警：{{ dashboard.lowStockCount }}</div>
    </section>

    <section class="grid-2">
      <div class="card">
        <h2>公厕基础信息</h2>
        <form @submit.prevent="onCreateToilet" class="form compact">
          <input v-model="toiletForm.toiletCode" placeholder="编码" required />
          <input v-model="toiletForm.name" placeholder="名称" required />
          <input v-model="toiletForm.address" placeholder="地址" required />
          <input v-model="toiletForm.district" placeholder="区域" />
          <input v-model="toiletForm.openTime" placeholder="开放时间" />
          <button>新增公厕</button>
        </form>
        <ul><li v-for="t in toilets" :key="t.id">{{ t.name }} - {{ t.address }}</li></ul>
      </div>

      <div class="card">
        <h2>报修工单</h2>
        <form @submit.prevent="onCreateRepair" class="form compact">
          <select v-model.number="repairForm.toiletId" required>
            <option disabled value="">选择公厕</option>
            <option v-for="t in toilets" :key="t.id" :value="t.id">{{ t.name }}</option>
          </select>
          <input v-model="repairForm.faultDesc" placeholder="故障描述" required />
          <input v-model="repairForm.reporter" placeholder="上报人" required />
          <button>创建工单</button>
        </form>
        <ul>
          <li v-for="r in repairs" :key="r.id">
            {{ r.toiletName }} - {{ r.faultDesc }} [{{ r.status }}]
            <select :value="r.status" @change="onUpdateStatus(r.id, $event.target.value)">
              <option>PENDING</option><option>REPAIRING</option><option>CHECKING</option><option>FINISHED</option>
            </select>
          </li>
        </ul>
      </div>
    </section>

    <section class="card">
      <h2>耗材库存预警</h2>
      <ul>
        <li v-for="c in consumables" :key="c.id" :class="{ warn: c.lowStock }">
          {{ c.toiletName }} - {{ c.consumableName }}：{{ c.currentStock }}/下限{{ c.minStock }}
        </li>
      </ul>
    </section>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { checkHealth, createRepair, createToilet, getConsumables, getDashboard, getRepairs, getToilets, updateRepairStatus } from '../api/feedback'

const dashboard = reactive({ toiletCount: 0, feedbackCount: 0, repairPendingCount: 0, lowStockCount: 0 })
const toilets = ref([]); const repairs = ref([]); const consumables = ref([])
const error = ref('')
const frontendPort = ref(window.location.port || '80')
const backendPort = ref('-')
const backendUp = ref(false)
const toiletForm = reactive({ toiletCode: '', name: '', address: '', district: '', openTime: '' })
const repairForm = reactive({ toiletId: '', faultDesc: '', reporter: '' })

async function load() {
  error.value = ''
  try {
    const health = await checkHealth()
    backendUp.value = health.status === 'UP'
    backendPort.value = health.serverPort
    Object.assign(dashboard, await getDashboard())
    toilets.value = await getToilets()
    repairs.value = await getRepairs()
    consumables.value = await getConsumables()
  } catch (e) { error.value = e.message }
}
async function onCreateToilet() { await createToilet({ ...toiletForm }); await load() }
async function onCreateRepair() { await createRepair({ ...repairForm }); await load() }
async function onUpdateStatus(id, status) { await updateRepairStatus(id, status); await load() }

onMounted(load)
</script>
