create table result(
    "round" smallint not null,
    "team" integer not null,
    "position" smallint not null,
	FOREIGN KEY (team) REFERENCES team(id),
	PRIMARY KEY (team,round),
	CONSTRAINT check_position CHECK (position > 0 AND position <5)
);
