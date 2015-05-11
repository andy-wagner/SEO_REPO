package com.corpus.parallel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.data.DataEntry;
import com.parameters.CategorizerParameters;

public class ParallelResumingCategorizerCorpusFrequencyComputingProcess {

	public static String categorizer_conf_path = "/home/sduprey/My_Data/My_Categorizer_Conf/categorizer.conf";
	public static Properties properties;

	private static String select_not_added_to_tfidf_entry_from_category4 = " select SKU, CATEGORIE_NIVEAU_1, CATEGORIE_NIVEAU_2, CATEGORIE_NIVEAU_3, CATEGORIE_NIVEAU_4,  LIBELLE_PRODUIT, MARQUE, DESCRIPTION_LONGUEUR80, VENDEUR, ETAT, RAYON, TO_FETCH FROM DATA WHERE IS_IN_TFIDF_INDEX=false";

	private static List<DataEntry> tofetch_list = new ArrayList<DataEntry>();

	public static void main(String[] args) {
		System.out.println("Reading the configuration files : "+categorizer_conf_path);
		try{
			loadProperties();
			CategorizerParameters.database_con_path=properties.getProperty("categorizer.database_con_path");
			CategorizerParameters.small_list_pool_size =Integer.valueOf(properties.getProperty("categorizer.small_list_pool_size")); 
			CategorizerParameters.small_list_size_bucket =Integer.valueOf(properties.getProperty("categorizer.small_list_size_bucket")); 
			CategorizerParameters.big_list_pool_size =Integer.valueOf(properties.getProperty("categorizer.big_list_pool_size")); 
			CategorizerParameters.big_list_size_bucket =Integer.valueOf(properties.getProperty("categorizer.big_list_size_bucket")); 
			CategorizerParameters.max_list_size_separator_string=properties.getProperty("categorizer.max_list_size_separator_string");
			CategorizerParameters.recreate_table=Boolean.parseBoolean(properties.getProperty("categorizer.recreate_table"));
			CategorizerParameters.compute_optimal_parameters=Boolean.parseBoolean(properties.getProperty("categorizer.compute_optimal_parameters"));
			CategorizerParameters.kriter_threshold =Integer.valueOf(properties.getProperty("categorizer.kriter_threshold")); 
			CategorizerParameters.small_computing_max_list_size =Integer.valueOf(properties.getProperty("categorizer.small_computing_max_list_size"));
			CategorizerParameters.big_computing_max_list_size =Integer.valueOf(properties.getProperty("categorizer.big_computing_max_list_size"));
			CategorizerParameters.batch_size =Integer.valueOf(properties.getProperty("categorizer.batch_size"));
			CategorizerParameters.displaying_threshold =Integer.valueOf(properties.getProperty("categorizer.displaying_threshold"));
			CategorizerParameters.computing_max_list_size =CategorizerParameters.small_computing_max_list_size;
			CategorizerParameters.corpus_frequency_pool_size =Integer.valueOf(properties.getProperty("categorizer.corpus_frequency_pool_size")); 
			CategorizerParameters.corpus_frequency_size_bucket =Integer.valueOf(properties.getProperty("categorizer.corpus_frequency_size_bucket")); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Trouble getting the configuration : unable to launch the crawler");
			System.exit(0);
		}
		System.out.println("Number of threads for list crawler : "+CategorizerParameters.small_list_pool_size);
		System.out.println("Bucket size for list crawler : "+CategorizerParameters.small_list_size_bucket);
		System.out.println("Database configuration path : "+CategorizerParameters.database_con_path);
		System.out.println("Maximum list size separator : "+CategorizerParameters.max_list_size_separator_string);
		System.out.println("Recreating table : "+CategorizerParameters.recreate_table);
		System.out.println("Computing optimal parameters : "+CategorizerParameters.compute_optimal_parameters);
		System.out.println("Kriter threshold : "+CategorizerParameters.kriter_threshold);
		System.out.println("Small computing maximum list size : "+CategorizerParameters.computing_max_list_size);
		System.out.println("Batch size : "+CategorizerParameters.batch_size);
		System.out.println("Displaying threshold : "+CategorizerParameters.displaying_threshold);
		System.out.println("Number of threads for list crawler : "+CategorizerParameters.corpus_frequency_pool_size);
		System.out.println("Bucket size for list crawler : "+CategorizerParameters.corpus_frequency_size_bucket);
		
		// it would be best to use a property file to store MD5 password
		//		// Getting the database property
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(CategorizerParameters.database_con_path);
			props.load(in);
		} catch (IOException ex) {
			System.out.println("Trouble fetching database configuration");
			ex.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				System.out.println("Trouble fetching database configuration");
				ex.printStackTrace();
			}
		}
		//the following properties have been identified
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String passwd = props.getProperty("db.passwd");


		System.out.println("You'll connect to the postgresql CATEGORIZERDB database as "+user);
		// The database connection
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ExecutorService executor = null;
		try {  
			con = DriverManager.getConnection(url, user, passwd);

			// getting the number of URLs to fetch
			System.out.println("Requesting all data from categories");
			System.out.println("We here fetch all data even those with the to_fetch flag to false");

			// we here just fetch the SKUs which are not already in the tf/idf index
			pst = con.prepareStatement(select_not_added_to_tfidf_entry_from_category4);
			rs = pst.executeQuery();
			int loop_count = 0;
			while (rs.next()) {
				loop_count++;
				// fetching all
				DataEntry entry = new DataEntry();
				String sku = rs.getString(1);
				entry.setSKU(sku);
				// category fetching
				String CATEGORIE_NIVEAU_1 = rs.getString(2);
				entry.setCATEGORIE_NIVEAU_1(CATEGORIE_NIVEAU_1);
				String CATEGORIE_NIVEAU_2 = rs.getString(3);
				entry.setCATEGORIE_NIVEAU_2(CATEGORIE_NIVEAU_2);
				String CATEGORIE_NIVEAU_3 = rs.getString(4);
				entry.setCATEGORIE_NIVEAU_3(CATEGORIE_NIVEAU_3);
				String CATEGORIE_NIVEAU_4 = rs.getString(5);
				entry.setCATEGORIE_NIVEAU_4(CATEGORIE_NIVEAU_4);
				// product libelle
				String  LIBELLE_PRODUIT = rs.getString(6);
				entry.setLIBELLE_PRODUIT(LIBELLE_PRODUIT);
				String MARQUE = rs.getString(7);
				entry.setMARQUE(MARQUE);
				// brand description
				String  DESCRIPTION_LONGUEUR80 = rs.getString(8);
				entry.setDESCRIPTION_LONGUEUR80(DESCRIPTION_LONGUEUR80);
				// vendor and state (available or not)
				String VENDEUR = rs.getString(9);
				entry.setVENDEUR(VENDEUR);
				String ETAT = rs.getString(10);
				entry.setETAT(ETAT);
				String RAYON = rs.getString(11);
				entry.setRAYON(RAYON);
				Boolean to_fetch = rs.getBoolean(12);
				entry.setTO_FETCH(to_fetch);
				// we here just keep the small categories
				tofetch_list.add(entry);
				// that is the very task we want to parallelize
				//				System.out.println("Processing entry SKU : "+entry.getSKU()+" number : "+loop_count);
				//				manager.updateEntry(entry);
				//				manager.flagSkuInTFIDF(entry.getSKU());
			}	

			rs.close();
			pst.close();

			System.out.println("Number of threads for list crawler : "+CategorizerParameters.corpus_frequency_pool_size);
			System.out.println("Bucket size for list crawler : "+CategorizerParameters.corpus_frequency_size_bucket);

			// Instantiating the pool thread
			executor = Executors.newFixedThreadPool(CategorizerParameters.corpus_frequency_pool_size);
			// Instantiating the pool thread

			int size=tofetch_list.size();
			int size_bucket = CategorizerParameters.corpus_frequency_size_bucket;

			System.out.println("We have : " +size + " URL status to fetch according to the database \n");
			// we add one for the euclidean remainder
			int local_count=0;
			List<DataEntry> thread_list = new ArrayList<DataEntry>();
			for (int size_counter=0; size_counter<size;size_counter ++){
				if(local_count<size_bucket ){
					thread_list.add(tofetch_list.get(size_counter));
					local_count++;
				}
				if (local_count==size_bucket){
					// one new connection per task
					System.out.println("Launching another thread with "+local_count+ " SKUs to fetch");
					Runnable worker = new ResumingCategorizerCorpusFrequencyWorkerThread(thread_list);
					executor.execute(worker);		
					// we initialize everything for the next thread
					local_count=0;
					thread_list = new ArrayList<DataEntry>();
				}
			}
			// there might be a last task with the euclidean remainder
			if (thread_list.size()>0){
				// one new connection per task
				System.out.println("Launching another thread with "+local_count+ " SKUs to fetch");
				Runnable worker = new ResumingCategorizerCorpusFrequencyWorkerThread(thread_list);
				executor.execute(worker);
			}
			tofetch_list.clear();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Finished all threads");
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private static void loadProperties(){
		properties = new Properties();
		try {
			properties.load(new FileReader(new File(categorizer_conf_path)));
		} catch (Exception e) {
			System.out.println("Failed to load properties file!!");
			e.printStackTrace();
		}
	}
}

