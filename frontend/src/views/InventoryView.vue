<template>
  <div class="inventory">
    <div class="page-header">
      <h2>在庫管理</h2>
      <div class="actions">
        <el-select
          v-model="selectedFactory"
          placeholder="工場選択"
          style="width: 200px;"
        >
          <el-option
            v-for="f in factories"
            :key="f.factoryId"
            :label="f.factoryName"
            :value="f.factoryCode"
          />
        </el-select>
        <el-button
          type="success"
          @click="showReceiveDialog = true"
        >
          受入登録
        </el-button>
      </div>
    </div>

    <!-- 在庫ツリー：工場 → 倉庫 → ゾーン →棚 → Bin -->
    <el-card class="location-tree">
      <template #header>
        保管場所別在庫
      </template>
      <el-table
        :data="inventoryList"
        stripe
        row-key="lotId"
        style="width: 100%"
      >
        <el-table-column
          prop="locationPath"
          label="保管場所"
          width="280"
        />
        <el-table-column
          prop="materialName"
          label="品目"
          min-width="150"
        />
        <el-table-column
          prop="lotNumber"
          label="ロット番号"
          width="160"
        />
        <el-table-column
          prop="quantity"
          label="残量"
          width="120"
          sortable
        >
          <template #default="{ row }">
            {{ row.quantity }} {{ row.unit }}
          </template>
        </el-table-column>
        <el-table-column
          prop="expiryDate"
          label="expiry日"
          width="120"
          sortable
        >
          <template #default="{ row }">
            <el-tag :type="isExpiringSoon(row.expiryDate) ? 'warning' : ''">
              {{ formatDate(row.expiryDate) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状態"
          width="100"
        >
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="120"
        >
          <template #default="{ row }">
            <el-button
              size="small"
              @click="showSplitDialog(row)"
            >
              分割
            </el-button>
            <el-button
              size="small"
              type="danger"
              @click="showAdjustDialog(row)"
            >
              調整
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- FIFOコンプライアンス表示 -->
    <el-alert
      v-if="fifoViolations.length > 0"
      :title="`FIFO違反: ${fifoViolations.length}件`"
      type="error"
      :closable="false"
      show-icon
      style="margin-top: 16px"
    />

    <!-- ロット分割ダイアログ -->
    <el-dialog
      v-model="showSplitDialog"
      title="ロット分割"
      width="500px"
    >
      <el-form
        :model="splitForm"
        label-width="120px"
      >
        <el-form-item label="対象ロット">
          <span>{{ currentLot?.lotNumber }}</span>
        </el-form-item>
        <el-form-item label="分割数">
          <el-input-number
            v-model="splitForm.count"
            :min="2"
            :max="10"
          />
        </el-form-item>
        <el-form-item label="各ロット数量">
          <el-input-number
            v-model="splitForm.eachQty"
            :precision="4"
            :step="1"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSplitDialog = false">
          キャンセル
        </el-button>
        <el-button
          type="primary"
          @click="executeLotSplit"
        >
          分割実行
        </el-button>
      </template>
    </el-dialog>

    <!-- 受入登録ダイアログ -->
    <el-dialog
      v-model="showReceiveDialog"
      title="受入登録"
      width="600px"
    >
      <el-form
        :model="receiveForm"
        label-width="120px"
      >
        <el-form-item label="Purchase Order No.">
          <el-input v-model="receiveForm.poNumber" />
        </el-form-item>
        <el-form-item label="品目">
          <el-select
            v-model="receiveForm.materialId"
            filterable
          >
            <el-option
              v-for="m in materials"
              :key="m.materialId"
              :label="m.materialName"
              :value="m.materialId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number
            v-model="receiveForm.quantity"
            :precision="4"
            :step="1"
          />
        </el-form-item>
        <el-form-item label="expiry日">
          <el-date-picker
            v-model="receiveForm.expiryDate"
            type="date"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showReceiveDialog = false">
          キャンセル
        </el-button>
        <el-button
          type="primary"
          @click="executeReceipt"
        >
          受入実行
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
// import dayjs from 'dayjs'

const factories = [
  { factoryId: 'b0000000-0000-0000-0000-000000000001', factoryCode: 'FTY-001', factoryName: '東京第1工場' },
  { factoryId: 'b0000000-0000-0000-0000-000000000002', factoryCode: 'FTY-002', factoryName: '大阪製造センター' },
  { factoryId: 'b0000000-0000-0000-0000-000000000003', factoryCode: 'FTY-003', factoryName: '名古屋生産工場' }
]

const selectedFactory = ref<string>('FTY-001')

interface InventoryItem {
  lotId: string
  locationPath: string // "Warehouse A -> Zone 1 -> Shelf 2 -> Bin 3"
  materialName: string
  lotNumber: string
  quantity: number
  unit: string
  expiryDate: string
  status: string
}

const inventoryList = ref<InventoryItem[]>([])
// TODO: API fetch
// const { data } = await api.get('/inventories', { params: { factoryCode: selectedFactory.value } })

const fifoViolations = ref<any[]>([]) // TODO: FIFO違反チェック

function formatDate(date: string) {
  return date ? new Date(date).toLocaleDateString('ja-JP') : '--'
}

function isExpiringSoon(date: string): boolean {
  // TODO: 30日以内のexpiryをチェック
  return false
}

function statusType(s: string): 'success' | 'warning' | 'danger' | '' {
  const map: Record<string, 'success' | 'warning' | 'danger' | ''> = {
    ACTIVE: 'success',
    QUARANTINE: 'warning',
    REJECTED: 'danger',
    EXPIRED: 'danger'
  }
  return map[s] || ''
}

// ロット分割
const showSplitDialog = ref(false)
const currentLot = ref<InventoryItem | null>(null)
const splitForm = ref({ count: 2, eachQty: 0 })

function showSplitDialog(item: InventoryItem) {
  currentLot.value = item
  showSplitDialog.value = true
}

function executeLotSplit() {
  // TODO: API呼び出し
  // await api.post(`/inventories/${currentLot.value!.lotId}/split`, splitForm.value)
  console.log('LOT SPLIT', currentLot.value, splitForm.value)
  showSplitDialog.value = false
}

// 受入登録
const showReceiveDialog = ref(false)
const materials = [
  { materialId: 'mat-001', materialName: '小麦粉（中力）' },
  { materialId: 'mat-002', materialName: '砂糖' },
  { materialId: 'mat-003', materialName: '牛乳' }
]
const receiveForm = ref({ poNumber: '', materialId: '', quantity: 0, expiryDate: '' })

function executeReceipt() {
  // TODO: API呼び出し
  console.log('RECEIVE', receiveForm.value)
  showReceiveDialog.value = false
}
</script>

<style scoped>
.inventory { padding-bottom: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.actions { display: flex; gap: 12px; }
.location-tree { margin-top: 12px; }
</style>
