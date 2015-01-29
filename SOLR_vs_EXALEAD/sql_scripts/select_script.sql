select sum(XPATH1_COMPARISON) as H1  , sum(XPATH2_COMPARISON) as DESCRIPTION_PRODUCT, sum(XPATH3_COMPARISON) as PRESENTATION_PRODUCT, sum(XPATH4_COMPARISON) as GEOLOC, sum(XPATH5_COMPARISON) as PRICES_PRODUCT, sum(XPATH6_COMPARISON) as OFFERS, sum(XPATH7_COMPARISON) as DELIVERING_TIME, sum(XPATH8_COMPARISON) AS DETAILS_PRODUCT, count(*) as TOTAL_FICHE_PRODUIT_CRAWLED  into crawl_summary_statistics from solr_vs_exalead where to_fetch=false;
drop table crawl_summary_statistics;
