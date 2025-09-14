CREATE TABLE IF NOT EXISTS `t_inventory` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `sku_code` VARCHAR(255),
    `quantity` INTEGER(11),
    PRIMARY KEY (`id`)
);