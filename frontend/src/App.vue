<template>
  <el-container class="app-container">
    <el-header height="60px" class="app-header">
      <div class="header-content">
        <h1 class="logo">Food MES</h1>
        <el-menu
          :default-active="$route.path"
          mode="horizontal"
          router
          class="main-nav"
        >
          <el-menu-item index="/dashboard">ダッシュボード</el-menu-item>
          <el-menu-item index="/inventory">在庫管理</el-menu-item>
          <el-menu-item index="/production">製造実行</el-menu-item>
          <el-menu-item index="/haccp">HACCP管理</el-menu-item>
          <el-menu-item index="/quality">品質管理</el-menu-item>
          <el-menu-item index="/traceability">トレーサビリティ</el-menu-item>
        </el-menu>
      </div>
    </el-header>
    <el-main class="app-main">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
// Root component - navigation handled by el-menu
</script>

<style>
:root {
  --mes-primary: #409eff;
  --mes-success: #67c23a;
  --mes-warning: #e6a23c;
  --mes-danger: #f56c6c;
}

body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.app-container {
  height: 100vh;
}

.app-header {
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0 20px;
  display: flex;
  align-items: center;
}

.header-content {
  display: flex;
  align-items: center;
  width: 100%;
}

.logo {
  margin: 0 20px 0 0;
  font-size: 20px;
  color: var(--mes-primary);
  white-space: nowrap;
}

.main-nav {
  flex: 1;
}

.app-main {
  background: #f5f7fa;
  padding: 20px;
}
</style>
