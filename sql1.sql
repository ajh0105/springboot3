-- 사용자 테이블 생성
CREATE TABLE users (
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name TEXT NOT NULL,
	email TEXT UNIQUE,
	created_at TIMESTAMP DEFAULT now()
);

-- 권한 테이블 생성
CREATE TABLE roles (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_name TEXT NOT NULL UNIQUE
);

-- 컬럼 추가
ALTER TABLE users ADD COLUMN age INT DEFAULT 1;

-- 컬럼 타입 변경
ALTER TABLE users ALTER COLUMN name TYPE VARCHAR(100);

-- 컬럼값 기본값 추가
ALTER TABLE users ALTER COLUMN email SET DEFAULT 'mem@gmail.com';

-- 컬럼 제약조건 추가
ALTER TABLE us

select * from board;

insert into board(title, content, writer) values ('제목1', '내용1', '관리자');

commit;

drop table board;

select * from board;

insert into board(title, content, writer, view_count) values ('제목1', '내용1', '관리자', 0);
insert into board(title, content, writer, view_count) values ('제목2', '내용2', '김기태', 0);
insert into board(title, content, writer, view_count) values ('제목3', '내용3', '유환희', 0);

commit;


