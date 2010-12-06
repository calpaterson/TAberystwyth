CREATE TABLE judge(
	"id" integer primary key autoincrement,
	"name" text not null,
	"rating" smallint not null
	CONSTRAINT chk_rating CHECK (rating<=100)
);