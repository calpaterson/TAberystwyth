create table team(
    "speaker1" text not null,
    "speaker2" text not null,
    "team_name" text not null,
    constraint fk_speaker1 foreign key (speaker1) references speaker(name),
    constraint fk_speaker2 foreign key (speaker2) references speaker(name)
);
