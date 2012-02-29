-- mysql -u root -p boryi_01_campaign

TRUNCATE TABLE `types_typ`;
TRUNCATE TABLE `invitees_nvt`;
TRUNCATE TABLE `inviters_nvtr`;
TRUNCATE TABLE `relations_rlt`;
TRUNCATE TABLE `invitations_nvtt`;

ALTER TABLE types_typ auto_increment = 1;
ALTER TABLE invitees_nvt auto_increment = 1;
ALTER TABLE inviters_nvtr auto_increment = 1;
ALTER TABLE relations_rlt auto_increment = 1;
ALTER TABLE invitations_nvtt auto_increment = 1;