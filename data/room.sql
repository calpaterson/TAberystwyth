create table room(
	"id" integer primary key autoincrement,
    "location" text not null,
    "fprop" integer not null,
    "fop" integer not null,
    "sprop" integer not null,
    "sop" integer not null,
    "round" smallint not null

	CONSTRAINT check_round CHECK (round<0),

	FOREIGN KEY(location) REFERENCES location(name),
	FOREIGN KEY(fprop) REFERENCES team(id),
	FOREIGN KEY(fop) REFERENCES team(id),
	FOREIGN KEY(sprop) REFERENCES team(id),
	FOREIGN KEY(sop) REFERENCES team(id)
);