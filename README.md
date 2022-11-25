# for-developers-connection-leak

https://www.kaggle.com/datasets/jainilcoder/online-payment-fraud-detection

CREATE TABLE payment (
id             NUMBER PRIMARY KEY,
step           NUMBER NOT NULL,
type           VARCHAR2(50),
amount         NUMBER,
nameorig       VARCHAR2(50),
oldbalanceorg  NUMBER,
newbalanceorig NUMBER,
namedest       VARCHAR2(50),
oldbalancedest NUMBER,
newbalancedest NUMBER,
isfraud        NUMBER,
isflaggedfraud NUMBER
);

CREATE SEQUENCE payment_seq
MINVALUE 1
START WITH 1
INCREMENT BY 50