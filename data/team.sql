create table team(
	"id" integer primary key autoincrement,
    "speaker1" integer not null,
    "speaker2" integer not null,
    "name" text not null,
	FOREIGN KEY (speaker1) REFERENCES speaker(id),
	FOREIGN KEY (speaker2) REFERENCES speaker(id)
);
