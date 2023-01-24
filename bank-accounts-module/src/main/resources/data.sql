CREATE SEQUENCE "NUMERO_CUENTA_SEQ" 
MINVALUE 1 
MAXVALUE 999999999 
INCREMENT BY 1 
START WITH 1000000;

INSERT INTO cuenta (cliente_id, numero_cuenta, tipo_cuenta, saldo_inicial, estado)
VALUES (1, 1000001, 'A', 1000, true);

INSERT INTO cuenta (cliente_id, numero_cuenta, tipo_cuenta, saldo_inicial, estado)
VALUES (1, 1000011, 'C', 10000, true);

INSERT INTO cuenta (cliente_id, numero_cuenta, tipo_cuenta, saldo_inicial, estado)
VALUES (2, 1000020, 'A', 5000, true);

INSERT INTO cuenta (cliente_id, numero_cuenta, tipo_cuenta, saldo_inicial, estado)
VALUES (3, 1000300, 'A', 100, false);