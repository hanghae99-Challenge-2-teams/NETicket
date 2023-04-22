INSERT INTO users VALUES (1, 'test@test.com', 'testuser', '$2a$10$DKtF2Bx/dTcFkIPeqg2CkexmiiEojqEpR3cGQb9BGIUzPLQ/ikvou', 'USER');
INSERT INTO users VALUES (2, 'admin@admin.com', '관리자', '$2a$10$DKtF2Bx/dTcFkIPeqg2CkexmiiEojqEpR3cGQb9BGIUzPLQ/ikvou', 'ADMIN');
INSERT INTO users VALUES (3, 'jangjh45@naver.com', '장진혁1', '$2a$10$74l14KuZyHZ2WWdCc9uwd.Yg6HszFHpOhi.CQ6PzCzIG59G1OKc/i', 'USER');
INSERT INTO users VALUES (4, 'test2@test.com', 'testuser2', '$2a$10$DKtF2Bx/dTcFkIPeqg2CkexmiiEojqEpR3cGQb9BGIUzPLQ/ikvou', 'USER');

INSERT INTO event VALUES (1,'2023-06-05T19:00:00', '92edd8f1-90a8-4500-be3d-51fc9e60af8a_옥탑방 고양이.jpg', '영등포구민회관', 10000, '뮤지컬 옥탑방 고양이');
INSERT INTO event VALUES (2,'2023-06-08T19:00:00', 'd993741a-50c3-41d4-b9d1-7592842b401f_클래식음악회.jpg', '세종문화회관', 70000, '클래식 음악회 브람스&슈만');
INSERT INTO event VALUES (3,'2023-04-22T19:00:00', '89834567-e2c1-4903-8ae7-923d3646ccf7_국악 음악회.jpg', '서울아트센터', 50000, '국악 음악회 "향기와 함께"');
INSERT INTO event VALUES (4,'2023-06-02T19:00:00', 'cc6a1ea2-ebc9-4b36-9daa-f17a7a44de0f_아델 콘서트.jpg', '잠실실내체육관', 20000, '아델 내한공연');
INSERT INTO event VALUES (5,'2023-06-07T19:00:00', '3ce539d3-f5da-4124-b420-8d534919d08e_한국시리즈.jpg', '잠실야구장', 50000, 'LG VS 삼성 한국시리즈');
INSERT INTO event VALUES (6,'2023-06-23T19:00:00', 'cc3fc316-4d93-4abf-adf5-8970a0a43ac9_아이유 콘서트.jpg', '잠실종합운동장', 150000, '아이유 콘서트');
INSERT INTO event VALUES (7,'2023-06-03T19:00:00', 'af1db3d5-c115-42fe-8c13-e42453c05dcb_김장훈 콘서트.jpg', '올림픽체조경기장', 100000, '김장훈 콘서트');
INSERT INTO event VALUES (8,'2023-06-05T19:00:00', 'fe2a410b-fc39-4385-833a-a3f96c83d58b_bts콘서트.jpg', '고척돔', 200000, 'BTS 콘서트');
INSERT INTO event VALUES (9,'2023-06-01T19:00:00', '9ae9bf5c-8298-449f-877b-1d851d0a5bb0_연극 잡념.jpg', '예술의전당', 30000, '연극 "잡념"');
INSERT INTO event VALUES (10,'2023-06-01T19:00:00', 'c2542c82-dd16-4d67-86c5-e710db6d2244_강연.jpg', '삼성홀', 30000, '명사 초청 강연 "비즈니스 성공을 위한 전략"');

INSERT INTO ticket_info VALUES (1,TRUE, 50000, '2023-06-01T18:00:00', 50000, 1);
INSERT INTO ticket_info VALUES (2,TRUE, 50000, '2023-04-22T18:00:00', 50000, 2);
INSERT INTO ticket_info VALUES (3,TRUE, 50000, '2023-04-01T18:00:00', 50000, 3);
INSERT INTO ticket_info VALUES (4,TRUE, 50000, '2023-04-01T18:00:00', 50000, 4);
INSERT INTO ticket_info VALUES (5,TRUE, 50000, '2023-04-01T18:00:00', 50000, 5);
INSERT INTO ticket_info VALUES (6,TRUE, 50000, '2023-04-01T18:00:00', 50000, 6);
INSERT INTO ticket_info VALUES (7,TRUE, 50000, '2023-04-01T18:00:00', 50000, 7);
INSERT INTO ticket_info VALUES (8,TRUE, 50000, '2023-04-01T18:00:00', 50000, 8);
INSERT INTO ticket_info VALUES (9,TRUE, 50000, '2023-04-01T18:00:00', 50000, 9);
INSERT INTO ticket_info VALUES (10,TRUE, 1000,  '2023-04-01T18:00:00', 80000, 10);


INSERT INTO reservation VALUES (1, 2, 3, 1);
INSERT INTO reservation VALUES (2, 1, 7, 1);
INSERT INTO reservation VALUES (3, 2, 1, 1);
INSERT INTO reservation VALUES (4, 1, 8, 1);
INSERT INTO reservation VALUES (5, 2, 10, 1);
INSERT INTO reservation VALUES (6, 2, 10, 4);
