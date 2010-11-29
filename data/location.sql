CREATE TABLE location(
	text name not null;
	smallint rating;
	CONSTRAINT check_rating CHECK (rating>=0 AND rating <=100)
)