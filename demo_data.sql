-- ============================================
-- 私人诊所管理系统 - 演示数据SQL脚本
-- ============================================

-- 清空现有数据（可选，谨慎使用）
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE prescription_item;
-- TRUNCATE TABLE prescription;
-- TRUNCATE TABLE medical_case;
-- TRUNCATE TABLE patient;
-- TRUNCATE TABLE drug;
-- TRUNCATE TABLE user;
-- SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 1. 用户表数据 (user)
-- ============================================
INSERT INTO user (password, user_name, role, phone, status, create_time, update_time) VALUES
('admin123', '系统管理员', 'ADMIN', '13800000001', 1, NOW(), NOW()),
('doctor123', '张医生', 'DOCTOR', '13800000002', 1, NOW(), NOW()),
('doctor123', '李医生', 'DOCTOR', '13800000003', 1, NOW(), NOW()),
('doctor123', '王医生', 'DOCTOR', '13800000004', 1, NOW(), NOW());

-- ============================================
-- 2. 药品表数据 (drug)
-- ============================================
INSERT INTO drug (drug_name, specification, price, stock, status, create_time, update_time) VALUES
('阿莫西林胶囊', '0.25g*24粒/盒', 15.50, 100, 1, NOW(), NOW()),
('头孢克肟片', '0.1g*12片/盒', 28.00, 80, 1, NOW(), NOW()),
('布洛芬缓释胶囊', '0.3g*20粒/盒', 22.50, 120, 1, NOW(), NOW()),
('复方甘草片', '50片/瓶', 8.00, 200, 1, NOW(), NOW()),
('感冒灵颗粒', '10g*9袋/盒', 18.00, 150, 1, NOW(), NOW()),
('板蓝根颗粒', '10g*20袋/盒', 12.00, 180, 1, NOW(), NOW()),
('维生素C片', '0.1g*100片/瓶', 6.50, 250, 1, NOW(), NOW()),
('蒙脱石散', '3g*10袋/盒', 16.00, 90, 1, NOW(), NOW()),
('奥美拉唑肠溶胶囊', '20mg*14粒/盒', 35.00, 60, 1, NOW(), NOW()),
('阿司匹林肠溶片', '0.1g*30片/盒', 9.00, 140, 1, NOW(), NOW()),
('双氯芬酸钠缓释片', '75mg*10片/盒', 25.00, 70, 1, NOW(), NOW()),
('左氧氟沙星片', '0.1g*12片/盒', 32.00, 85, 1, NOW(), NOW()),
('对乙酰氨基酚片', '0.5g*20片/盒', 5.00, 300, 1, NOW(), NOW()),
('复方氨酚烷胺片', '12片/盒', 14.00, 110, 1, NOW(), NOW()),
('藿香正气水', '10ml*10支/盒', 13.00, 95, 1, NOW(), NOW());

-- ============================================
-- 3. 患者表数据 (patient)
-- ============================================
INSERT INTO patient (patient_name, gender, age, phone, address, remark, doctor_id, create_time, update_time) VALUES
('张三', '男', 35, '13900000001', '北京市朝阳区XX街道XX号', '高血压病史', 2, NOW(), NOW()),
('李四', '女', 28, '13900000002', '北京市海淀区XX路XX号', '无特殊病史', 2, NOW(), NOW()),
('王五', '男', 45, '13900000003', '北京市西城区XX街XX号', '糖尿病病史', 3, NOW(), NOW()),
('赵六', '女', 32, '13900000004', '北京市东城区XX巷XX号', '无特殊病史', 3, NOW(), NOW()),
('钱七', '男', 50, '13900000005', '北京市丰台区XX路XX号', '心脏病病史', 2, NOW(), NOW()),
('孙八', '女', 25, '13900000006', '北京市石景山区XX街XX号', '无特殊病史', 4, NOW(), NOW()),
('周九', '男', 38, '13900000007', '北京市通州区XX路XX号', '无特殊病史', 4, NOW(), NOW()),
('吴十', '女', 42, '13900000008', '北京市昌平区XX街XX号', '无特殊病史', 2, NOW(), NOW()),
('郑十一', '男', 29, '13900000009', '北京市大兴区XX路XX号', '无特殊病史', 3, NOW(), NOW()),
('王十二', '女', 55, '13900000010', '北京市房山区XX街XX号', '高血压、糖尿病病史', 4, NOW(), NOW());

-- ============================================
-- 4. 病例表数据 (medical_case)
-- ============================================
INSERT INTO medical_case (patient_id, doctor_id, symptom, diagnosis, case_status, visit_time, create_time, update_time) VALUES
(1, 2, '头痛、发热、咳嗽3天', '上呼吸道感染', 'FINISHED', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),
(2, 2, '腹痛、腹泻2天', '急性肠胃炎', 'PRESCRIBED', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 3, '关节疼痛、肿胀', '风湿性关节炎', 'TREATING', DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 3, '胸闷、气短', '支气管炎', 'FINISHED', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),
(5, 2, '胃痛、反酸', '慢性胃炎', 'TREATING', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(6, 4, '发热、咽痛', '急性扁桃体炎', 'PRESCRIBED', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(7, 4, '头痛、头晕', '偏头痛', 'NEW', NOW(), NOW(), NOW()),
(8, 2, '咳嗽、咳痰', '支气管炎', 'TREATING', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(9, 3, '腹痛、恶心', '急性胃炎', 'FINISHED', DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(10, 4, '关节疼痛', '骨关节炎', 'TREATING', DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 2, '复查：血压控制良好', '高血压随访', 'FINISHED', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(2, 2, '复查：症状已缓解', '肠胃炎复查', 'FINISHED', NOW(), NOW(), NOW());

-- ============================================
-- 5. 处方表数据 (prescription)
-- ============================================
INSERT INTO prescription (case_id, doctor_id, total_amount, create_time) VALUES
(1, 2, 45.50, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 2, 24.00, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(4, 3, 67.00, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(6, 4, 50.00, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(9, 3, 30.00, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(11, 2, 18.00, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(12, 2, 16.00, NOW());

-- ============================================
-- 6. 处方明细表数据 (prescription_item)
-- ============================================
-- 处方1：上呼吸道感染
INSERT INTO prescription_item (prescription_id, drug_id, quantity, usage_method, price) VALUES
(1, 1, 2, '口服，一次2粒，一日3次，饭后服用', 15.50),
(1, 5, 1, '口服，一次1袋，一日3次，用温开水冲服', 18.00),
(1, 7, 1, '口服，一次1片，一日1次', 6.50);

-- 处方2：急性肠胃炎
INSERT INTO prescription_item (prescription_id, drug_id, quantity, usage_method, price) VALUES
(2, 8, 1, '口服，一次1袋，一日3次，用温开水冲服', 16.00),
(2, 13, 1, '口服，一次1片，一日3次，饭后服用', 5.00);

-- 处方3：支气管炎
INSERT INTO prescription_item (prescription_id, drug_id, quantity, usage_method, price) VALUES
(3, 2, 2, '口服，一次1片，一日2次，饭后服用', 28.00),
(3, 4, 1, '含服，一次2片，一日3次', 8.00),
(3, 6, 1, '口服，一次1袋，一日3次，用温开水冲服', 12.00);

-- 处方4：急性扁桃体炎
INSERT INTO prescription_item (prescription_id, drug_id, quantity, usage_method, price) VALUES
(4, 12, 1, '口服，一次1片，一日2次，饭后服用', 32.00),
(4, 5, 1, '口服，一次1袋，一日3次，用温开水冲服', 18.00);

-- 处方5：急性胃炎
INSERT INTO prescription_item (prescription_id, drug_id, quantity, usage_method, price) VALUES
(5, 9, 1, '口服，一次1粒，一日1次，饭前30分钟服用', 35.00),
(5, 13, 1, '口服，一次1片，一日3次，饭后服用', 5.00);

-- 处方6：高血压随访
INSERT INTO prescription_item (prescription_id, drug_id, quantity, usage_method, price) VALUES
(6, 6, 1, '口服，一次1袋，一日2次，用温开水冲服', 12.00),
(6, 7, 1, '口服，一次1片，一日1次', 6.50);

-- 处方7：肠胃炎复查
INSERT INTO prescription_item (prescription_id, drug_id, quantity, usage_method, price) VALUES
(7, 8, 1, '口服，一次1袋，一日2次，用温开水冲服', 16.00);

-- ============================================
-- 数据说明
-- ============================================
-- 1. 用户数据：1个管理员，3个医生
-- 2. 药品数据：15种常见药品
-- 3. 患者数据：10个患者，分配给不同的医生
-- 4. 病例数据：12个病例，包含不同状态（NEW、TREATING、PRESCRIBED、FINISHED）
-- 5. 处方数据：7个处方，对应已完成的病例
-- 6. 处方明细：包含多个药品的处方明细
-- 
-- 登录账号（使用user_name作为登录用户名）：
-- - 管理员：系统管理员 / admin123
-- - 医生1：张医生 / doctor123
-- - 医生2：李医生 / doctor123
-- - 医生3：王医生 / doctor123


