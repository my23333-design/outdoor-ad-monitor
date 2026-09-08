-- 新增用户区域权限关联表
CREATE TABLE IF NOT EXISTS sys_user_region (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    region_code VARCHAR(100) NOT NULL COMMENT '区域编码',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_region_code (region_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户区域权限关联表';

-- 新增操作审计日志表
CREATE TABLE IF NOT EXISTS sys_audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '操作账号ID',
    user_name VARCHAR(100) NOT NULL COMMENT '操作账号',
    role_id BIGINT COMMENT '操作角色ID',
    role_name VARCHAR(100) COMMENT '操作角色',
    real_name VARCHAR(100) COMMENT '操作人',
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    module_name VARCHAR(100) COMMENT '操作模块',
    operate_type VARCHAR(50) COMMENT '操作类型',
    operate_content TEXT COMMENT '操作内容',
    region_code VARCHAR(100) COMMENT '所属区域',
    result VARCHAR(20) COMMENT '操作结果',
    error_msg TEXT COMMENT '错误信息',
    INDEX idx_user_id (user_id),
    INDEX idx_operate_time (operate_time),
    INDEX idx_module_name (module_name),
    INDEX idx_operate_type (operate_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作审计日志表';

-- 初始化角色数据
INSERT IGNORE INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, status, del_flag, create_by, create_time) VALUES
(1, '超级管理员', 'admin', 1, '1', '0', '0', 'admin', NOW()),
(2, '区域运维员', 'regional', 2, '4', '0', '0', 'admin', NOW()),
(3, '数据分析师', 'analyst', 3, '1', '0', '0', 'admin', NOW());

-- 创建审计日志索引
CREATE INDEX IF NOT EXISTS idx_sys_audit_log_role_id ON sys_audit_log(role_id);
CREATE INDEX IF NOT EXISTS idx_sys_audit_log_region_code ON sys_audit_log(region_code);