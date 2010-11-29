CREATE table panel(
	"location" text not null,
	"round" smallint not null,
	"judgeID" smallint not null,
	"isChair" boolean not null,
	CONSTRAINT check_round CHECK (round>0),
	CONSTRAINT check_one_chair UNIQUE (round, "isChair")
);