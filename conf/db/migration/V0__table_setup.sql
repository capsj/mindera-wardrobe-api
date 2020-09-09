CREATE TABLE IF NOT EXISTS clothing_item
(
    id          SERIAL NOT NULL PRIMARY KEY,
    name        text   NOT NULL UNIQUE,
    uploaded_at TIMESTAMP without TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP without TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS category
(
    id   SERIAL NOT NULL PRIMARY KEY,
    name text   NOT NULL UNIQUE UNIQUE
);

CREATE TABLE IF NOT EXISTS outfit
(
    id   SERIAL NOT NULL PRIMARY KEY,
    name text   NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS clothing_item_category
(
    id               SERIAL NOT NULL PRIMARY KEY,
    clothing_item_id bigint NOT NULL,
    category_id      bigint NOT NULL,
    FOREIGN KEY (clothing_item_id) REFERENCES clothing_item (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE IF NOT EXISTS clothing_item_outfit
(
    id               SERIAL NOT NULL PRIMARY KEY,
    clothing_item_id bigint NOT NULL,
    outfit_id        bigint NOT NULL,
    FOREIGN KEY (clothing_item_id) REFERENCES clothing_item (id),
    FOREIGN KEY (outfit_id) REFERENCES outfit (id)
);

CREATE VIEW vw_clothing_item_details AS
SELECT ci.*,
    COALESCE(json_agg(row_to_json(c)) FILTER (WHERE c IS NOT NULL), '[]') AS categories,
    COALESCE(json_agg(row_to_json(o)) FILTER (WHERE o IS NOT NULL), '[]') AS outfits
FROM clothing_item ci
    LEFT JOIN clothing_item_category cic on cic.clothing_item_id = ci.id
    LEFT JOIN category c ON c.id = cic.category_id
    LEFT JOIN clothing_item_outfit cio ON cio.clothing_item_id = ci.id
    LEFT JOIN outfit o ON o.id = cio.id
GROUP BY ci.id
;
