-- ============================================================
-- V1: 食品製造業MES - 初期スキーマ
-- PostgreSQL 17 / factory_idパーティション対応
-- ============================================================

-- ---- ユーザー・権限 ----
CREATE TABLE role (
    role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

INSERT INTO role (role_id, role_name, description) VALUES
    ('a0000000-0000-0000-0000-000000000001', 'ADMIN', 'システム管理者'),
    ('a0000000-0000-0000-0000-000000000002', 'FACTORY_MANAGER', '工場長'),
    ('a0000000-0000-0000-0000-000000000003', 'PRODUCTION_OPERATOR', '製造オペレーター'),
    ('a0000000-0000-0000-0000-000000000004', 'QUALITY_INSPECTOR', '品質検査員'),
    ('a0000000-0000-0000-0000-000000000005', 'WAREHOUSE_OPERATOR', '倉庫オペレーター');

CREATE TABLE factory (
    factory_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_code VARCHAR(20) NOT NULL UNIQUE,
    factory_name VARCHAR(200) NOT NULL,
    address TEXT,
    contact_email VARCHAR(255),
    phone VARCHAR(30),
    operating_hours VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 初期3工場データ
INSERT INTO factory (factory_id, factory_code, factory_name) VALUES
    ('b0000000-0000-0000-0000-000000000001', 'FTY-001', '東京第1工場'),
    ('b0000000-0000-0000-0000-000000000002', 'FTY-002', '大阪製造センター'),
    ('b0000000-0000-0000-0000-000000000003', 'FTY-003', '名古屋生産工場');

CREATE TABLE "mes_user" (

    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    login_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE user_role (
    user_role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES "mes_user"(user_id) ON DELETE CASCADE,
    role_id UUID REFERENCES role(role_id) ON DELETE CASCADE
);

-- ---- マスタ管理 ----
CREATE TABLE product_master (
    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sku_code VARCHAR(50) NOT NULL UNIQUE,
    product_name VARCHAR(200) NOT NULL,
    category VARCHAR(100),
    storage_condition VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE material_master (
    material_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sku_code VARCHAR(50) NOT NULL UNIQUE,
    material_name VARCHAR(200) NOT NULL,
    category VARCHAR(100),
    is_allergen BOOLEAN DEFAULT FALSE,
    allergen_types JSONB,
    storage_condition VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE supplier (
    supplier_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    supplier_code VARCHAR(50) NOT NULL UNIQUE,
    supplier_name VARCHAR(200) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(30),
    address TEXT,
    certification_expiry DATE,
    is_approved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE product_category (
    category_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    sku_code VARCHAR(50) NOT NULL,
    category_name VARCHAR(100),
    unit VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE product_spec (
    spec_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    sku_code VARCHAR(50) NOT NULL,
    min_weight_g DECIMAL(10,2),
    max_weight_g DECIMAL(10,2),
    shelf_life_days INTEGER,
    storage_temp_min_c DECIMAL(5,2),
    storage_temp_max_c DECIMAL(5,2),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ---- 工場・ライン階層 ----
CREATE TABLE production_line (
    line_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id) ON DELETE CASCADE,
    line_code VARCHAR(20) NOT NULL,
    line_name VARCHAR(200) NOT NULL,
    line_type VARCHAR(50) NOT NULL,
    max_throughput_per_hour INTEGER,
    capabilities JSONB,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(factory_id, line_code)
);

-- 各工場の初期ライン（例）
INSERT INTO production_line (line_id, factory_id, line_code, line_name, line_type) VALUES
    ('c0000001-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'LN-01', 'ミキシングラインA', 'mixing'),
    ('c0000001-0000-0000-0000-000000000002', 'b0000000-0000-0000-0000-000000000001', 'LN-02', '加熱ラインB', 'heating'),
    ('c0000001-0000-0000-0000-000000000003', 'b0000000-0000-0000-0000-000000000001', 'LN-03', '包装ラインC', 'packaging'),
    ('c0000002-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000002', 'LN-01', 'ミキシングラインA', 'mixing'),
    ('c0000002-0000-0000-0000-000000000002', 'b0000000-0000-0000-0000-000000000002', 'LN-02', '充填ラインB', 'filling'),
    ('c0000003-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000003', 'LN-01', '調理ラインA', 'cooking'),
    ('c0000003-0000-0000-0000-000000000002', 'b0000000-0000-0000-0000-000000000003', 'LN-02', '殺菌ラインB', 'sterilization');

CREATE TABLE line_shift (
    shift_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    line_id UUID REFERENCES production_line(line_id) ON DELETE CASCADE,
    shift_name VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    max_batch_capacity INTEGER,
    operator_requirements JSONB
);

-- ---- IoTセンサー ----
CREATE TABLE iot_sensor (
    sensor_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sensor_code VARCHAR(50) NOT NULL UNIQUE,
    factory_id UUID REFERENCES factory(factory_id) ON DELETE CASCADE,
    line_id UUID REFERENCES production_line(line_id) ON DELETE SET NULL,
    sensor_type VARCHAR(50) NOT NULL,
    protocol VARCHAR(20) NOT NULL DEFAULT 'mqtt',
    configuration JSONB,
    is_active BOOLEAN DEFAULT TRUE,
    last_calibration_date DATE,
    next_calibration_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE sensor_record (
    record_id BIGSERIAL PRIMARY KEY,
    sensor_id UUID REFERENCES iot_sensor(sensor_id) ON DELETE CASCADE,
    factory_id UUID REFERENCES factory(factory_id),
    line_id UUID REFERENCES production_line(line_id),
    recorded_at TIMESTAMP NOT NULL DEFAULT NOW(),
    value DOUBLE PRECISION NOT NULL,
    unit VARCHAR(20),
    status VARCHAR(10) DEFAULT 'OK',
    metadata JSONB
);

-- ---- 保管場所（工場階層対応） ----
CREATE TABLE warehouse (
    warehouse_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id) ON DELETE CASCADE,
    wh_code VARCHAR(20) NOT NULL UNIQUE,
    wh_name VARCHAR(200),
    temperature_zone VARCHAR(50),
    total_volume DECIMAL(12,2),
    total_weight_capacity_kg DECIMAL(12,2),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE zone (
    zone_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    warehouse_id UUID REFERENCES warehouse(warehouse_id) ON DELETE CASCADE,
    zone_code VARCHAR(20) NOT NULL,
    zone_name VARCHAR(200),
    temperature_override DECIMAL(5,2)
);

CREATE TABLE shelf (
    shelf_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_id UUID REFERENCES zone(zone_id) ON DELETE CASCADE,
    shelf_code VARCHAR(20) NOT NULL,
    weight_limit_kg DECIMAL(10,2),
    volume_limit_l DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE bin (
    bin_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shelf_id UUID REFERENCES shelf(shelf_id) ON DELETE CASCADE,
    bin_code VARCHAR(20) NOT NULL,
    weight_capacity_kg DECIMAL(10,2),
    is_occupied BOOLEAN DEFAULT FALSE
);

-- ---- 在庫・ロット（factoryパーティション対象） ----
CREATE TABLE lot (
    lot_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    lot_number VARCHAR(100) NOT NULL,
    material_id UUID REFERENCES material_master(material_id),
    parent_lot_id UUID REFERENCES lot(lot_id),
    manufacturing_date DATE,
    expiry_date DATE,
    quantity DECIMAL(15,4),
    unit VARCHAR(20) DEFAULT '個',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    supplier_id UUID REFERENCES supplier(supplier_id),
    received_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(factory_id, lot_number)
);

-- lotテーブルのfactoryパーティション分割
CREATE TABLE lot_partition (
    lot_id UUID DEFAULT gen_random_uuid(),
    factory_id VARCHAR(20) NOT NULL,
    lot_number VARCHAR(100) NOT NULL,
    material_id UUID REFERENCES material_master(material_id),
    parent_lot_id UUID REFERENCES lot(lot_id),
    manufacturing_date DATE,
    expiry_date DATE,
    quantity DECIMAL(15,4) DEFAULT 0,
    unit VARCHAR(20) DEFAULT '個',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    supplier_id UUID REFERENCES supplier(supplier_id),
    received_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(factory_id, lot_number),
    PRIMARY KEY (lot_id, factory_id)
) PARTITION BY LIST (factory_id);

CREATE TABLE lot_factory_001 PARTITION OF lot_partition FOR VALUES IN ('FTY-001');
CREATE TABLE lot_factory_002 PARTITION OF lot_partition FOR VALUES IN ('FTY-002');
CREATE TABLE lot_factory_003 PARTITION OF lot_partition FOR VALUES IN ('FTY-003');

CREATE TABLE lot_location (
    lot_location_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lot_id UUID REFERENCES lot(lot_id),
    bin_id UUID REFERENCES bin(bin_id),
    factory_id VARCHAR(20) NOT NULL,
    current_qty DECIMAL(15,4),
    moved_in_date TIMESTAMP NOT NULL DEFAULT NOW(),
    moved_out_date TIMESTAMP
);

-- ---- 受入（Incoming） ----
CREATE TABLE purchase_order (
    po_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    po_number VARCHAR(50) NOT NULL UNIQUE,
    supplier_id UUID REFERENCES supplier(supplier_id),
    order_date DATE NOT NULL,
    expected_delivery_date DATE,
    status VARCHAR(20) DEFAULT 'PENDING'
);

CREATE TABLE po_item (
    po_item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    po_id UUID REFERENCES purchase_order(po_id) ON DELETE CASCADE,
    material_id UUID REFERENCES material_master(material_id),
    ordered_qty DECIMAL(15,4) NOT NULL,
    unit VARCHAR(20) NOT NULL
);

CREATE TABLE receipt (
    receipt_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    po_id UUID REFERENCES purchase_order(po_id),
    receipt_date TIMESTAMP NOT NULL DEFAULT NOW(),
    inspection_result VARCHAR(20),
    received_by_user_id UUID REFERENCES "mes_user"(user_id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE receipt_item (
    receipt_item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    receipt_id UUID REFERENCES receipt(receipt_id) ON DELETE CASCADE,
    material_id UUID REFERENCES material_master(material_id),
    received_qty DECIMAL(15,4),
    lot_id UUID REFERENCES lot(lot_id),
    inspection_passed BOOLEAN,
    coa_file_url TEXT
);

-- ---- 製造実行 ----
CREATE TABLE production_order (
    production_order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    line_id UUID REFERENCES production_line(line_id),
    order_number VARCHAR(50) NOT NULL UNIQUE,
    product_id UUID REFERENCES product_master(product_id),
    warehouse_id UUID REFERENCES warehouse(warehouse_id),
    planned_start_date TIMESTAMP,
    actual_start_date TIMESTAMP,
    planned_end_date TIMESTAMP,
    actual_end_date TIMESTAMP,
    planned_qty DECIMAL(15,4),
    actual_qty DECIMAL(15,4),
    status VARCHAR(20) DEFAULT 'PLANNED',
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE production_order_item (
    po_item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    production_order_id UUID REFERENCES production_order(production_order_id) ON DELETE CASCADE,
    material_id UUID REFERENCES material_master(material_id),
    source_lot_id UUID REFERENCES lot(lot_id),
    allocated_qty DECIMAL(15,4),
    used_qty DECIMAL(15,4)
);

CREATE TABLE production_batch (
    batch_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    line_id UUID REFERENCES production_line(line_id),
    batch_number VARCHAR(50) NOT NULL UNIQUE,
    production_order_id UUID REFERENCES production_order(production_order_id),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    input_qty DECIMAL(15,4),
    output_qty DECIMAL(15,4),
    yield_rate DECIMAL(5,2),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE batch_lot (
    batch_lot_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    lot_number VARCHAR(100) NOT NULL UNIQUE,
    batch_id UUID REFERENCES production_batch(batch_id),
    product_id UUID REFERENCES product_master(product_id),
    manufacturing_date DATE,
    expiry_date DATE,
    quantity DECIMAL(15,4),
    status VARCHAR(20) DEFAULT 'COMPLETED'
);

-- ---- 工程フロー & HACCP ----
CREATE TABLE process_flow (
    process_flow_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    product_id UUID REFERENCES product_master(product_id),
    flow_name VARCHAR(200) NOT NULL,
    version DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    effective_date DATE NOT NULL
);

CREATE TABLE process_step (
    step_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    process_flow_id UUID REFERENCES process_flow(process_flow_id) ON DELETE CASCADE,
    step_order INTEGER NOT NULL,
    step_name VARCHAR(200) NOT NULL,
    step_type VARCHAR(50),
    is_ccp BOOLEAN DEFAULT FALSE,
    is_oprp BOOLEAN DEFAULT FALSE,
    ccp_parameters JSONB
);

CREATE TABLE haccp_plan (
    haccp_plan_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    warehouse_id UUID REFERENCES warehouse(warehouse_id),
    plan_version VARCHAR(20) NOT NULL,
    effective_date DATE NOT NULL,
    expiry_date DATE,
    haccp_type VARCHAR(50) NOT NULL, -- OPRP / HACCP_PLAN
    is_active BOOLEAN DEFAULT TRUE
);

-- ---- CCP監視 & 逸脱管理 ----
CREATE TABLE ccp_monitoring_record (
    record_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    line_id UUID REFERENCES production_line(line_id),
    step_id UUID REFERENCES process_step(step_id),
    batch_id UUID REFERENCES production_batch(batch_id),
    monitoring_date TIMESTAMP NOT NULL DEFAULT NOW(),
    measured_values JSONB,
    result VARCHAR(10) CHECK (result IN ('OK', 'NG')),
    monitored_by_user_id UUID REFERENCES "mes_user"(user_id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE ccp_deviation (
    deviation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    record_id UUID REFERENCES ccp_monitoring_record(record_id),
    detected_at TIMESTAMP NOT NULL DEFAULT NOW(),
    deviation_description TEXT,
    corrective_action_taken TEXT,
    affected_quantity DECIMAL(15,4),
    resolved_at TIMESTAMP,
    resolved_by_user_id UUID REFERENCES "mes_user"(user_id)
);

-- ---- 品質管理 ----
CREATE TABLE inspection_spec (
    spec_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    material_id UUID REFERENCES material_master(material_id),
    product_id UUID REFERENCES product_master(product_id),
    spec_type VARCHAR(20) NOT NULL CHECK (spec_type IN ('incoming', 'in_process', 'final')),
    effective_date DATE NOT NULL
);

CREATE TABLE spec_item (
    spec_item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    spec_id UUID REFERENCES inspection_spec(spec_id) ON DELETE CASCADE,
    item_name VARCHAR(200) NOT NULL,
    measurement_unit VARCHAR(50),
    min_limit DECIMAL(15,4),
    max_limit DECIMAL(15,4),
    target_value VARCHAR(200)
);

CREATE TABLE inspection_result (
    result_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lot_id UUID REFERENCES lot(lot_id),
    spec_id UUID REFERENCES inspection_spec(spec_id),
    inspection_date TIMESTAMP NOT NULL DEFAULT NOW(),
    inspector_user_id UUID REFERENCES "mes_user"(user_id)
);

CREATE TABLE result_item (
    item_result_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    result_id UUID REFERENCES inspection_result(result_id) ON DELETE CASCADE,
    spec_item_id UUID REFERENCES spec_item(spec_item_id),
    measured_value DECIMAL(15,4),
    judgment VARCHAR(10) CHECK (judgment IN ('OK', 'NG')),
    note TEXT
);

CREATE TABLE ncr (
    ncr_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    ncr_number VARCHAR(50) NOT NULL UNIQUE,
    lot_id UUID REFERENCES lot(lot_id),
    cause_category VARCHAR(50),
    description TEXT,
    reported_date TIMESTAMP NOT NULL DEFAULT NOW(),
    reported_by_user_id UUID REFERENCES "mes_user"(user_id),
    disposition VARCHAR(50), -- scrap, rework, return_to_supplier
    resolved_date TIMESTAMP
);

-- ---- トレーサビリティ・出荷 ----
CREATE TABLE shipment_order (
    shipment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    shipment_number VARCHAR(50) NOT NULL UNIQUE,
    customer_name VARCHAR(200) NOT NULL,
    delivery_address TEXT,
    shipment_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE shipment_item (
    item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    shipment_id UUID REFERENCES shipment_order(shipment_id) ON DELETE CASCADE,
    batch_lot_id UUID REFERENCES batch_lot(batch_lot_id),
    quantity DECIMAL(15,4),
    shipped_date TIMESTAMP
);

CREATE TABLE product_recall (
    recall_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recall_number VARCHAR(50) NOT NULL UNIQUE,
    recall_reason TEXT,
    affected_product_id UUID REFERENCES product_master(product_id),
    announcement_date TIMESTAMP NOT NULL DEFAULT NOW(),
    target_expiry_start DATE,
    target_expiry_end DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    total_units_affected DECIMAL(15,4),
    total_units_recalled DECIMAL(15,4)
);

-- ---- HACCP関連マスタ ----
CREATE TABLE hazard_analysis (
    hazard_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    haccp_plan_id UUID REFERENCES haccp_plan(haccp_plan_id),
    step_id UUID REFERENCES process_step(step_id),
    hazard_type VARCHAR(10) CHECK (hazard_type IN ('bio', 'chem', 'phys')),
    hazard_description TEXT,
    severity_score INTEGER,
    probability_score INTEGER,
    is_acceptable BOOLEAN DEFAULT FALSE
);

CREATE TABLE pre_requisite_program (
    prp_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    program_type VARCHAR(50) NOT NULL, -- cleaning, sanitation, pest_control, etc.
    description TEXT,
    frequency_days INTEGER,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE prp_execution_record (
    record_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prp_id UUID REFERENCES pre_requisite_program(prp_id),
    execution_date TIMESTAMP NOT NULL DEFAULT NOW(),
    result VARCHAR(10),
    details JSONB,
    executed_by_user_id UUID REFERENCES "mes_user"(user_id)
);

CREATE TABLE equipment_calibration (
    cal_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    step_id UUID REFERENCES process_step(step_id),
    calibration_due_date DATE,
    last_calibrated_date DATE,
    next_calibration_date DATE,
    calibration_result VARCHAR(10),
    calibrated_by_user_id UUID REFERENCES "mes_user"(user_id)
);

CREATE TABLE internal_audit (
    audit_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    haccp_plan_id UUID REFERENCES haccp_plan(haccp_plan_id),
    audit_type VARCHAR(50),
    scheduled_date TIMESTAMP,
    actual_date TIMESTAMP,
    finding TEXT,
    nonconformity_description TEXT,
    corrective_action TEXT,
    status VARCHAR(20) DEFAULT 'OPEN',
    auditor_user_id UUID REFERENCES "mes_user"(user_id)
);

CREATE TABLE capa (
    capa_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reference_type VARCHAR(20), -- audit, deviation, ncr
    reference_id UUID,
    description TEXT,
    root_cause_analysis TEXT,
    corrective_action_plan TEXT,
    due_date DATE,
    completed_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'OPEN'
);

-- ---- IF連携 ----
CREATE TABLE integration_config (
    config_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    factory_id UUID REFERENCES factory(factory_id),
    integration_name VARCHAR(100) NOT NULL UNIQUE,
    target_system VARCHAR(50) NOT NULL, -- ERP, WMS, LIMS, HR, PRINTER, IOT_GATEWAY, ACCOUNTING
    endpoint_url TEXT NOT NULL,
    auth_config JSONB,
    data_format VARCHAR(20) DEFAULT 'JSON',
    is_active BOOLEAN DEFAULT TRUE,
    retry_count INTEGER DEFAULT 3,
    retry_delay_seconds INTEGER DEFAULT 5,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE integration_log (
    log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    config_id UUID REFERENCES integration_config(config_id),
    factory_id VARCHAR(20) NOT NULL,
    direction VARCHAR(10) NOT NULL CHECK (direction IN ('outbound', 'inbound')),
    entity_type VARCHAR(50),
    request_payload JSONB,
    response_payload JSONB,
    http_status_code INTEGER,
    error_message TEXT,
    started_at TIMESTAMP NOT NULL DEFAULT NOW(),
    finished_at TIMESTAMP,
    duration_ms INTEGER
);

-- ---- 共通テーブル：STOCK_TRANSACTION ----
CREATE TABLE stock_transaction (
    transaction_id BIGSERIAL PRIMARY KEY,
    lot_id UUID REFERENCES lot(lot_id),
    factory_id VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL, -- RECEIPT, CONSUMPTION, PRODUCTION, SHIPMENT, TRANSFER, ADJUSTMENT
    quantity_delta DECIMAL(15,4) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by_user_id UUID REFERENCES "mes_user"(user_id),
    reference_doc_no VARCHAR(100)
);

-- ---- インデックス ----
-- 工場で頻繁にフィルタリングされる列
CREATE INDEX idx_lot_factory ON lot(factory_id);
CREATE INDEX idx_lot_expiry ON lot(expiry_date);
CREATE INDEX idx_lot_status ON lot(status);
CREATE INDEX idx_production_order_factory ON production_order(factory_id);
CREATE INDEX idx_production_order_line ON production_order(line_id);
CREATE INDEX idx_production_batch_factory ON production_batch(factory_id);
CREATE INDEX idx_production_batch_line ON production_batch(line_id);
CREATE INDEX idx_batch_lot_factory ON batch_lot(factory_id);
CREATE INDEX idx_receipt_factory ON receipt(factory_id);
CREATE INDEX idx_shipment_factory ON shipment_order(factory_id);
CREATE INDEX idx_ccp_record_factory ON ccp_monitoring_record(factory_id);
CREATE INDEX idx_ccp_record_line ON ccp_monitoring_record(line_id);
CREATE INDEX idx_sensor_record_factory ON sensor_record(factory_id);
CREATE INDEX idx_sensor_record_factory_date ON sensor_record(factory_id, recorded_at DESC);

-- FIFO検索用（lot_locationの未出庫分）
CREATE INDEX idx_lot_location_active ON lot_location(bin_id, current_qty) WHERE moved_out_date IS NULL;

-- トレーサビリティ検索用
CREATE INDEX idx_shipment_item_batch ON shipment_item(batch_lot_id);
CREATE INDEX idx_production_order_item_lot ON production_order_item(source_lot_id);
CREATE INDEX idx_receipt_item_lot ON receipt_item(lot_id);
CREATE INDEX idx_ccp_record_batch ON ccp_monitoring_record(batch_id);

-- 認証・権限
CREATE INDEX idx_user_role_user ON user_role(user_id);
CREATE INDEX idx_user_factory ON "mes_user"(factory_id);

-- IF連携ログ（高速検索）
CREATE INDEX idx_integration_log_config ON integration_log(config_id, started_at DESC);
CREATE INDEX idx_integration_log_factory ON integration_log(factory_id, started_at DESC);

-- ---- シーケンス・コメント ----
COMMENT ON TABLE factory IS '工場マスタ - 3工場初期値(FTY-001/002/003)';
COMMENT ON TABLE production_line IS '製造ラインマスタ - 各工場に複数ライン';
COMMENT ON TABLE lot IS 'ロット（原材料・半成品・製品）- factoryパーティション分割済み';
COMMENT ON TABLE lot_partition IS 'lotのfactory_id別リストパーティショニング対象';
COMMENT ON TABLE sensor_record IS 'IoTセンサー記録 - 大量データ用、後日パーティション化検討';
COMMENT ON TABLE haccp_plan IS 'HACCP計画マスタ - OPRP/HACCP_PLANの種類に対応';
COMMENT ON TABLE ccp_monitoring_record IS 'CCP監視記録 - IoT自動記録対応';
COMMENT ON TABLE integration_config IS 'IF連携設定 - ERP/WMS/LIMS等外部システム接続';
COMMENT ON TABLE integration_log IS 'IF連携ログ - 全通信履歴保存';
