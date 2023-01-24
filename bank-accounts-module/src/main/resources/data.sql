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

INSERT INTO movimiento (cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES (1, '2023-01-01', 'D', 1200, 2200);
INSERT INTO movimiento (cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES (1, '2023-01-07', 'R', 300, 1900);
INSERT INTO movimiento (cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES (1, '2023-01-12', 'R', 400, 1500);


INSERT INTO movimiento (cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES (2, '2023-01-07', 'R', 4000, 6000);
INSERT INTO movimiento (cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES (2, '2023-01-15', 'D', 6000, 12000);
INSERT INTO movimiento (cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES (2, '2023-01-24', 'R', 2000, 10000);

INSERT INTO movimiento (cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES (4, '2022-04-01', 'D', 200, 300);
INSERT INTO movimiento (cuenta_id, fecha, tipo_movimiento, valor, saldo)
VALUES (4, '2022-06-01', 'R', 150, 150);