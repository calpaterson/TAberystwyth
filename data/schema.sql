-- This file is part of TAberystwyth, a debating competition organiser
-- Copyright (C) 2010, Roberto Sarrionandia and Cal Paterson

-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.

-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/>.

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
       "name" text not null,
       "institution" text not null,
       "rating" smallint not null default 10,
       primary key (name, institution),
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
       "speaker2" text not null,
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
       "location" text not null,
       "first_prop" text not null,
       "first_op" text not null,
       "second_prop" text not null,
       "second_op" text not null,
       primary key (round, location),
       foreign key (location) references location(name),
       foreign key (first_prop) references team(name),
       foreign key (first_op) references team(name),
       foreign key (second_prop) references team(name),
       foreign key (second_op) references team(name)
);

-- Judging panels
create table judging_panels (
       "name" text not null,
       "round" smallint not null,
       "room" text not null,
       "isChair" boolean not null,
       primary key (name, round, room),
       foreign key (name) references judge(name),
       foreign key (room) references locations(name)
);

create table motions (
       "text" text not null,
       "round" smallint not null,
       primary key ("round")
);

-- FIXME: Would be nice to have a check that there is only one chair per
-- panel
