CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT NOT NULL,
    customer_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_orders_users FOREIGN KEY (customer_id) REFERENCES users (id)
);

CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT NOT NULL,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_orderItems_orders FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT FK_orderItems_products FOREIGN KEY (product_id) REFERENCES products (id)
);