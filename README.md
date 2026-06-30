# 食品製造業MES - 生産管理システム

食品製造業向けの製造実行システム（MES）。HACCP衛生管理・FIFO在庫管理・トレーサビリティを網羅。

## 技術スタック

| 層 | 技術 |
|---|------|
| **フロントエンド** | Vue 3.5 + Vite 6 + TypeScript 5 + Element Plus + Node.js 24 |
| **バックエンド** | Java 21 + Spring Boot 3.4 + jOOQ 3.19 |
| **データベース** | PostgreSQL 17（factoryパーティション対応） |
| **IoTブローカー** | EMQX 5.8 (CE) |
| **メッセージキュー** | Apache Kafka 3.8 |
| **キャッシュ** | Redis 7 |
| **CI/CD** | GitHub Actions |

## プロジェクト構成

```
mes-system/
├── backend/                    # Spring Boot + jOOQ
│   ├── build.gradle.kts        # Gradleビルド設定（Shadow JAR対応）
│   └── gradle/wrapper/         # Gradle Wrapper 8.12
│   ├── src/main/java/com/mes/
│   │   ├── MesApplication.java # アプリケーション開始点
│   │   ├── config/             # 設定クラス（Security, jOOQ, Kafka, MQTT）
│   │   ├── controller/         # REST APIコントローラ
│   │   ├── service/            # ドメインロジック
│   │   ├── repository/         # jOOQ Generated DAO
│   │   └── model/              # jOOQ Generated Records
│   └── src/main/resources/
│       ├── application.yml     # 共通設定
│       ├── db/migration/       # Flywayマイグレーション
│       └── Dockerfile          # 本番イメージ
├── frontend/                   # Vue 3 + Vite + TypeScript
│   ├── package.json            # npm依存管理
│   ├── vite.config.ts          # ビルド設定（Element Plus自動インポート対応）
│   ├── src/
│   │   ├── main.ts             # エントリポイント
│   │   ├── App.vue             # ルートコンポーネント（ナビゲーション）
│   │   ├── router/             # Vue Router定義
│   │   ├── stores/             # Pinia状態管理
│   │   ├── services/           # APIクライアント
│   │   └── views/              # ページビューコンポーネント
│   ├── tests/                  # Vitestユニットテスト + Playwright E2E
│   └── Dockerfile.dev          # 開発用Dockerイメージ
├── infrastructure/             # インフラ設定
│   └── nginx/default.conf      # Nginxプロキシ設定
├── .github/workflows/ci.yml    # GitHub Actions CI/CD
├── docker-compose.yml          # ローカル開発環境（PG17, EMQX, Kafka, Redis）
└── docs/                       # 設計文書
```

## 起動方法

### ローカル開発環境（Docker Compose）

```bash
# 全てのサービスを一括起動
docker compose up --build -d

# サービスのステータス確認
docker compose ps

# ログ確認
docker compose logs -f backend   # バックエンドログ
docker compose logs -f frontend  # フロントエンドログ
docker compose logs -f emqx      # EMQXログ
```

### アクセス可能なURL

| サービス | URL | 備考 |
|---------|-----|------|
| フロントエンド | http://localhost:5173 | Vite開発サーバー |
| バックエンドAPI | http://localhost:8080/api/v1 | Swagger UI付き |
| EMQXダッシュボード | http://localhost:18083 | user: admin / pass: mes_emqx_admin |
| Nginxプロキシ | http://localhost:80 | フロント+API統合 |

### データベース初期化

```bash
# jOOQコード生成
cd backend && ./gradlew jooqCodegenMain

# コンパイル（jOOQ生成含む）
cd backend && ./gradlew compileJava
```

## テスト

```bash
# バックエンド
cd backend && ./gradlew test              # ユニットテスト
cd backend && ./gradlew integrationTest   # インテグレーションテスト(Testcontainers)

# Shadow JARビルド
cd backend && ./gradlew shadowJar

# フロントエンド
cd frontend && npm run test:unit    # Vitestユニットテスト
cd frontend && npm run test:e2e     # Playwright E2Eテスト
```

## 開発ガイドライン

### フロントエンド（Vue 3）

- Composition API + `<script setup>` を使用
- TypeScript strict mode有効
- Element Plusコンポーネントは自動インポート設定済み
- 状態管理はPinia + persistedstateプラグイン
- API通信はAxios（JWT認証インターセプター付き）

### バックエンド（Spring Boot）

- jOOQのみでDBアクセス（MyBatis併用なし）
- Flywayによるデータベースマイグレーション
- Factoryパーティショニング済みテーブルはfactory_idでフィルタ
- KafkaイベントはDomain Eventパターンで発行

### コード品質

```bash
# フロントエンド
cd frontend && npm run lint    # ESLint
cd frontend && npm run format  # Prettier

# バックエンド
cd backend && ./gradlew check    # SpotBugs + Checkstyle
```
