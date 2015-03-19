package com.similarity.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.similarity.computing.SimilarityComputingWorkerThread;

public class SimilarityTest {

	private static String database_con_path = "/home/sduprey/My_Data/My_Postgre_Conf/kriter.properties";
	private static Map<String, List<String>> unfetched_skus = new ConcurrentHashMap<String, List<String>>();

	public static void main(String[] args) throws SQLException{
		//		// Getting the database property
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(database_con_path);
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

		System.out.println("You'll connect to the postgresql KRITERDB database as "+user);

		Connection local_con = DriverManager.getConnection(url, user, passwd);
		List<String> categories = new ArrayList<String>();
		//		categories.add("TARTE");
		//		categories.add("CARTE TUNER TV");
		//		categories.add("PANES - CORDON BLEUS");
		//		categories.add("FANION DE SIGNALISATION");	
		//		categories.add("TIGE A URETRE");
		//		categories.add("COQUE - HOUSSE");
		categories.add("SALADE");
		Runnable worker = new SimilarityComputingWorkerThread(local_con,categories,unfetched_skus);
		worker.run();
	}
}
