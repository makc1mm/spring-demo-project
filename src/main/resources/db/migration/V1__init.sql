CREATE TABLE IF NOT EXISTS person (
	id bigserial,
	email varchar(255) NULL,
	phone_number varchar(255) NULL,
	username varchar(255) NOT NULL,

	CONSTRAINT person_id_pkey PRIMARY KEY (id),
	CONSTRAINT person_username_key UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS product (
	id bigserial,
	description varchar(255) NULL,
	name varchar(255) NOT NULL,
	price int4 NOT NULL,
	person_id bigserial NOT NULL,

	CONSTRAINT product_id_pkey PRIMARY KEY (id)
);

ALTER TABLE product DROP CONSTRAINT IF EXISTS product_person_id_fkey;
ALTER TABLE product ADD CONSTRAINT product_person_id_fkey FOREIGN KEY (person_id) REFERENCES person(id);