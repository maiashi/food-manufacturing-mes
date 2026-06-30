<template>
  <div class="production">
    <div class="page-header">
      <h2>製造実行</h2>
      <el-button type="primary" @click="showCreateOrder = true">新発注</el-button>
    </div>

    <!-- 製造指示書一覧 -->
    <el-card>
      <template #header>製造指示書</template>
      <el-table :data="orders" stripe>
        <el-table-column prop="orderNumber" label="Order No." width="140" />
        <el-table-column prop="lineName" label="ライン" width="150" />
        <el-table-column prop="productName" label="製品" min-width="120" />
        <el-table-column label="計画数量" width="120">
          <template #default="{ row }">{{ row.plannedQty }} {{ row.unit }}</template>
        </el-table-column>
        <el-table-column label="実績数量" width="120">
          <template #default="{ row }">{{ row.actualQty ?? '---' }} {{ row.unit }}</template>
        </el-table-column>
        <el-table-column prop="plannedStart" label="計画開始" width="160" />
        <el-table-column prop="actualStart" label="実績開始" width="160" />
        <el-table-column prop="status" label="状態" width="100">
          <template #default="{ row }">
            <el-tag :type="orderStatusType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button v-if="row.status === 'PLANNED'" size="small" @click="startBatch(row)">
              開始
            </el-button>
            <el-button
              v-if="row.status === 'IN_PROGRESS'"
              size="small"
              type="success"
              @click="completeBatch(row)"
            >
              完了
            </el-button>
            <el-button
              v-if="row.status === 'PLANNED' || row.status === 'IN_PROGRESS'"
              size="small"
              type="warning"
              @click="pauseBatch(row)"
            >
              一時停止
            </el-button>
            <el-button size="small" @click="showOrderDetail(row)">詳細</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- バッチ情報 -->
    <el-card v-if="selectedBatch" style="margin-top: 16px">
      <template #header>バッチ詳細 - {{ selectedBatch.batchNumber }}</template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="工程">
          {{ selectedBatch.steps?.map((s: any) => s.name).join(', ') }}
        </el-descriptions-item>
        <el-descriptions-item label="投入量">
          {{ selectedBatch.inputQty }} {{ selectedBatch.unit }}
        </el-descriptions-item>
        <el-descriptions-item label="出力量">
          {{ selectedBatch.outputQty ?? '---' }} {{ selectedBatch.unit }}
        </el-descriptions-item>
        <el-descriptions-item label="歩留率">
          {{ selectedBatch.yieldRate ?? '---' }}%
        </el-descriptions-item>
      </el-descriptions>

      <!-- CCPモニタリング -->
      <h4 style="margin-top: 16px">CCP モニタリング</h4>
      <el-table :data="ccpRecords" stripe>
        <el-table-column prop="stepName" label="工程" width="150" />
        <el-table-column prop="parameter" label="パラメータ" width="120" />
        <el-table-column prop="measuredValue" label="測定値" width="120">
          <template #default="{ row }">{{ row.measuredValue }} {{ row.unit }}</template>
        </el-table-column>
        <el-table-column prop="minLimit" label="下限" width="80" />
        <el-table-column prop="maxLimit" label="上限" width="80" />
        <el-table-column prop="result" label="判定" width="80">
          <template #default="{ row }">
            <el-tag :type="row.result === 'OK' ? 'success' : 'danger'">
              {{ row.result }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 製造指示書作成ダイアログ -->
    <el-dialog v-model="showCreateOrder" title="新製造指示書" width="700px">
      <el-form :model="newOrderForm" label-width="120px">
        <el-form-item label="ライン">
          <el-select v-model="newOrderForm.lineId" placeholder="選択">
            <el-option v-for="l in lines" :key="l.lineId" :label="l.lineName" :value="l.lineId" />
          </el-select>
        </el-form-item>
        <el-form-item label="製品">
          <el-select v-model="newOrderForm.productId" filterable>
            <el-option
              v-for="p in products"
              :key="p.productId"
              :label="p.productName"
              :value="p.productId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="newOrderForm.plannedQty" />
        </el-form-item>
        <el-form-item label="計画開始">
          <el-date-picker
            v-model="newOrderForm.plannedStart"
            type="datetime"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateOrder = false">キャンセル</el-button>
        <el-button type="primary" @click="createProductionOrder">作成</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const orders = ref<any[]>([])
// TODO: API fetch
// const { data } = await api.get('/production-orders', { params: { factoryCode: selectedFactory.value } })

const lines = [
  { lineId: 'c0000001-0000-0000-0000-000000000001', lineName: '東京第1工場 - ミキシングラインA' },
  { lineId: 'c0000001-0000-0000-0000-000000000002', lineName: '東京第1工場 - 加熱ラインB' },
]

const products = [
  { productId: 'p-001', productName: '惣菜A (300g)' },
  { productId: 'p-002', productName: '惣菜B (500g)' },
]

const selectedBatch = ref<any>(null)
const ccpRecords = ref<any[]>([])

function orderStatusType(s: string): 'success' | 'warning' | 'danger' | 'primary' | 'info' {
  const map: Record<string, 'success' | 'warning' | 'danger' | 'primary' | 'info'> = {
    PLANNED: 'info',
    IN_PROGRESS: 'primary',
    COMPLETED: 'success',
    PAUSED: 'warning',
    CANCELLED: 'danger',
  }
  return map[s] || 'primary'
}

function startBatch(row: any) {
  console.log('START', row.orderNumber)
}
function completeBatch(row: any) {
  console.log('COMPLETE', row.orderNumber)
}
function pauseBatch(row: any) {
  console.log('PAUSE', row.orderNumber)
}
function showOrderDetail(row: any) {
  selectedBatch.value = row
  orders.value.forEach((o) => (o.selected = o === row))
}
const showCreateOrder = ref(false)
const newOrderForm = ref({ lineId: '', productId: '', plannedQty: 0, plannedStart: '' })

function createProductionOrder() {
  console.log('CREATE ORDER', newOrderForm.value)
  showCreateOrder.value = false
}
</script>

<style scoped>
.production {
  padding-bottom: 20px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
</style>
