CREATE table panel(
	text location not null;
	smallint round not null;
	smallint judgeID not null;
	boolean isChair not null;
	CONSTRAINT check_round CHECK (round>0)
	CONSTRAINT check_one_chair UNIQUE (round,isChar)
)