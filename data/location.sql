CREATE TABLE location(
	"name" text not null,
	"rating" smallint
	CONSTRAINT check_rating CHECK (rating>=0 AND rating <=100),
	PRIMARY KEY(name)
);
