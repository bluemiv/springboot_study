-- User
INSERT INTO USER(ID, USERNAME, PASSWORD, EMAIL, PHONE, REGISTERED_AT)
VALUES (1, 'hong', '1234', 'hong@gmail.com', '010-1234-1234', '2021-04-25 16:18:02.000000');
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, PHONE, REGISTERED_AT)
VALUES (2, 'hwang', '5678', 'hwang@gmail.com', '010-5678-5678', '2021-04-26 19:43:36.000000');
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, PHONE, REGISTERED_AT)
VALUES (3, 'choi', '0987', 'choi@gmail.com', '010-0897-0789', '2021-04-28 12:45:25.000000');

-- Notice
INSERT INTO NOTICE(ID, TITLE, CONTENTS, HITS, LIKES, DELETED, REGISTERED_AT, USER_ID)
VALUES (1, '테스트로 작성한 제목 1', '테스트로 작성한 내용 1', 0, 0, false, '2021-05-17 12:01:02.000000', 1);
INSERT INTO NOTICE(ID, TITLE, CONTENTS, HITS, LIKES, DELETED, REGISTERED_AT, USER_ID)
VALUES (2, '테스트로 작성한 제목 2', '테스트로 작성한 내용 2', 0, 0, false, '2021-05-18 01:57:46.000000', 1);
INSERT INTO NOTICE(ID, TITLE, CONTENTS, HITS, LIKES, DELETED, REGISTERED_AT, USER_ID)
VALUES (3, '테스트로 작성한 제목 3', '테스트로 작성한 내용 3', 0, 0, false, '2021-05-19 15:20:12.000000', 3);