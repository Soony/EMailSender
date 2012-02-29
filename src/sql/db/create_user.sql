-- mysql -u root -p boryi_01_campaign

CREATE USER 'cmpgn_master'@'localhost' IDENTIFIED BY 'p@ssw0rd';
CREATE USER 'cmpgn_user'@'localhost' IDENTIFIED BY 'pass#word1';

GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, CREATE, DROP ON boryi_01_campaign.* TO 'cmpgn_master'@'localhost' IDENTIFIED BY '********';
GRANT SELECT, EXECUTE ON boryi_01_campaign.* TO 'cmpgn_user'@'localhost' IDENTIFIED BY '********';
