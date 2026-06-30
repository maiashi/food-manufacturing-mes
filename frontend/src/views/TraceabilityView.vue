<template>
  <div class="traceability">
    <h2>トレーサビリティ検索</h2>

    <!-- 追跡検索フォーム -->
    <el-card style="margin-bottom: 20px">
      <template #header>
        ロット追跡
      </template>
      <el-form :inline="true">
        <el-form-item label="ロット番号">
          <el-input
            v-model="searchLot"
            placeholder="LOT-xxxxxx-xxx"
          />
        </el-form-item>
        <el-form-item label="検索方向">
          <el-select v-model="traceDirection">
            <el-option
              label="前方追跡（原材料→製品）"
              value="forward"
            />
            <el-option
              label="後方追跡（製品→原材料）"
              value="backward"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="executeTrace"
          >
            検索
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 追跡結果ビジュアライゼーション -->
    <el-card v-if="traceResult">
      <template #header>
        {{ traceDirection === 'forward' ? '前方追跡' : '後方追跡' }}結果
      </template>
      <div class="trace-flow">
        <div
          v-for="(node, i) in traceResult"
          :key="i"
          class="trace-node"
        >
          <el-card
            class="node-card"
            :class="{ current: node.current }"
          >
            <h4>{{ node.label }}</h4>
            <p>{{ node.details }}</p>
            <el-tag
              v-if="node.ccp"
              type="danger"
            >
              CCP
            </el-tag>
            <span class="timestamp">{{ node.timestamp }}</span>
          </el-card>
          <div
            v-if="i < traceResult.length - 1"
            class="arrow"
          >
            ↓
          </div>
        </div>
      </div>
    </el-card>

    <!-- 関連記録 -->
    <el-card style="margin-top: 20px">
      <template #header>
        関連記録（全て）
      </template>
      <el-table
        :data="relatedRecords"
        stripe
      >
        <el-table-column
          prop="timestamp"
          label="時刻"
          width="160"
        />
        <el-table-column
          prop="eventType"
          label="イベント種別"
          width="130"
        />
        <el-table-column
          prop="lotNumber"
          label="ロットNo."
          width="150"
        />
        <el-table-column
          prop="description"
          label="内容"
          min-width="200"
        />
      </el-table>
    </el-card>

    <!-- リコール情報 -->
    <el-alert
      v-if="recallInfo"
      :title="`リコール: ${recallInfo.recallNumber}`"
      type="warning"
      show-icon
    >
      <p>{{ recallInfo.reason }}</p>
      <el-progress
        :percentage="Math.round((recallInfo.unitsRecalled / recallInfo.unitsAffected) * 100)"
        :status="recallInfo.status === 'COMPLETED' ? 'success' : ''"
      />
    </el-alert>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const searchLot = ref('')
const traceDirection = ref<'forward' | 'backward'>('forward')
const traceResult = ref<Record<string, unknown>[] | null>(null)
const relatedRecords = ref<Record<string, unknown>[]>([])
const recallInfo = ref<Record<string, unknown> | null>(null)

function executeTrace() {
  console.log('TRACE LOT:', searchLot.value, 'DIRECTION:', traceDirection.value)
  // TODO: API call
}
</script>

<style scoped>
.traceability { padding-bottom: 20px; }
.trace-flow { display: flex; flex-direction: column; gap: 8px; }
.node-card { margin-top: 12px; transition: all 0.3s; }
.node-card.current { border-color: #e6a23c; background: #fdf6ec; }
.arrow { text-align: center; color: #909399; font-size: 20px; }
.timestamp { float: right; font-size: 12px; color: #909399; margin-top: 8px; }
</style>
