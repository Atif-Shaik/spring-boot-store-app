INSERT INTO categories (name)
VALUES
('Fruits & Vegetables'),
('Dairy & Eggs'),
('Bakery & Bread'),
('Pantry Staples'),
('Beverages');

INSERT INTO products (name, price, description, category_id)
VALUES
-- Category 1: Fruits & Vegetables
('Organic Cavendish Bananas (1 Dozen)', 60.00, 'Fresh, sweet, and perfectly ripe organic bananas packed with potassium.', 1),
('Fresh Hydroponic Spinach (200g)', 45.50, 'Pre-washed, crisp hydroponic baby spinach leaves, perfect for salads and smoothies.', 1),

-- Category 2: Dairy & Eggs
('Premium Whole Milk (1L)', 68.00, 'Pasteurized, homogenized whole milk fresh from local dairy farms.', 2),
('Farm Fresh Large Brown Eggs (Pack of 6)', 55.00, 'All-natural, antibiotic-free brown eggs rich in protein and essential nutrients.', 2),

-- Category 3: Bakery & Bread
('Whole Wheat Atta Bread (400g)', 40.00, 'Healthy, high-fiber sliced brown bread made from 100% whole wheat grains.', 3),
('Freshly Baked Butter Croissants (Pack of 2)', 120.00, 'Flaky, golden-brown, layered French-style pastries made with pure premium butter.', 3),

-- Category 4: Pantry Staples
('Extra Virgin Olive Oil (500ml)', 650.00, 'Cold-pressed extra virgin olive oil perfect for light sautéing, dressings, and dips.', 4),
('Premium Basmati Rice (1kg)', 145.00, 'Aromatic, long-grain basmati rice aged to perfection for non-sticky, fluffy meals.', 4),

-- Category 5: Beverages
('100% Tender Coconut Water (200ml)', 50.00, 'Pure, refreshing, naturally sweet tender coconut water with zero added sugar or preservatives.', 5),
('Dark Roast Ground Coffee (250g)', 299.00, '100% Arabica medium-dark roast coffee blend with rich, chocolatey undertones.', 5);