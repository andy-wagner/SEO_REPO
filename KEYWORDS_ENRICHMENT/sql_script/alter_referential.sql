ALTER TABLE REFERENTIAL_KEYWORDS ADD SOURCE VARCHAR(200);
ALTER TABLE REFERENTIAL_KEYWORDS ADD SEARCH_VOLUME INT;
ALTER TABLE REFERENTIAL_KEYWORDS ADD CDS_TREND INT;

UPDATE referential_keywords set SOURCE = 'RANKS';
UPDATE referential_keywords set SEARCH_VOLUME = -1;
UPDATE referential_keywords set CDS_TREND = -1;

ALTER TABLE REFERENTIAL_KEYWORDS ADD MAGASIN VARCHAR(200);
ALTER TABLE REFERENTIAL_KEYWORDS ADD RAYON VARCHAR(200);
ALTER TABLE REFERENTIAL_KEYWORDS ADD PRODUIT VARCHAR(400);

UPDATE referential_keywords set MAGASIN = 'Unknown';
UPDATE referential_keywords set RAYON = 'Unknown';
UPDATE referential_keywords set PRODUIT = 'Unknown';

ALTER TABLE PRICING_KEYWORDS ALTER COLUMN SUBDOMAIN TYPE varchar(400);