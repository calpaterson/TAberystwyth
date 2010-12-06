create table speaker_points(
    "round" integer not null,
    "speaker" text not null,
    "points" integer not null

CONSTRAINT check_round CHECK (round>0),

FOREIGN KEY (speaker) REFERENCES speaker(id)
);