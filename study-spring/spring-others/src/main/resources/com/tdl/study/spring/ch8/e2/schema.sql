CREATE SEQUENCE public.hibernate_sequence
INCREMENT 1
START 1
;

ALTER SEQUENCE public.hibernate_sequence
OWNER TO postgres;


CREATE TABLE public.person
(
  id bigint NOT NULL,
  name text,
  age integer,
  address text,
  PRIMARY KEY (id)
)
  WITH (
  OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.person
OWNER to postgres;