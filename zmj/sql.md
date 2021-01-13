
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tb_project`;
CREATE TABLE `tb_project` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `platform` varchar(50) NOT NULL COMMENT '平台',
  `project`varchar(50) NOT NULL DEFAULT '0' COMMENT '项目',
  `module` varchar(50) not NULL COMMENT '模块',
	`ip` varchar(50) not NULL COMMENT 'ip',
	`state` varchar(50)  not  NULL COMMENT '备注',
	`creator` varchar(50) not NULL COMMENT '创建者',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `modify_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目管理';

SET FOREIGN_KEY_CHECKS = 1;
//////////////////////////////////////////
添加多字段唯一性：
alter table tb_project_platform
add unique key no_account(platform,project,module)

//////////////////////////////////////////

