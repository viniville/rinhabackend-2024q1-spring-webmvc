CREATE TABLE IF NOT EXISTS public.cliente (
	id bigint NOT NULL,
	limite bigint DEFAULT 0 NOT NULL,
	saldo bigint DEFAULT 0 NOT NULL,
	CONSTRAINT cliente_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.transacao (
	id bigserial NOT NULL,
	id_cliente bigint NOT NULL,
	descricao varchar(10) NULL,
	tipo char(1) NOT NULL,
	valor bigint NOT NULL,
	realizada_em timestamp DEFAULT current_timestamp NULL,
	CONSTRAINT transacao_pk PRIMARY KEY (id),
	CONSTRAINT transacao_cliente_fk FOREIGN KEY (id_cliente) REFERENCES public.cliente(id)
);

CREATE INDEX IF NOT EXISTS transacao_id_cliente_idx ON public.transacao (id_cliente);

