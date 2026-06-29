<template>
  <div class="haccp">
    <div class="page-header">
      <h2>HACCP管理</h2>
      <div class="actions">
        <el-select v-model="selectedFactory" placeholder="工場選択" style="width: 200px;">
          <el-option v-for="f in factories" :key="f.factoryId" :label="f.factoryName" :value="f.factoryCode" />
        </el-select>
        <el-button type="primary" @click="showAddPlan">計画追加</el-button>
      </div>
    </div>

    <!-- HACCP計画一覧 -->
    <el-tabs v-model="activeTab">
      <el-tab-pane label="HACCP計画" name="plans">
        <el-table :data="haccpPlans" stripe>
          <el-table-column prop="planVersion" label="版数" width="100" />
          <el-table-column prop="haccpType" label="タイプ" width="120">
            <template #default="{ row }">
              <el-tag :type="row.haccpType === 'HACCP_PLAN' ? 'danger' : 'warning'">{{ row.haccpType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="effectiveDate" label="適用日" width="120" />
          <el-table-column prop="expiryDate" label=" expiry日" width="120" />
          <el-table-column label="CCP数" width="80">
            <template #default="{ row }">{{ row.ccpCount ?? 0 }}</template>
          </el-table-column>
          <el-table-column label="状態" width="80">
            <template #default="{ row }">
              <el-tag :type="row.isActive ? 'success' : 'info'">{{ row.isActive ? '有効' : '無効' }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- CCP監視記録 -->
      <el-tab-pane label="CCP監視" name="ccp">
        <el-table :data="ccpRecords" stripe>
          <el-table-column prop="batchNumber" label="バッチNo." width="140" />
          <el-table-column prop="stepName" label="工程" width="150" />
          <el-table-column prop="parameter" label="パラメータ" width="120" />
          <el-table-column label="測定値" width="100">
            <template #default="{ row }">{{ row.measuredValue }} {{ row.unit }}</template>
          </el-table-column>
          <el-table-column label="規格" width="120">
            <template #default="{ row }">
              {{ row.minLimit }} ~ {{ row.maxLimit }}
            </template>
          </el-table-column>
          <el-table-column prop="result" label="判定" width="80">
            <template #default="{ row }">
              <el-tag :type="row.result === 'OK' ? 'success' : 'danger'">{{ row.result }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="monitoredAt" label="時刻" width="160" />
        </el-table>
      </el-tab-pane>

      <!-- CCP逸脱 -->
      <el-tab-pane label="逸脱管理" name="deviations">
        <el-table :data="deviations" stripe>
          <el-table-column prop="batchNumber" label="バッチNo." width="140" />
          <el-table-column prop="stepName" label="工程" width="150" />
          <el-table-column prop="description" label="逸脱内容" min-width="200" />
          <el-table-column label="是正処置" width="200">
            <template #default="{ row }">{{ row.correctiveAction }}</template>
          </el-table-column>
          <el-table-column label="対象数量" width="100">
            <template #default="{ row }">{{ row.affectedQuantity }} 個</template>
          </el-table-column>
          <el-table-column prop="status" label="状態" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 'RESOLVED' ? 'success' : 'warning'">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 要件プログラム -->
      <el-tab-pane label="要件プログラム" name="prp">
        <el-table :data="prpRecords" stripe>
          <el-table-column prop="programType" label="プログラム種別" width="150" />
          <el-table-column prop="executionDate" label="実施日" width="120" />
          <el-table-column prop="result" label="結果" width="80">
            <template #default="{ row }"><el-tag :type="row.result === 'OK' ? 'success' : 'danger'">{{ row.result }}</el-tag></template>
          </el-table-column>
          <el-table-column prop="details" label="詳細" min-width="200" />
        </el-table>
      </el-tab-pane>

      <!-- 内部監査 -->
      <el-tab-pane label="内部監査" name="audit">
        <el-table :data="audits" stripe>
          <el-table-column prop="auditType" label="監査種別" width="120" />
          <el-table-column prop="scheduledDate" label="予定日" width="120" />
          <el-table-column prop="actualDate" label="実施日" width="120" />
          <el-table-column prop="finding" label="所見" min-width="200" />
          <el-table-column prop="status" label="状態" width="80">
            <template #default="{ row }"><el-tag :type="row.status === 'CLOSED' ? 'success' : 'warning'">{{ row.status }}</el-tag></template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const factories = [
  { factoryId: 'b0000000-0000-0000-0000-000000000001', factoryCode: 'FTY-001', factoryName: '東京第1工場' },
  { factoryId: 'b0000000-0000-0000-0000-000000000002', factoryCode: 'FTY-002', factoryName: '大阪製造センター' },
  { factoryId: 'b0000000-0000-0000-0000-000000000003', factoryCode: 'FTY-003', factoryName: '名古屋生産工場' }
]

const selectedFactory = ref<string>('FTY-001')
const activeTab = ref('plans')
const showAddPlan = ref(false)

const haccpPlans = ref<any[]>([])
// TODO: API fetch

const ccpRecords = ref<any[]>([])
const deviations = ref<any[]>([])
const prpRecords = ref<any[]>([])
const audits = ref<any[]>([])
</script>

<style scoped>
.haccp { padding-bottom: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.actions { display: flex; gap: 12px; }
</style>
