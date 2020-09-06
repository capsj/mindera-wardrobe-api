CREATE TABLE IF NOT EXISTS clothing_item
(
    id          bigint NOT NULL PRIMARY KEY,
    name        text   NOT NULL,
    uploaded_at TIMESTAMP without TIME ZONE NOT NULL,
    updated_at  TIMESTAMP without TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS category
(
    id   bigint NOT NULL PRIMARY KEY,
    name text   NOT NULL
);

CREATE TABLE IF NOT EXISTS outfit
(
    id   bigint NOT NULL PRIMARY KEY,
    name text   NOT NULL
);

CREATE TABLE IF NOT EXISTS clothing_item_category
(
    id               bigint NOT NULL PRIMARY KEY,
    clothing_item_id bigint NOT NULL,
    category_id      bigint NOT NULL,
    FOREIGN KEY (clothing_item_id) REFERENCES clothing_item (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE IF NOT EXISTS clothing_item_outfit
(
    id               bigint NOT NULL PRIMARY KEY,
    clothing_item_id bigint NOT NULL,
    outfit_id        bigint NOT NULL,
    FOREIGN KEY (clothing_item_id) REFERENCES clothing_item (id),
    FOREIGN KEY (outfit_id) REFERENCES outfit (id)
);

CREATE VIEW vw_clothing_details
SELECT ci.*,
       COALESCE(c.name, '[]'::json) AS categories,
       COALESCE(o.name, '[]'::json) AS outfits
FROM clothing_item ci
         LEFT JOIN clothing_item_category cic on cic.clothing_item_id = ci.id
         LEFT JOIN category c ON c.id = cic.category_id
         LEFT JOIN clothing_item_outfit cio ON cio.clothing_item_id = ci.id
         LEFT JOIN outfit o ON o.id = cio.id
GROUP BY ci.id
;
