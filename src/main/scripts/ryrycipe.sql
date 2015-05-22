-- Drop tables insctruction

DROP TABLE IF EXISTS recipe_component;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS recipe_category;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS material_category;
DROP TABLE IF EXISTS material_spec;
DROP TABLE IF EXISTS material_category_type;
DROP TABLE IF EXISTS material_component;
DROP TABLE IF EXISTS component;
DROP TABLE IF EXISTS faction;

-- Tables creation

CREATE TABLE material(
    id          TEXT        NOT NULL     PRIMARY KEY,
    description TEXT        NOT NULL,
    category_id INTEGER     NOT NULL,
    spec_id     INTEGER     NOT NULL,
    FOREIGN KEY(category_id) REFERENCES material_category(id),
    FOREIGN KEY(spec_id) REFERENCES material_spec(id)

);

CREATE TABLE material_category(
    id                      INTEGER     NOT NULL PRIMARY KEY,
    category                TEXT        NOT NULL,
    type_id                 INTEGER     NOT NULL,
    name                    TEXT        NOT NULL,
    material_component_id   INTEGER     NOT NULL,
    FOREIGN KEY(type_id) REFERENCES material_category_type(id),
    FOREIGN KEY(material_component_id) REFERENCES material_component(id)
);

CREATE TABLE material_spec(
    id          INTEGER     NOT NULL    PRIMARY KEY,
    faction     TEXT        NOT NULL,
    quality     TEXT        NOT NULL
);

CREATE TABLE material_category_type(
    id          INTEGER     NOT NULL    PRIMARY KEY,
    name        TEXT        NOT NULL,
    icon        BLOB        NOT NULL
);

CREATE TABLE material_component(
    id              INTEGER     NOT NULL    PRIMARY KEY,
    component_id_1  TEXT        NOT NULL,
    component_id_2  TEXT        NOT NULL,
    FOREIGN KEY(component_id_1) REFERENCES component(id), 
    FOREIGN KEY(component_id_2) REFERENCES component(id) 
);

CREATE TABLE component(
    id          TEXT     NOT NULL PRIMARY KEY,
    name        TEXT        NOT NULL,
    icon        BLOB        NOT NULL
);

CREATE TABLE recipe(
    id          INTEGER     NOT NULL    PRIMARY KEY,
    name        TEXT        NOT NULL,
    quality     TEXT        NOT NULL,
    icon        BLOB        NOT NULL,
    category_id INTEGER     NOT NULL,
    FOREIGN KEY(category_id) REFERENCES recipe_category(id)
);

CREATE TABLE recipe_category(
    id          INTEGER     NOT NULL    PRIMARY KEY,
    category    TEXT        NOT NULL,
    subcategory TEXT,
    hand        INTEGER
);

CREATE TABLE recipe_component(
    recipe_id       INTEGER     NOT NULL,
    component_id    INTEGER     NOT NULL,
    amount          INTEGER     NOT NULL,
    PRIMARY KEY(recipe_id, component_id),
    FOREIGN KEY(recipe_id) REFERENCES recipe(id),
    FOREIGN KEY(component_id) REFERENCES component(id)
);

CREATE TABLE faction(
    name        TEXT    NOT NULL    PRIMARY KEY,
    icon        TEXT    NOT NULL
);  
