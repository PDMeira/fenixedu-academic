alter table PARTY_PARTY change column `KEY_PARTY_CHILDS` PARTY_PARTY.KEY_PARTY_CHILD_PARTIES int NOT NULL default 0;
alter table PARTY_PARTY change column `KEY_PARTY_PARENTS` PARTY_PARTY.KEY_PARTY_PARENT_PARTIES int NOT NULL default 0;

insert into ACCOUNTABILITY_TYPE values (1, 'ORGANIZATIONAL_STRUCTURE');
insert into ACCOUNTABILITY select PARTY_PARTY.ID_INTERNAL, PARTY_PARTY.KEY_PARTY_CHILDS, PARTY_PARTY.KEY_PARTY_PARENTS, 1 from PARTY_PARTY;