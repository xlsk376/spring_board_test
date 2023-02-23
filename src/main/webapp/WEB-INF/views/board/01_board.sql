CREATE TABLE board(
	num INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    writer VARCHAR(20),
    email VARCHAR(20),
    subject VARCHAR(20),
    password VARCHAR(20),
    reg_date DATETIME,
    ref INT,
    re_step INT,
    re_level INT,
    readcount INT,
    content VARCHAR(100)
);

INSERT INTO board (writer, email, subject, password, reg_date, ref, re_step, re_level, readcount, content)
VALUES('qwer', 'qwer@naver.com', '제목1', '1234', now(), 1, 1, 1, 0, '내용1');

INSERT INTO board (writer, email, subject, password, reg_date, ref, re_step, re_level, readcount, content)
VALUES('abcd', 'abcd@naver.com', '제목2', '1234', now(), 2, 1, 1, 0, '내용2');