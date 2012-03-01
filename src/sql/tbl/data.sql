-- mysql -u root -p boryi_01_campaign

INSERT INTO types_typ (typ_type) VALUES 
    ('Friend'), ('Subscriber'), ('Candidate'), ('Recruiter'), ('Web-seeker'), 
    ('Web-university'), ('Web-recruiter'), ('Web-other');

-- import from boryi_01_queries.emails_ml

-- import from boryi_01_jobs.companies_cmp

-- import from boryi_01_jobs.jobs_jb

INSERT INTO  `boryi_01_campaign`.`domains_dmn` (`dmn_id` ,`dmn_domain` ,`photo`)
VALUES 
(NULL ,  'qq.com',  '1'), 
(NULL ,  '163.com',  '1'),
(NULL ,  'gmail.com',  '0')


INSERT INTO  `boryi_01_campaign`.`invitees_nvt` (`nvt_id` ,`nvt_email` ,`nvt_typ_id` ,`nvt_name` ,`nvt_gender` ,`nvt_inserted` ,`nvt_disabled`)
VALUES 
(NULL ,  'waoywssy@163.com',  '1',  'Yang Song 1',  '1',  '2012-03-01 20:05:02',  '0'),
(NULL ,  'songyang201@163.com',  '2',  'Yang Song 2',  '1',  '2012-03-01 20:05:02',  '0'),
(NULL ,  '165175546@qq.com',  '2',  'Yang Song 3',  '1',  '2012-03-01 20:05:02',  '0'),
(NULL ,  'waoywssy@gmail.com',  '2',  'Yang Song 5',  '1',  '2012-03-01 20:05:02',  '0'),
(NULL ,  'songyang201@gmail.com',  '1',  'Yang Song 6',  '1',  '2012-03-01 20:05:02',  '0');

INSERT INTO `boryi_01_campaign`.`inviters_nvtr` (`nvtr_id`, `nvtr_email`, `nvtr_name`, `nvtr_gender`, `nvtr_inserted`) 
VALUES 
(NULL, 'yang.song@boryi.com', 'Boryi', '0', '2012-03-01 20:20:28');