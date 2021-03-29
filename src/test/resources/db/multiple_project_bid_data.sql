--Customer table data
INSERT INTO customer VALUES (1,'Customer1'); -- Project 1 Customer
INSERT INTO customer VALUES (2,'Customer2'); -- Project 2 Customer

--Project table data
INSERT INTO project VALUES (1,'Project1','2021-03-26 23:59:59',5,1);
INSERT INTO project VALUES (2,'Project2','2021-03-29 10:00:59',10,2);

--Bidder table data
INSERT INTO bidder VALUES (1,'Bidder1');
INSERT INTO bidder VALUES (2,'Bidder2');
INSERT INTO bidder VALUES (3,'Bidder3');
INSERT INTO bidder VALUES (4,'Bidder4');
INSERT INTO bidder VALUES (5,'Bidder5');

--Bid table data
-- Project 1 bids
INSERT INTO bid VALUES (1,600,NULL,1,1,'2021-03-25 23:59:59');
INSERT INTO bid VALUES (2,NULL,25,1,2,'2021-03-25 01:59:59');

--Project 2 bids
INSERT INTO bid VALUES (3,1000,NULL,2,3,'2021-03-29 08:59:59');
INSERT INTO bid VALUES (4,NULL,30,2,4,'2021-03-29 09:59:59');
INSERT INTO bid VALUES (5,800,NULL,2,5,'2021-03-29 07:59:59');