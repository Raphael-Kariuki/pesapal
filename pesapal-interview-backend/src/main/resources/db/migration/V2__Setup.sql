INSERT INTO finance.chart_classes(class_name, class_code,  class_type,user_name)
VALUES
    ('Assets', '100', 'BA', 'insert'),
    ('Liabilities', '200', 'BL', 'insert'),
    ('Income', '300', 'PI', 'insert'),
    ('Expenses', '400', 'BE', 'insert'),
    ('Equity', '500', 'PE', 'insert');
--('100','Assets','BA',1,1),
--('200','Liabilities','BL',1,1),
--('300','Income','PI',1,1),
--('400','Expenses','PE',1,1),
--('500','Equity','BE',1,1);


--DELETE FROM finance.chart_types
INSERT INTO finance.chart_types (chart_class_id, chart_type_name,type_code,parent_id, user_name)
VALUES
    (1,'Fixed Assets',100,0,'insert'),
    (1,'Current Assets',200,0,'insert'),
    (1,'Inventory Assets',300,0,'insert'),
    (1,'Banks',400,0,'insert'),
    (1,'M-Pesa',500,4,'insert'),
    (2,'Short-term Liabilities',100,0,'insert'),
    (2,'Long-term Liabilities',200,0,'insert'),
    (3,'Sales',100,0,'insert'),
    (3,'Other Revenue',200,0,'insert'),
    (4,'Cost of Goods Sold',100,0,'insert'),
    (5,'Shares',100,0,'insert'),
    (4,'Administrative Expenses',200,0,'insert'),
    (4,'Finance Expenses',300,0,'insert'),
    (4,'Payroll Expenses',400,0,'insert');
