<template>
  <div class="quality">
    <div class="page-header">
      <h2>品質管理</h2>
      <el-button
        type="primary"
        @click="showNewInspection"
      >
        検査登録
      </el-button>
    </div>

    <!-- 検査結果一覧 -->
    <el-card>
      <template #header>
        検査実績
      </template>
      <el-table
        :data="results"
        stripe
      >
        <el-table-column
          prop="lotNumber"
          label="ロットNo."
          width="150"
        />
        <el-table-column
          prop="specName"
          label="規格名"
          min-width="120"
        />
        <el-table-column
          prop="specType"
          label="種別"
          width="80"
        >
          <template #default="{ row }">
            <el-tag :type="row.specType === 'incoming' ? 'success' : row.specType === 'in_process' ? 'warning' : 'info'">
              {{ row.specType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="inspectionDate"
          label="検査日"
          width="120"
        />
        <el-table-column
          prop="inspectorName"
          label="検査者"
          width="100"
        />
        <el-table-column
          label="結果"
          width="80"
        >
          <template #default="{ row }">
            <el-tag :type="row.overall === 'PASS' ? 'success' : 'danger'">
              {{ row.overall }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 不適合報告書（NCR） -->
    <h4 style="margin-top: 20px">
      不適合報告書 (NCR)
    </h4>
    <el-table
      :data="ncrs"
      stripe
    >
      <el-table-column
        prop="ncrNumber"
        label="NCR No."
        width="130"
      />
      <el-table-column
        prop="lotNumber"
        label="ロットNo."
        width="150"
      />
      <el-table-column
        prop="causeCategory"
        label="原因区分"
        width="120"
      />
      <el-table-column
        prop="description"
        label="内容"
        min-width="200"
      />
      <el-table-column
        prop="disposition"
        label="処置"
        width="130"
      >
        <template #default="{ row }">
          {{ row.disposition }}
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        label="状態"
        width="80"
      >
        <template #default="{ row }">
          <el-tag :type="row.status === 'RESOLVED' ? 'success' : 'warning'">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 規格管理 -->
    <h4 style="margin-top: 20px">
      規格一覧
    </h4>
    <el-table
      :data="specs"
      stripe
    >
      <el-table-column
        prop="specName"
        label="規格名"
        min-width="150"
      />
      <el-table-column
        prop="specType"
        label="種別"
        width="100"
      />
      <el-table-column
        prop="effectiveDate"
        label="適用日"
        width="120"
      />
      <el-table-column
        label="項目数"
        width="80"
      >
        <template #default="{ row }">
          {{ row.itemCount }} 項目
        </template>
      </el-table-column>
    </el-table>

    <!-- 新検査登録ダイアログ -->
    <el-dialog
      v-model="showNewInspection"
      title="新規検査登録"
      width="600px"
    >
      <el-form
        :model="inspectionForm"
        label-width="120px"
      >
        <el-form-item label="ロット">
          <el-select
            v-model="inspectionForm.lotId"
            filterable
          >
            <el-option
              v-for="l in lots"
              :key="l"
              :label="l"
              :value="l"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="規格">
          <el-select
            v-model="inspectionForm.specId"
            filterable
          >
            <el-option
              v-for="s in inspectionSpecs"
              :key="s.id"
              :label="s.name"
              :value="s.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showNewInspection = false">
          キャンセル
        </el-button>
        <el-button
          type="primary"
          @click="submitInspection"
        >
          登録
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const results = ref<any[]>([])
const ncrs = ref<any[]>([])
const specs = ref<any[]>([])
const showNewInspection = ref(false)
const inspectionForm = ref({ lotId: '', specId: '' })

const lots = ['LOT-20250101-001', 'LOT-20250101-002']
const inspectionSpecs = [
  { id: 'spc-001', name: '微生物検査（規格A）' },
  { id: 'spc-002', name: '重量管理（規格B）' }
]

function submitInspection() { console.log('SUBMIT INSPECTION', inspectionForm.value); showNewInspection.value = false }
</script>

<style scoped>
.quality { padding-bottom: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
</style>
