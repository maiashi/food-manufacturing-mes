import pluginVue from 'eslint-plugin-vue'
import tseslint from 'typescript-eslint'
import vueParser from 'vue-eslint-parser'

export default tseslint.config(
  // 1. 完全に除外するディレクトリ
  {
    ignores: [
      '**/node_modules/**',
      '**/dist/**',
      '**/vendor/**',
      '**/*.d.ts',
    ],
  },

  // 2. JavaScript / TypeScript / Vue ファイル共通の基本設定
  {
    files: ['**/*.js', '**/*.ts', '**/*.vue'],
    extends: [
      ...tseslint.configs.recommended, // TypeScriptの推奨ルール
      ...pluginVue.configs['flat/recommended'], // Vue3の推奨ルール（最も厳格）
    ],
    languageOptions: {
      ecmaVersion: 'latest',
      sourceType: 'module',
      parser: vueParser, // Vueファイルを正しくパースするための設定
      parserOptions: {
        parser: tseslint.parser, // Vue内のScript部やTSファイルを解析
        extraFileExtensions: ['.vue'],
      },
    },
    rules: {
      // プロジェクトに応じたカスタムルール
      'vue/multi-word-component-names': 'off', // Element Plus等で単一単語コンポーネントを許容するため
      '@typescript-eslint/no-explicit-any': 'warn', // any型は警告（必要に応じてoff）
      'vue/no-v-html': 'off', // Element Plusのコンポーネントでhtmlを扱う場合があるため
    },
  },

  // 3. Element Plusの自動インポート（unplugin-auto-import）を使用している場合の設定
  // ※ `auto-imports.d.ts` 等が生成されている場合は、TS側で解決されるため追加ルールは不要です。
)

