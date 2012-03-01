-- mysql -u root -p boryi_01_campaign

-- --------------------------------------------------------
--
-- Table structure for table `types_typ`
--

CREATE TABLE IF NOT EXISTS `types_typ` (
  `typ_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id of the invitee type',
  `typ_type` VARCHAR(32) COMMENT 'Invitee type',
  PRIMARY KEY  (`typ_id`),
  UNIQUE INDEX (`typ_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Invitee types table' AUTO_INCREMENT=1;


-- --------------------------------------------------------
--
-- Table structure for table `invitees_nvt`
--

CREATE TABLE IF NOT EXISTS `invitees_nvt` (
  `nvt_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id of the invitee',
  `nvt_email` VARCHAR(128) NOT NULL COMMENT 'Email of the invitee',
  `nvt_typ_id` INT UNSIGNED NOT NULL COMMENT 'Type of the invitee',
  `nvt_name` VARCHAR(32) COMMENT 'Name of the invitee',
  `nvt_gender` TINYINT(1) COMMENT 'Gender of the invitee',
  `nvt_inserted` DATETIME NOT NULL COMMENT 'When the invitee is added',
  `nvt_disabled` TINYINT(1) DEFAULT 0 COMMENT 'Whether the invitee is disabled',
  PRIMARY KEY  (`nvt_id`),
  UNIQUE INDEX (`nvt_email`),
  INDEX (`nvt_typ_id`, `nvt_id`),
  INDEX (`nvt_disabled`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Invitees table' AUTO_INCREMENT=1;


-- --------------------------------------------------------
--
-- Table structure for table `inviters_nvt`
--

CREATE TABLE IF NOT EXISTS `inviters_nvtr` (
  `nvtr_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id of the inviter',
  `nvtr_email` VARCHAR(128) NOT NULL COMMENT 'Email of the inviter',
  `nvtr_name` VARCHAR(32) COMMENT 'Name of the inviter',
  `nvtr_gender` TINYINT(1) COMMENT 'Gender of the inviter',
  `nvtr_inserted` DATETIME NOT NULL COMMENT 'When the inviter is added',
  PRIMARY KEY  (`nvtr_id`),
  UNIQUE INDEX (`nvtr_email`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Inviters table' AUTO_INCREMENT=1;


-- --------------------------------------------------------
--
-- Table structure for table `relations_rlt`
--

CREATE TABLE IF NOT EXISTS `relations_rlt` (
  `rlt_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id of the relation',
  `rlt_nvt_id` INT UNSIGNED NOT NULL COMMENT 'Id of the invitee',
  `rlt_nvtr_id` INT UNSIGNED NOT NULL COMMENT 'Id of the inviter',
  `rlt_inserted` DATETIME NOT NULL COMMENT 'When the relation is added',
  PRIMARY KEY  (`rlt_id`),
  UNIQUE INDEX (`rlt_nvt_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Invitee and inviter relations table' AUTO_INCREMENT=1;


-- --------------------------------------------------------
--
-- Table structure for table `invitations_nvtt`
--

CREATE TABLE IF NOT EXISTS `invitations_nvtt` (
  `nvtt_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id of the invitation',
  `nvtt_nvt_id` INT UNSIGNED NOT NULL COMMENT 'Id of the invitee',
  `nvtt_key` INT UNSIGNED NOT NULL COMMENT 'Number of invitation for the invitee',
  `nvtt_sent_date` DATETIME NOT NULL COMMENT 'When the invitation is sent',
  `nvtt_opened_date` DATETIME COMMENT 'When the invitation is opend by the invitee',
  `nvtt_clicked_date` DATETIME COMMENT 'When the invitation is clicked by the invitee',
  `nvtt_opened` TINYINT(1) DEFAULT 0 COMMENT 'Whether the invitation was opened by the invitee',
  `nvtt_clicked` TINYINT(1) DEFAULT 0 COMMENT 'Whether the invitation was clicked by the invitee',
  PRIMARY KEY  (`nvtt_id`),
  UNIQUE INDEX (`nvtt_nvt_id`, nvtt_key),
  INDEX (`nvtt_opened`),
  INDEX (`nvtt_clicked`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Invitee and inviter relations table' AUTO_INCREMENT=1;


-- --------------------------------------------------------
--
-- Table structure for table `domains_dmn`
--

CREATE TABLE IF NOT EXISTS `domains_dmn` (
  `dmn_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id of the domain',
  `dmn_domain` VARCHAR(32) NOT NULL COMMENT 'Name of the domain',
  PRIMARY KEY  (`dmn_id`),
  UNIQUE INDEX (`dmn_domain`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Domains table' AUTO_INCREMENT=1;


-- --------------------------------------------------------
--
-- Table structure for table `sendlist_sndl`
--

CREATE TABLE IF NOT EXISTS `sendlist_sndl` (
  `sndl_id` INT UNSIGNED NOT NULL COMMENT 'Id of the invitees',
  `sndl_done` TINYINT(1) NOT NULL COMMENT 'If the email has been sent already',
  INDEX (`sndl_id`),
  INDEX (`sndl_done`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Email list table';
