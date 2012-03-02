-- mysql -u root -p boryi_01_campaign

DELIMITER //
DROP PROCEDURE IF EXISTS `insert_invitee`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `insert_invitee`(
email VARCHAR(128), typ_id INT UNSIGNED, domain VARCHAR(32), name VARCHAR(32), 
gender TINYINT(1), inserted DATETIME)
BEGIN
  DECLARE id INT UNSIGNED;
  DECLARE otyp_id INT UNSIGNED;
  DECLARE oname VARCHAR(32);
  DECLARE ogender TINYINT(1);

  DECLARE ntyp_id INT UNSIGNED;
  DECLARE nname VARCHAR(32);
  DECLARE ngender TINYINT(1);

  DECLARE dmnid INT UNSIGNED;

  SELECT nvt_id, nvt_typ_id, nvt_name, nvt_gender INTO id, otyp_id, oname, ogender 
    FROM invitees_nvt WHERE nvt_email = email;

  IF id IS NULL THEN
    SELECT dmn_id INTO dmnid FROM domains_dmn WHERE dmn_domain = domain;
    IF dmnid IS NULL THEN
      INSERT IGNORE INTO `domains_dmn` (dmn_domain) VALUES (domain);
      SELECT dmn_id INTO dmnid FROM domains_dmn WHERE dmn_domain = domain;
    END IF;
    INSERT INTO `invitees_nvt` (`nvt_email`, `nvt_typ_id`, `nvt_dmn_id`, 
        `nvt_name`, `nvt_gender`, `nvt_inserted`) 
      VALUES (email, typ_id, dmnid, name, gender, inserted);
  ELSE
    IF typ_id < otyp_id THEN
      SET ntyp_id = typ_id;
    END IF;
    IF (name IS NOT NULL AND name <> oname) THEN
      SET nname = name;
    END IF;
    IF (gender IS NOT NULL AND gender <> ogender) THEN
      SET ngender = gender;
    END IF;
    IF (ntyp_id IS NOT NULL AND nname IS NOT NULL AND ngender IS NOT NULL) THEN
      UPDATE `invitees_nvt` SET nvt_typ_id = ntyp_id, nvt_name = nname, nvt_gender = ngender 
        WHERE nvt_email = email;
    ELSEIF (ntyp_id IS NOT NULL AND nname IS NOT NULL) THEN
      UPDATE `invitees_nvt` SET nvt_typ_id = ntyp_id, nvt_name = nname 
        WHERE nvt_email = email;
    ELSEIF (ntyp_id IS NOT NULL AND ngender IS NOT NULL) THEN
      UPDATE `invitees_nvt` SET nvt_typ_id = ntyp_id, nvt_gender = ngender 
        WHERE nvt_email = email;
    ELSEIF (nname IS NOT NULL AND ngender IS NOT NULL) THEN
      UPDATE `invitees_nvt` SET nvt_name = nname, nvt_gender = ngender 
        WHERE nvt_email = email;
    ELSEIF (ntyp_id IS NOT NULL) THEN
      UPDATE `invitees_nvt` SET nvt_typ_id = ntyp_id
        WHERE nvt_email = email;
    ELSEIF (nname IS NOT NULL) THEN
      UPDATE `invitees_nvt` SET nvt_name = nname 
        WHERE nvt_email = email;
    ELSEIF (ngender IS NOT NULL) THEN
      UPDATE `invitees_nvt` SET nvt_gender = ngender 
        WHERE nvt_email = email;
    END IF;
  END IF;
END;//


DELIMITER //
DROP PROCEDURE IF EXISTS `insert_inviter`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `insert_inviter`(
email VARCHAR(128), name VARCHAR(32), gender TINYINT(1), inserted DATETIME)
BEGIN
  DECLARE id INT UNSIGNED;
  DECLARE oname VARCHAR(32);
  DECLARE ogender TINYINT(1);

  DECLARE nname VARCHAR(32);
  DECLARE ngender TINYINT(1);

  SELECT nvtr_id, nvtr_name, nvtr_gender INTO id, oname, ogender 
    FROM inviters_nvtr WHERE nvtr_email = email;

  IF id IS NULL THEN
    INSERT INTO `inviters_nvtr` (`nvtr_email`, `nvtr_name`, `nvtr_gender`, `nvtr_inserted`) 
      VALUES (email, name, gender, inserted);
  ELSE
    IF (name IS NOT NULL AND name <> oname) THEN
      SET nname = name;
    END IF;
    IF (gender IS NOT NULL AND gender <> ogender) THEN
      SET ngender = gender;
    END IF;
    IF (nname IS NOT NULL AND ngender IS NOT NULL) THEN
      UPDATE `inviters_nvtr` SET nvtr_name = nname, nvtr_gender = ngender 
        WHERE nvtr_email = email;
    ELSEIF (nname IS NOT NULL) THEN
      UPDATE `inviters_nvtr` SET nvtr_name = nname 
        WHERE nvtr_email = email;
    ELSEIF (ngender IS NOT NULL) THEN
      UPDATE `inviters_nvtr` SET nvtr_gender = ngender 
        WHERE nvtr_email = email;
    END IF;
  END IF;
END;//


DELIMITER //
DROP PROCEDURE IF EXISTS `insert_relation`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `insert_relation`(
invitee_email VARCHAR(128), inviter_email VARCHAR(128), inserted DATETIME)
BEGIN
  DECLARE nvtid INT UNSIGNED;
  DECLARE nvtrid INT UNSIGNED;
  
  SELECT nvt_id INTO nvtid FROM invitees_nvt WHERE nvt_email = invitee_email;
  SELECT nvtr_id INTO nvtrid FROM inviters_nvtr WHERE nvtr_email = inviter_email;

  IF (nvtid IS NOT NULL AND nvtrid IS NOT NULL) THEN
    INSERT IGNORE INTO relations_rlt (rlt_nvt_id, rlt_nvtr_id, rlt_inserted) 
      VALUES (nvtid, nvtrid, inserted);
  END IF;
END;//


DELIMITER //
DROP PROCEDURE IF EXISTS `get_invitees`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `get_invitees`(
typ_id INT UNSIGNED, dmn_id INT UNSIGNED, start_id INT UNSIGNED, end_id INT UNSIGNED)
BEGIN
  SELECT nvt_id, nvt_email, nvt_name, nvt_gender, nvtr_email, nvtr_name, nvtr_gender , nvt_typ_id
    FROM invitees_nvt LEFT JOIN relations_rlt ON nvt_id = rlt_nvt_id 
      LEFT JOIN inviters_nvtr ON nvtr_id = rlt_nvtr_id 
    WHERE nvt_disabled = 0 AND nvt_typ_id = typ_id AND nvt_dmn_id = dmn_id 
      AND nvt_id >= start_id AND nvt_id < end_id
        ORDER BY nvt_id;
END;//


DELIMITER //
DROP PROCEDURE IF EXISTS `disable_invitees`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `disable_invitees`(
typ_id INT UNSIGNED, dmn_id INT UNSIGNED, max_key INT UNSIGNED)
BEGIN
  UPDATE invitees_nvt SET nvt_disabled = 1 WHERE nvt_diabled = 0 
    AND nvt_typ_id = typ_id AND nvt_dmn_id = dmn_id AND nvt_id IN (
      SELECT nvtt_nvt_id FROM (
          SELECT nvtt_nvt_id, COUNT(*) AS nvt_cnt, MAX(nvtt_key) AS nvt_key FROM invitations_nvtt 
            WHERE nvtt_opened = 0 GROUP BY nvtt_nvt_id) AS A
        WHERE A.nvt_cnt > max_key AND A.nvt_cnt = A.nvt_key);
END;//


DELIMITER //
DROP PROCEDURE IF EXISTS `get_domains`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `get_domains`(
photo TINYINT(1))
BEGIN
  SELECT dmn_id, dmn_domain
    FROM domains_dmn 
        WHERE dmn_photo = photo
            ORDER BY dmn_id;
END;//

DELIMITER //
DROP PROCEDURE IF EXISTS `get_types`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `get_types`()
BEGIN
  SELECT typ_id, typ_type
    FROM types_typ
        ORDER BY typ_id;
END;//

DELIMITER //
DROP PROCEDURE IF EXISTS `get_emails_not_sent`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `get_emails_not_sent`()
BEGIN
  SELECT `sndl_id` 
    FROM `sendlist_sndl` 
        WHERE `sndl_done` = 0
            ORDER BY `sndl_id`;
END;//

DELIMITER //
DROP PROCEDURE IF EXISTS `clear_email_list`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `clear_email_list`()
BEGIN
  TRUNCATE TABLE `sendlist_sndl`;
END;//


DELIMITER //
DROP PROCEDURE IF EXISTS `build_checklist`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `build_checklist`(
vnt_id INT UNSIGNED)
BEGIN
    IF NOT EXISTS (SELECT * FROM `sendlist_sndl` WHERE sndl_id = vnt_id) THEN
    INSERT INTO `sendlist_sndl` (`sndl_id`, `sndl_done`) VALUES (vnt_id, '0');
    END IF;
END;//


DELIMITER //
DROP PROCEDURE IF EXISTS `check_lastrun`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `check_lastrun`()
BEGIN
    DELETE FROM sendlist_sndl WHERE sndl_done = 1;
    SELECT sndl_id FROM  `sendlist_sndl`
        WHERE sndl_done = 0;
END;//


DELIMITER //
DROP PROCEDURE IF EXISTS `check_email_sent`//
CREATE DEFINER=`cmpgn_master`@`localhost` PROCEDURE `check_email_sent`(
vnt_id INT UNSIGNED)
BEGIN
    UPDATE `sendlist_sndl` SET sndl_done = 1
        WHERE sndl_id = vnt_id;
END;//