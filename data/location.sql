CREATE TABLE location(
	"location_name" text not null,
	"rating" smallint
	CONSTRAINT check_rating CHECK (rating>=0 AND rating <=100)
);