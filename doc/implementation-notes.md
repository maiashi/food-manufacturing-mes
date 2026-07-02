# 実装ノート - 食品製造業MES

## 1. 作成日: 2026-06-26

## 2. 仕様に含まれていなかった決定事項

### 2.1 ロット状態のEnum定義
- 仕様: LOT-005「ロット状態：新規/検収済/生産使用済/廃棄済/出荷済」
- 実装: `LotStatus` Enumは `RECEIVED, RESERVED, IN_USE, COMPLETED, QUARANTINE, REJECTED`
- 決定事項: 実装のEnumを基準とし、仕様とのマッピングは以下の通りとする:
  - 新規 → RECEIVED（受入済）
  - 検収済 → RECEIVED（検収OK）
  - 生産使用済 → IN_USE または RESERVED
  - 廃棄済 → REJECTED または QUARANTINE
  - 出荷済 → COMPLETED

### 2.2 FIFO管理の優先順位
- 仕様: 4.2 優先順位 - 1.同一有効期限内: 製造日/受入日の早い順, 2.異なる有効期限: 有効期限の早い順
- 実装: `getAvailableStock` メソッドでは `manufacturingDate` によるソートのみ実装
- 決定事項: FIFOソートの優先順位を以下の通り実装する:
  1. 有効期限（expiryDate）の昇順
  2. 有効期限が同じ場合は、製造日（manufacturingDate）の昇順
  3. 両方ともnullの場合は、受入日（created_at）の昇順

### 2.3 カテゴリ別FIFO厳格度の実装
- 仕様: レトルト（低）、チルド総菜（高）、デリカ（最高）、おせち（中）
- 決定事項: カテゴリ別FIFO厳格度は以下の通り実装する:
  - レトルト: FIFO警告のみ、手動オーバーライドを許可
  - チルド総菜: 厳格なFIFO、手動変更は管理者権限のみ
  - デリカ: 最優先FIFO、FIFO違反検知時は即時アラート
  - おせち: 日付ベース優先（12月31日まで商習慣）

## 3. 変更しなければならなかったこと

### 3.1 InventoryService.javaのallocateStockメソッド
- 現状: FIFO順での在庫割り当ては実装されているが、ロット分割(SPL-001~SPL-005)の機能は実装されていない
- 変更内容: ロット分割機能を追加実装する (`splitLot`メソッド)

### 3.2 InventoryService.javaのreleaseAllocationメソッド
- 現状: コメントに「Find original received quantity via stock transaction history could be done here」とある
- 変更内容: 予約解除機能を完全実装する

### 3.3 LotStatus Enumの拡張
- 現状: `RECEIVED, RESERVED, IN_USE, COMPLETED, QUARANTINE, REJECTED`
- 変更内容: 仕様の「新規/検収済/生産使用済/廃棄済/出荷済」に合わせるか、実装のEnumを仕様として採用する

## 4. トレードオフ

### 4.1 ロット分割機能の実装
- オプションA: 分割時に新しいLotエンティティを作成し、親-子関係を保持
- オプションB: 分割履歴を別テーブルで管理し、Lotエンティティは更新しない
- 決定: オプションAを採用。新しいLotエンティティを作成し、`parentLotId`で親子関係を保持する

### 4.2 FIFOソートの優先順位
- オプションA: 有効期限優先、製造日次優先
- オプションB: 製造日優先、有効期限次優先
- 決定: 仕様通り有効期限優先、製造日次優先とする

### 4.3 カテゴリ別FIFO厳格度の管理
- オプションA: カテゴリ毎に異なるInventoryServiceを実装
- オプションB: 単一のInventoryServiceでカテゴリ情報をパラメータとして受け取り、処理を分岐
- 決定: オプションBを採用。カテゴリ情報をパラメータとして受け取り、処理を分岐する

## 5. 実装状況サマリー

### 5.1 在庫管理 (02_在庫管理.md)
- [x] INV-001/LOT-001: 物品コード・ロットIDの固有採番機能実装確認 - Lotエンティティに`lotId`, `lotNumber`実装
- [x] INV-005/LOT-004: 賞味期限タイプの実装状況確認 - `manufacturingDate`, `expiryDate`実装
- [x] INV-006/LOT-005: ロット状態のEnum実装確認 - `LotStatus` Enum実装（RECEIVED, RESERVED, IN_USE, COMPLETED, QUARANTINE, REJECTED）
- [x] STK-001: ロット別・保管場所別の現在高をリアルタイムで把握する機能の実装確認 - `Lot`エンティティに`warehouseId`, `quantity`実装
- [x] STK-002: 保管場所/物品別にFIFO順序で表示する機能の実装確認 - `getAvailableStock`メソッド実装（有効期限優先のソート実装済み）
- [x] STK-003: 有効期限間近警告（期限が○日以内のロットを自動通知）の実装 - 実装済み (`getExpiringLots`メソッド)
- [x] STK-006: 生産指示による原材料の予約管理（リザーブ）の実装確認 - `InventoryReservation`エンティティ実装、`allocateStock`でRESERVED状態へ遷移

### 5.2 ロット分割機能
- [x] SPL-001: 元ロットの一部数量を出荷/使用目的で分離する機能の実装 - 実装済み (`splitLot`メソッド)
- [x] SPL-002: 分割後のロットに新しいLot IDを発行する機能の実装 - 実装済み
- [x] SPL-003: 分割後ロットは元ロットと同じ有効期限を継承する機能の実装 - 実装済み
- [x] SPL-004: ロットツリーとして親子関係を保持する機能の実装 - `parentLotId`は実装済み
- [x] SPL-005: 分割時に保管場所を変更可能にする機能の実装 - 実装済み (`splitLot`メソッドの`newWarehouseId`パラメータ)

### 5.3 カテゴリ別賞味期限・FIFO要件
- [x] 4.1 賞味期限マスタ（カテゴリ別デフォルト）: レトルト/チルド総菜/デリカ/おせちの賞味日数・保管温度・FIFO優先度・警告閾値の実装 - 実装済み (`CategoryFifoConfig`エンティティ実装)
- [x] 4.2 カテゴリ別FIFO実行ルール: 各カテゴリに応じたFIFO厳格度の実装 - 実装済み (`validateFifoAllocation`, `shouldIssueFifoWarning`メソッド実装)
  - [x] レトルト食品：FIFO優先度低、同一原材料ロットによる多製品共用時の注意 - 実装済み
  - [x] チルド総菜：厳格なFIFO必須、手動変更は管理者権限のみ - 実装済み
  - [x] デリカ：最優先FIFO、製造→倉庫入库→出荷のサイクルを24時間以内に監視 - 実装済み
  - [x] おせち料理：通常期間(1月〜11月)基本生産なし、生産期間(12月1日〜20日)、出荷期間(12月21日〜30日) - 実装済み

### 5.4 廃棄管理
- [x] DSC-001: 賞味期限超過ロットを自動で「廃棄対象」に状態変更する機能の実装 - 実装済み (`markExpiredLotsAsRejected`メソッド)
- [ ] DSC-002: 廃棄の申請から管理者承認までのフローの実装 - 未実装
- [ ] DSC-003: 廃棄理由分類（期限切れ/品質不良/破損/その他）の実装 - 未実装
- [ ] DSC-004: 法令遵守のため全廃棄を記録（数量・日付・方法）の実装 - 未実装

### 5.5 保管場所管理
- [x] WH-001~WH-005: 倉庫マスタ管理の実装 - 実装済み (`Warehouse`エンティティ実装)
- [x] ZN-001~ZN-004: ゾーンマスタ管理の実装 - 実装済み (`Zone`エンティティ実装)
- [x] SH-001~SH-004: 棚・ラックマスタ管理の実装 - 実装済み (`Shelf`エンティティ実装)
- [x] LOC-001~LOC-004: 保管場所と物品の対応管理の実装 - 実装済み (Lotエンティティに`warehouseId`, `zoneId`, `shelfId`実装、温度帯検証も実装済み)
- [x] TRF-001~TRF-004: 保管場所移動（Transfer）の実装 - 実装済み (`LocationTransfer`エンティティ、`createLocationTransfer`, `approveLocationTransfer`, `executeLocationTransfer`, `cancelLocationTransfer`メソッド、`BarcodeScanRecord`エンティティ、RFID/バーコード対応メソッド実装)
- [x] MON-001~MON-003: 保管環境モニタリング連携の実装 - 実装済み (`TemperatureLog`エンティティ、`TemperatureAlert`エンティティ、温度検証・警告メソッド実装)

### 5.6 カテゴリ別保管場所要件
- [x] 6.1 温度区分ゾーン定義: 冷凍倉庫/冷蔵A庫/冷蔵B庫/冷蔵C庫/常温倉庫/前処理室のゾーン定義の実装 - 実装済み (`CategoryStorageConfig`エンティティ実装)
- [x] 6.2 カテゴリ別の保管場所フロー: レトルト/チルド総菜/デリカ/おせちの保管場所フローの実装 - 実装済み (`validateStorageLocationAssignment`, `validateWarehouseCategoryMatch`, `validateZoneCategoryMatch`, `validateShelfCategoryMatch`メソッド実装)
- [x] 6.3 保管場所割り当てルール: SKU毎に保管可能ゾーンを定義、入库時に自動判別、混在禁止ゾーン、おせち期間特殊ルール、FIFOによる保管位置案内の実装 - 実装済み (`getAvailableStorageLocationsForCategory`メソッド実装)

### 5.7 HACCP衛生管理
- [x] PR-001~PR-012: プリ・レquisites管理の実装 - 一部実装
- [x] HA-001~HA-004: 危害要因分析の実装 - 実装済み (`HazardAnalysis`エンティティ実装)
- [x] CCP-001~CCP-003: CCPの特定の実装 - 実装済み (`CcpMap`エンティティ実装)
- [x] CL-001~CL-003: クリティカルリミットの設定の実装 - 実装済み (`CriticalLimit`エンティティ実装)
- [x] MS-001~MS-004: 監視システムの設定の実装 - 実装済み (`CcpMonitoringRecord`エンティティ実装)
- [x] CA-001~CA-004: クリティカルリミット逸脱時の是正措置の実装 - 実装済み (`CorrectiveAction`エンティティ実装)
- [x] VA-001~VA-005: 検証手順の確立の実装 - 実装済み (`VerificationProcedure`エンティティ実装)
- [ ] RC-001~RC-005: 記録文書の維持の実装 - 一部実装

### 5.8 品質管理
- [x] QC-001~QC-004: 検査規格マスタの実装 - 実装済み (`InspectionSpec`エンティティ実装)
- [x] QP-001~QP-004: 検査実施行の実装 - 実装済み (`InspectionRecord`エンティティ実装)
- [ ] QR-001~QC-003: 出荷判断の実装 - 一部実装
- [x] NC-001~NC-004: 不適合品管理（NCR）の実装 - 実装済み (`NcrReport`エンティティ実装)
- [ ] QRT-001~QRT-003: 品質レポートの実装 - 未実装

### 5.9 製造実行
- [ ] BM-001~BM-004: BOM（部品表）管理の実装 - 未実装
- [ ] PF-001~PF-003: 工程フロー管理の実装 - 一部実装
- [ ] WI-001~WI-004: 生産指示書の実装 - 未実装
- [ ] WR-001~WR-004: 作業記録（生産実績）の実装 - 一部実装
- [ ] BT-001~BT-003: バッチトレーサビリティの実装 - 一部実装
- [ ] PR-001~PR-003: 生産実績レポートの実装 - 未実装

### 5.10 トレーサビリティ
- [ ] FT-001~FT-004: フォワードトレースの実装 - 一部実装
- [ ] BT-001~BT-003: バックトレースの実装 - 一部実装
- [ ] RC-001~RC-004: リコール管理の実装 - 未実装
- [ ] FSC-001~FSC-002: FSC対応の実装 - 未実装
- [ ] TR-001~TR-002: トレーサビリティレポートの実装 - 未実装

### 5.11 非機能要件
- [ ] PF-001~PF-009: パフォーマンス要件の実装 - 未実装
- [ ] AV-001~AV-005: 可用性・信頼性要件の実装 - 未実装
- [ ] SC-001~SC-005: セキュリティ要件の実装 - 未実装
- [ ] DP-010~DP-013: データ保護の実装 - 未実装
- [ ] AU-001~AU-004: 監査証跡の実装 - 一部実装

### 5.12 システムアーキテクチャ
- [ ] S-01~S-12: サービス層（マイクロサービス）設計の実装 - 一部実装
- [ ] 3.1~3.3: データベース設計方針の実装 - 一部実装
- [ ] 4.1~4.2: IF連携アーキテクチャの実装 - 未実装
- [ ] 8.1~8.2: 監査証跡・改ざん防止設計の実装 - 一部実装
