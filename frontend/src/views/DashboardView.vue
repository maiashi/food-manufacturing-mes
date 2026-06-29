<template>
  <div class="dashboard">
    <h2>ダッシュボード</h2>
    <!-- TODO: 各工場の生産状況カード -->
    <el-row :gutter="20">
      <el-col :span="8" v-for="factory in factories" :key="factory.factoryId">
        <el-card class="factory-card">
          <template #header>
            <span>{{ factory.factoryName }}</span>
            <el-tag :type="factory.statusType">{{ factory.statusText }}</el-tag>
          </template>
          <div class="metric-grid">
            <div class="metric-item">
              <span class="label">Today's Production</span>
              <span class="value">{{ factory.todayProduction ?? '--' }}</span>
            </div>
            <div class="metric-item">
              <span class="label">Yield Rate</span>
              <span class="value">{{ factory.yieldRate ?? '--' }}%</span>
            </div>
            <div class="metric-item">
              <span class="label">Active Lines</span>
              <span class="value">{{ factory.activeLines ?? 0 }}/{{ factory.totalLines ?? 0 }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- TODO: CCPアラート一覧 -->
    <el-card class="alert-section">
      <template #header>HACCP CCP アラート</template>
      <el-table :data="alerts" stripe>
        <el-table-column prop="factoryName" label="工場" />
        <el-table-column prop="lineName" label="ライン" />
        <el-table-column prop="stepName" label="工程" />
        <el-table-column prop="alertTime" label="時刻" />
        <el-table-column prop="description" label="内容" />
      </el-table>
    </el-card>

    <!-- TODO: 在庫アラート -->
    <el-card class="stock-section">
      <template #header>在庫アラート</template>
      <el-table :data="stockAlerts" stripe>
        <el-table-column prop="factoryName" label="工場" />
        <el-table-column prop="lotNumber" label="ロット番号" />
        <el-table-column prop="materialName" label="品目" />
        <el-table-column prop="quantity" label="残量" />
        <el-table-column prop="expiryDate" label=" expiry" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface FactoryCard {
  factoryId: string
  factoryCode: string
  factoryName: string
  statusText: string
  statusType: 'success' | 'warning' | 'danger'
  todayProduction: number | null
  yieldRate: number | null
  activeLines: number | null
  totalLines: number
}

const factories = ref<FactoryCard[]>([])
// TODO: APIからfetchする
// const { data } = await api.get('/factories/dashboard')

interface AlertItem {
  id: string
  factoryName: string
  lineName: string
  stepName: string
  alertTime: string
  description: string
}

const alerts = ref<AlertItem[]>([])
const stockAlerts = ref<any[]>([])
</script>

<style scoped>
.dashboard {
  padding-bottom: 20px;
}
.factory-card {
  margin-bottom: 20px;
}
.metric-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-top: 12px;
}
.metric-item .label {
  font-size: 13px;
  color: #909399;
  display: block;
}
.metric-item .value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}
.alert-section,
.stock-section {
  margin-top: 20px;
}
</style>
