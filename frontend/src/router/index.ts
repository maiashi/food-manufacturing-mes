import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: () => import('@/views/DashboardView.vue'),
      meta: { title: 'ダッシュボード' },
    },
    {
      path: '/inventory',
      name: 'Inventory',
      component: () => import('@/views/InventoryView.vue'),
      meta: { title: '在庫管理' },
    },
    {
      path: '/production',
      name: 'Production',
      component: () => import('@/views/ProductionView.vue'),
      meta: { title: '製造実行' },
    },
    {
      path: '/haccp',
      name: 'HACCP',
      component: () => import('@/views/HaccpView.vue'),
      meta: { title: 'HACCP管理' },
    },
    {
      path: '/quality',
      name: 'Quality',
      component: () => import('@/views/QualityView.vue'),
      meta: { title: '品質管理' },
    },
    {
      path: '/traceability',
      name: 'Traceability',
      component: () => import('@/views/TraceabilityView.vue'),
      meta: { title: 'トレーサビリティ' },
    },
  ],
})

export default router
