---------------------------------------------------------------Chart Classes ----------------------------------------------
CREATE TABLE IF NOT EXISTS finance.chart_classes
(
    oid bigint NOT NULL ,
    class_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    class_code character varying(100) COLLATE pg_catalog."default" NOT NULL,
    class_type character varying(100) COLLATE pg_catalog."default" NOT NULL  CHECK(class_type IN ('BA','BL','BE','PI','PE')),
    inactive boolean DEFAULT false,
    user_name character varying(100) NOT NULL,
    trans_time timestamp with time zone DEFAULT now(),
    update_user  character varying(100),
    update_time timestamp with time zone,
    version integer,
    CONSTRAINT chart_classes_pk PRIMARY KEY (oid),
    CONSTRAINT chart_classes_class_code_unique UNIQUE (class_code),
    CONSTRAINT chart_classes_class_name_unique UNIQUE (class_name)
)


    TABLESPACE pg_default;

ALTER TABLE IF EXISTS finance.chart_classes
    OWNER to postgres;

CREATE SEQUENCE IF NOT EXISTS finance.chart_classes_oid_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1
    OWNED BY finance.chart_classes.oid;

ALTER SEQUENCE finance.chart_classes_oid_seq
    OWNER TO postgres;

ALTER TABLE finance.chart_classes
    ALTER COLUMN oid SET DEFAULT nextval('finance.chart_classes_oid_seq'::regclass);

CREATE UNIQUE INDEX IF NOT EXISTS chart_class_name_unique_idx
    ON finance.chart_classes (LOWER(class_name));
CREATE UNIQUE INDEX IF NOT EXISTS chart_class_code_unique_idx
    ON finance.chart_classes (LOWER(class_code));


---------------------
CREATE TABLE IF NOT EXISTS finance.chart_types
(
    oid bigint NOT NULL,
    chart_type_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    chart_class_id bigint NOT NULL,
    type_code character varying(100) COLLATE pg_catalog."default" NOT NULL,
    parent_id bigint DEFAULT 0 NOT NULL,
    inactive boolean DEFAULT false,
    user_name character varying(100) NOT NULL,
    trans_time timestamp with time zone DEFAULT now(),
    update_user  character varying(100),
    update_time timestamp with time zone,
    version integer,

    CONSTRAINT chart_types_pk PRIMARY KEY (oid),
    CONSTRAINT chart_types_type_code_unique UNIQUE (chart_class_id, type_code),
    CONSTRAINT chart_types_unique UNIQUE (chart_type_name),
    CONSTRAINT chart_types_chart_class_id_fk FOREIGN KEY (chart_class_id)
        REFERENCES finance.chart_classes (oid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS finance.chart_types
    OWNER to postgres;


CREATE SEQUENCE IF NOT EXISTS finance.chart_types_oid_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1
    OWNED BY finance.chart_types.oid;

ALTER SEQUENCE finance.chart_types_oid_seq
    OWNER TO postgres;

ALTER TABLE finance.chart_types
    ALTER COLUMN oid SET DEFAULT nextval('finance.chart_types_oid_seq'::regclass);

CREATE UNIQUE INDEX IF NOT EXISTS chart_type_name_unique_idx
    ON finance.chart_types (LOWER(chart_type_name));