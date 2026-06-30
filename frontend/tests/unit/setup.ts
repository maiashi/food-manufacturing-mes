import { config } from '@vue/test-utils'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// Register all Element Plus icons for testing
config.global.plugins = [ElementPlus]
config.global.components = Object.fromEntries(Object.entries(ElementPlusIconsVue))
