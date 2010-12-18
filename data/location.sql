create table location(
	"name" text not null,
	"rating" smallint
	CONSTRAINT check_rating check (rating>-1 and rating<101),
	PRIMARY KEY(name)
);
