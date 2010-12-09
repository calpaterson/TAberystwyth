insert into speaker(institution,name,esl,novice) values ("ABER", "Tito", 0, 0);
insert into speaker(institution,name,esl,novice) values ("ABER", "Cal", 0, 0);
insert into team(speaker1, speaker2, name) values ("Cal", "Tito", "ABER A");
	
insert into speaker(institution,name,esl,novice) values("SWANSEA", "BOB", 0, 0);
insert into speaker(institution,name,esl,novice) values("SWANSEA", "JAMES", 0,1);
insert into team(speaker1, speaker2, name) values("BOB","JAMES","SWANSEA A");
	
insert into speaker(institution,name,esl,novice) values("CARDIFF", "JOHN", 1, 0);
insert into speaker(institution,name,esl,novice) values("CARDIFF", "EMILY", 0,1);
insert into team(speaker1, speaker2, name) values("JOHN","EMILY","CARDIFF B");
	
insert into speaker(institution,name,esl,novice) values("LAMPETER", "ALAN", 1, 0);
insert into speaker(institution,name,esl,novice) values("LAMPETER", "SELINA", 0,1);
insert into team(speaker1, speaker2, name) values("ALAN","SELINA","LAMPETER A");
	

insert into location values("Old Hall", 90);
insert into location values("A14", 30);
	
insert into judge(name,rating) values("Mike Keary",85);
insert into judge(name,rating) values("Tom Coyle",65);
insert into judge(name,rating) values("Richard Stevens",68);


insert into room(round, fprop,fop,sprop,sop,location) values (1,1,2,3,4, "Old Hall");
insert into panel values(1,1,'true');
insert into panel values(2,1,'false');
insert into panel values(3,1,'false');