CREATE TABLE carts
(
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL,
    date_created DATE DEFAULT (CURDATE()) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE cart_items
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    cart_id BINARY(16) NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1 NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT `FK_cart_items_carts` FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE,
    CONSTRAINT `FK_cart_items_products` FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT `UC_cart_items_product_quantity` UNIQUE(cart_id, product_id)
);