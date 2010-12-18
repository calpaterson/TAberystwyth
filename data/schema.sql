-- Schema.sql

-- Version (used to tell if the current debate is out of date)
-- This table should have a single entry, which should be the timestamp of the
-- "last modified" unixtime of this very file.  This table is used when 
-- deciding whether to throw an error due to the database being out of date
-- with this schema.
create table version (
       "version" integer primary key
);

-- Judges
create table judges (
       "name" text primary key,
       "rating" smallint not null default 10
       constraint check_rating check (rating between 0 and 100)
);

-- Speakers
-- Multiple speakers from the same institution are allowed, but they must have
-- different names
create table speakers (
       "name" text not null,
       "institution" text not null,
       "esl" boolean not null default 'f',
       "novice" boolean not null default 'f',
       primary key (name, institution)
);

-- Locations
create table locations (
       "name" text primary key,
       "rating" smallint not null default 10
       constraint check_rating check (rating between 0 and 100)
);

-- Teams
create table teams (
       "name" text not null,
       "speaker1" text not null,
       "speaker2" integer not null,
       primary key (name, speaker1, speaker2),
       foreign key (speaker1) references speaker(name) on delete cascade,
       foreign key (speaker2) references speaker(name) on delete cascade
);

-- Team Results ("team points")
create table team_results (
       "round" smallint not null,
       "team" text not null,
       "position" smallint not null,
       primary key (round, team),
       foreign key (team) references team(name),
       constraint check_position check (position between 1 and 4)
);

-- Speaker Results ("speaker points")
create table speaker_results (
       "round" smallint not null,
       "speaker" text not null,
       "points" smallint not null,
       primary key (round, speaker),
       foreign key (speaker) references speaker(name),
       constraint check_points check (points between 0 and 100)
);
      
-- Rooms (individual debates)
create table rooms (
       "round" smallint not null,
       "judging_panel" integer not null,
       "location" text not null,
       "first_prop" text not null,
       "first_op" text not null,
       "second_prop" text not null,
       "second_op" text not null,
       primary key (round, location),
       foreign key (location) references location(name),
       foreign key (judging_panel) references judging_panel(id),
       foreign key (first_prop) references team(name),
       foreign key (first_op) references team(name),
       foreign key (second_prop) references team(name),
       foreign key (second_op) references team(name)
);

-- Judging panels
create table judging_panels (
       "panel" smallint not null, -- used to differentiate between rounds
       "round" smallint not null,
       "name" text not null,
       "chair" boolean not null default 'f',
       primary key (panel, round, name),
       foreign key (name) references judges(name)
);

-- FIXME: Would be nice to have a check that there is only one chair per
-- panel