--liquibase formatted sql

--changeset egorsemenovv:1
CREATE TABLE products
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(64)    NOT NULL,
    description TEXT,
    price       NUMERIC(10, 2) NOT NULL,
    active      BOOLEAN        NOT NULL DEFAULT TRUE,
    start_date  DATE           NOT NULL DEFAULT CURRENT_DATE,
    created_at  DATE           NOT NULL DEFAULT CURRENT_DATE,
    updated_at  DATE           NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE skus
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT             NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    code       VARCHAR(64) UNIQUE NOT NULL,
    color      VARCHAR(32),
    stock      INTEGER            NOT NULL CHECK (stock > 0),
    created_at DATE               NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE               NOT NULL DEFAULT CURRENT_DATE
);

--changeset egorsemenovv:2
ALTER TABLE skus
    ADD COLUMN loaded BOOLEAN NOT NULL DEFAULT FALSE;

--changeset egorsemenovv:3
INSERT INTO products(name, description, price, active, start_date)
VALUES ('Starburst', 'A vibrant and colorful product.', 19.99, true, '2024-10-18'),
       ('Harmony', 'A product designed for balance and peace.', 29.99, true, '2024-10-20'),
       ('Vortex', 'A dynamic product for adventurous spirits.', 15.49, true, '2024-10-15'),
       ('Prism', 'A product that reflects beauty in every way.', 99.99, true, '2024-10-18'),
       ('Apex', 'The peak of innovation in product design.', 9.99, true, '2024-10-18'),
       ('Quantum', 'A product that offers a leap into the future.', 39.99, true, '2024-10-18'),
       ('Nebula', 'A mysterious and alluring product.', 249.99, true, '2024-10-18'),
       ('Fusion', 'Bringing together the best features in one product.', 12.99, true, '2024-10-18'),
       ('Zenith', 'The highest point of quality and design.', 89.99, true, '2024-10-21'),
       ('Element', 'The essential product for your daily needs.', 7.99, true, '2024-10-14'),
       ('Catalyst', 'Igniting change with innovative solutions.', 14.99, true, '2024-10-18'),
       ('Mirage', 'An illusive and captivating product.', 22.50, true, '2024-10-18'),
       ('Pulse', 'A product that beats with the rhythm of life.', 19.00, true, '2024-10-16'),
       ('Odyssey', 'A journey of discovery in product form.', 99.00, true, '2024-10-16'),
       ('Chroma', 'A colorful addition to your life.', 5.99, true, '2024-10-18'),
       ('Nimbus', 'A product that floats above the rest.', 150.00, true, '2024-10-14'),
       ('Echo', 'Resonating with quality and style.', 17.99, true, '2024-10-18'),
       ('Atlas', 'A product that carries the weight of the world.', 129.99, true, '2024-10-18'),
       ('Spectrum', 'Offering a range of features for everyone.', 14.99, true, '2024-10-18'),
       ('Vertex', 'The pinnacle of design and functionality.', 45.99, true, '2024-10-07'),
       ('Solstice', 'A seasonal product for year-round enjoyment.', 79.99, true, '2024-10-12'),
       ('Matrix', 'A complex and versatile product.', 11.99, true, '2024-10-18'),
       ('Enigma', 'A product shrouded in mystery and intrigue.', 199.99, true, '2024-10-11'),
       ('Radiance', 'A bright addition to your life.', 25.00, true, '2024-10-14'),
       ('Horizon', 'A product that opens up new possibilities.', 18.49, true, '2024-10-18'),
       ('Glimmer', 'A sparkling choice for every occasion.', 12.50, true, '2024-10-15');

--changeset egorsemenovv:4
INSERT INTO skus(product_id, code, color, stock)
VALUES (1, 'd44f03b5-1e90-4a8e-8f38-2537e147b295', 'RED', 100),
       (1, 'e7b7d4f5-d2f2-4091-bf2e-6b2896cfec44', 'ORANGE', 50),
       (1, '2c8e3ac7-083c-4a8f-bb94-17f27aaab5c4', 'YELLOW', 75),
       (2, '3a6e98f7-8dcf-42c5-b9a3-5cb8c03e5781', 'GREEN', 200),
       (2, 'aeac2f67-8910-4456-b7f7-dbcbd121572e', 'BLUE', 60),
       (3, 'cd88a5ee-3fc5-46f6-b8d1-b041ab12a2ae', 'PURPLE', 150),
       (3, 'af1b1fd3-3295-4e4a-a7a0-1df42edc8b20', 'RED', 30),
       (4, '5e3b2b60-44c1-4903-93b0-f429fd76b8e3', 'ORANGE', 120),
       (4, '4c9c21b4-1181-4419-9b2b-3d4e63884ab2', 'YELLOW', 90),
       (5, 'c46af018-7e73-4474-a28f-27fa02787296', 'GREEN', 40),
       (5, 'f24e3d71-b5ed-44e0-89c7-108bcd307f07', 'BLUE', 110),
       (6, 'dc8709f3-cab6-459e-b3c1-98dcfc292c99', 'PURPLE', 80),
       (6, '9bb0e02f-3005-4d5b-b5a5-bc7bb0a388c2', 'RED', 50),
       (7, '43ebdd0f-29ee-4c29-9089-dc45c4b5394e', 'ORANGE', 60),
       (7, 'e6bfb248-6889-489b-a05c-3b7ac6f104e7', 'YELLOW', 70),
       (8, 'd8d46e9d-e74c-4e77-b174-96a8b6532894', 'GREEN', 100),
       (8, '0f569c9e-57f2-487b-a517-5f23af33c3f7', 'BLUE', 40),
       (9, '21b009d7-0375-44e8-85f6-c46c7cf2b4f1', 'PURPLE', 30),
       (9, '752c4246-b83d-4ef0-8133-e6c68bc68f1d', 'RED', 200),
       (10, '5f7e25cb-e82d-4866-94ed-edddeaa5e1c8', 'ORANGE', 50),
       (10, '80b186e1-5cb4-485f-b597-97303c9c4179', 'YELLOW', 90),
       (11, '844c2a2d-1c9f-4a0a-b546-8e4467e8eb2c', 'GREEN', 150),
       (11, 'c1ae8884-81e7-4ef1-9f3a-1e7c503c09f9', 'BLUE', 120),
       (12, 'f327e188-e529-4e56-9356-7f6b91b21b2b', 'PURPLE', 110),
       (12, '95a37c45-6ac7-4038-9567-fb4aa9c3a46f', 'RED', 75),
       (13, 'b373e90c-5394-4453-9ab9-9c41edbc8829', 'ORANGE', 100),
       (13, '48ab0cb2-6216-4de2-a476-d44f87891a34', 'YELLOW', 60),
       (14, 'd70953ed-fbbf-4427-b81f-27226ab4c4ff', 'GREEN', 90),
       (14, '3f48a67e-bc3b-40e3-b96e-2ab4ed046e73', 'BLUE', 40),
       (15, 'f773843e-b56e-40c0-8953-fba63816db49', 'PURPLE', 200),
       (15, 'd2decb04-fc53-40f6-b6af-c1eab9c3ee8d', 'RED', 75),
       (16, '94b6bb12-e499-4d76-b3af-e92c8fd49ca1', 'ORANGE', 150),
       (16, '928f1bc2-c6e0-40ed-9f77-35f30b0a0594', 'YELLOW', 80),
       (17, '72b719c6-8c06-4f3e-b42e-456d93b3eb5b', 'GREEN', 60),
       (17, 'ac6c12af-d6a0-4f73-8fc9-f5f51fc3fc7b', 'BLUE', 90),
       (18, 'abf8fd74-f34c-4817-83e5-57c5a1bb74b3', 'PURPLE', 100),
       (18, '2db0b68e-28be-4e36-a0f6-69cc0a5c3e56', 'RED', 150),
       (19, '292d2f15-e9c5-40a7-8d71-7d91497e646b', 'ORANGE', 200),
       (19, '4cd8b26f-9083-4d94-bac5-f3751bba1d71', 'YELLOW', 75),
       (20, 'c6c64aa4-cb2a-4d8f-8967-e9c5b70c5367', 'GREEN', 50),
       (20, '86cd7b93-3704-4519-947f-6a25682616aa', 'BLUE', 90),
       (21, 'd78d5c91-d7eb-411f-b8ba-55d1cf8f803e', 'PURPLE', 40),
       (21, '8e20e96d-5f05-41b4-bff0-e5d7b5e1d0c8', 'RED', 200),
       (22, 'cdd4cf38-2ca0-42f9-bb4d-4e21a22b03eb', 'ORANGE', 60),
       (22, '4829066a-bd6d-4571-8c7b-8ab4182a3705', 'YELLOW', 120),
       (23, 'e1160552-97de-4c58-9b4c-e06a1c9cd0e7', 'GREEN', 100),
       (23, '396255a5-1a43-4b58-aaa4-1f83a4f84dfb', 'BLUE', 30),
       (24, '72a576a5-007c-4e42-92da-bc09e7ffb1fc', 'PURPLE', 90),
       (24, 'df1f1910-15a6-47b0-98c0-1fc1b3ab6e26', 'RED', 200),
       (25, 'c8abf0c2-dcc1-43f7-92ed-d5974e65a2a3', 'ORANGE', 75),
       (25, '68cf34e9-6b76-4b08-8f42-1b4f834fbab2', 'YELLOW', 60),
       (1, '220dbe31-378f-4820-a77c-58f76e4e1ab0', 'GREEN', 90),
       (2, 'f34b8a76-36da-4114-bfef-6bb3f97aa105', 'BLUE', 100),
       (3, '7649c77d-d7cc-4c07-87f7-454219a841ab', 'PURPLE', 150),
       (26, 'e34b2c25-34ad-3104-asca-7aa3f97aa115', 'RED', 100);