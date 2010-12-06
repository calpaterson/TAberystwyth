create table panel(
	"judge" integer not null,
	"room" integer not null,
	"isChair" boolean,
	
	FOREIGN KEY (room) REFERENCES room(id),
	FOREIGN KEY (judge) REFERENCES judge(id),
	
	PRIMARY KEY (room,judge)
	
	);
