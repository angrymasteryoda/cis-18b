DROP TABLE IF EXISTS `mr2358174_karate_entity_blackbelts`;
CREATE TABLE `mr2358174_karate_entity_blackbelts` (
  `blackbelt_id` int(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `belt_degree` int(2) DEFAULT NULL,
  `bio` text,
  `image_url` tinytext,
  PRIMARY KEY (`blackbelt_id`)
);

INSERT INTO `mr2358174_karate_entity_blackbelts` VALUES (1,'Master Arnold Sandubrae',8,'Sensei Arnold Sandubrae began his Isshinryu Karate training in 1973, under the instruction of Sensei\'s Robert L. White, and Sam Santilli, students of Master Willie Adams. Several years later, he transferred to train directly under Master Willie Adams because Master Adams Dojo was located only blocks from Sensei Sandubrae\'s office. From 1973 until 1986 he amassed over 200 trophies in all categories, Kata, Kumite, Weapons, and Breaking.  <a href=\"./biography.php\">Read More</a>','1ea2c871f3aa416b8b5876dbaa20d20c_ajfm.png'),(2,'Master Pat Mcconnell',7,'Master Pat Mcconnell, 7th Degree Black Belt Began his training under Master Sandubrae on June 12th 1980 and was one of the first students to join the Palm Springs ','53fdf94c89b7c650a3abffa9d8696eae.png'),(3,'Master Tom Tweedie',5,'Master Tom Tweedie 5th Degree Black Belt Began his training under Master Sandubrae on ','21fa2c2b103cad9b76c8ac5665ec1230.png'),(4,'Master Jay Doster',4,NULL,'fff876428d651d84ab19aa7b777e3986.png'),(5,'Mr. Erik Kindell',2,NULL,'de538475856f087cd2af50e4ab91049a_g6bq.png'),(6,'Mr. Jeff Manger',1,NULL,'blank_face.jpg'),(7,'Mr. Gerald Petersen',1,NULL,'7dd343ca0c9fe61918e0b8f0855fcca2.jpg'),(8,'Mr. Marvin Peterson',1,NULL,'e753dbfd68edb0eb7bfbd8a444567547_ej4p.png'),(9,'Mr. Daniel Caballero',1,NULL,'17335eef6f7ca36c1538540a3e0d15ec_876m_w19f.png'),(10,'Mr. Angel Mota',1,NULL,'blank_face.jpg');

DROP TABLE IF EXISTS `mr2358174_karate_entity_contact`;

CREATE TABLE `mr2358174_karate_entity_contact` (
  `contact_id` int(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(35) DEFAULT NULL,
  `message` text,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `read` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`contact_id`)
);