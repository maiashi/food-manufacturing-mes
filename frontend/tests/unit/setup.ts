import { config } from '@vue/test-utils'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// Register all Element Plus icons for testing
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  // Icons are auto-registered via Vite plugin in production
}
