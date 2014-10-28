package com.populating;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TitlePopulatingClass {

	private static int counter = 147728;

	public static void main(String[] args) {

		// Reading the property of our database
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream("database.properties");
			props.load(in);

		} catch (IOException ex) {

			Logger lgr = Logger.getLogger(TitlePopulatingClass.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {

			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				Logger lgr = Logger.getLogger(TitlePopulatingClass.class.getName());
				lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}

		// the following properties have been identified
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String passwd = props.getProperty("db.passwd");
		// the following properties have been identified for our files to parse 
		// and insert into a database
		String csvFile = "/home/sduprey/My_Data/My_GWT_Extracts/extract_dupli_20_oct.csv";
		// Instantiating the database
		Connection con = null;
		PreparedStatement pst = null;
		// the csv file variables
		ResultSet rs = null;
		BufferedReader br = null;
		String line = "";
		String header = null;
		String[] column_names = null;
		String cvsSplitBy = ",|;";
		int nb_line=1;
		// last error
		try {
			con = DriverManager.getConnection(url, user, passwd);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"));	
			// we skip the first line : the headers
			header = br.readLine();
			column_names= header.split(cvsSplitBy);
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				// use comma as separator

				if (nb_line >= counter){
					System.out.println("Inserting line number :"+nb_line);
					String[] splitted_line = line.split(cvsSplitBy);
					// INSERT INTO 
					String title = splitted_line[0];
					String protocol = "";
					String magasin = "";
					String rayon = "";
					String produit = "";
					String domain = "";
					if (splitted_line.length>=2){
						StringTokenizer tokenize = new StringTokenizer(splitted_line[1],"|");
						int counter=0;
						while (tokenize.hasMoreTokens()) {
							counter++;
							String current_url=tokenize.nextToken();
							StringTokenizer tokenizebis = new StringTokenizer(current_url,"/");
							if (tokenizebis.hasMoreTokens()){
								protocol = tokenizebis.nextToken();
							}
							if (tokenizebis.hasMoreTokens()){
								domain = tokenizebis.nextToken();
							}
							if (tokenizebis.hasMoreTokens()){
								magasin = tokenizebis.nextToken();
							}
							if (tokenizebis.hasMoreTokens()){
								rayon = tokenizebis.nextToken();
							}
							if (tokenizebis.hasMoreTokens()){
								produit = tokenizebis.nextToken();
							}

						}

						String stm = "INSERT INTO DUPLICATES(TITLE,NB_URLS,URLS,DUPLICATE_TIME,MAGAZIN,RAYON,PRODUCT)"
								+ " VALUES(?,?,?,?,?,?,?)";

						pst = con.prepareStatement(stm);
						pst.setString(1,title);
						pst.setInt(2,counter);
						pst.setString(3,splitted_line[1]);
						Date current_date = new Date();
						java.sql.Date sqlDate = new java.sql.Date(current_date.getTime());
						pst.setDate(4,sqlDate);
						pst.setString(5,magasin);
						pst.setString(6,rayon);
						pst.setString(7,produit);
						pst.executeUpdate();
					}					
				}
				nb_line++;
			}

		} catch (Exception ex) {
			Logger lgr = Logger.getLogger(TitlePopulatingClass.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}
				if (br != null) {
					br.close();
				}

			} catch (SQLException | IOException ex) {
				Logger lgr = Logger.getLogger(TitlePopulatingClass.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}
}