package dupli.titles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TitleCrawler {
	// we here just want to get every URL from the input file and get if the SKU is sold by market place/cdiscount and so on

	private static List<LineItem> items = new ArrayList<LineItem>();


	public static void main(String[] args)  {
		String fileName="/home/sduprey/My_Data/My_GWT_Extracts/My_Title_To_Fetch/title_to_fetch.csv";
		String outputPathFileName = "/home/sduprey/My_Data/My_Outgoing_Data/My_Title_MP_Extract/results_title_to_fetch.csv";
		try{
			parsing_file(fileName);
		} catch (IOException e){
			e.printStackTrace();
		}
		make_your_job();
		try {
			print_results(outputPathFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Trouble saving result flat file : "+outputPathFileName);
			e.printStackTrace();
		}
	}

	private static void print_results(String outputPathFileName) throws IOException{
		BufferedWriter writer = null;
		// we open the file
		writer=  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPathFileName), "UTF-8"));	
		// we write the header
		writer.write("Title;url_1;offer_1;url_2;offer_2;url_3;offer_3;url_4;offer_4;url_5;offer_5;url_6;offer_6;url_7;offer_7;url_8;offer_8;url_9;offer_9;url_10;offer_10;url_11;offer_11;url_12;offer_12;url_13;offer_13;url_14;offer_14;url_15;offer_15;url_16;offer_16;url_17;offer_17;url_18;offer_18;url_19;offer_19;url_20;offer_20;url_21;offer_21;url_22;offer_22;url_23;offer_23;url_24;offer_24;url_25;offer_25;url_26;offer_26;url_27;offer_27;url_28;offer_28;url_29;offer_29;url_30;offer_30\n");
		// we open the database
		for (LineItem item_to_write : items){

			String to_write = item_to_write.getTitle();
			List<DiscriminedURL> urls = item_to_write.getUrls();
			for (DiscriminedURL url : urls){
				to_write=to_write+";"+url.getUrl()+";"+url.getOffer();
			}
			writer.write(to_write+"\n");
		}
		writer.close();
	}

	private static void make_your_job(){
		for (LineItem item : items){
			List<DiscriminedURL> urls_to_fetch = item.getUrls();
			for (DiscriminedURL url : urls_to_fetch){
				try {
					if (!"".equals(url.getUrl())){
						System.out.println("Fetching url : "+url.getUrl());
						String type =fetch_url(url.getUrl());
						url.setOffer(type);
						System.out.println("Type : "+type);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Trouble fetching URL : "+url);
				}
			}
		}
	}


	private static String fetch_url(String my_url_to_fetch) throws IOException{
		URL page = new URL(my_url_to_fetch);
		HttpURLConnection conn = (HttpURLConnection) page.openConnection();
		conn.connect();
		InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
		BufferedReader buff = new BufferedReader(in);
		String line;
		StringBuilder contentbuilder = new StringBuilder();
		do {
			line = buff.readLine();
			contentbuilder.append(line);
		} while (line != null);
		int cdiscount_index = contentbuilder.toString().indexOf("<p class='fpSellBy'>Vendu et expédié par <span class='logoCDS'>");
		if (cdiscount_index >0){
			return "Cdiscount";

		}else{
			return "Market Place";
		}
	}

	private static void parsing_file(String fileName) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String header = br.readLine();
		System.out.println(header);
		String line="";
		while ((line = br.readLine()) != null) {
			String[] pieces=line.split(";");
			LineItem item = new LineItem();
			List<DiscriminedURL> url_to_fetch = new ArrayList<DiscriminedURL>();

			for (int i=0;i<pieces.length;i++){
				if (i==0){
					item.setTitle(pieces[i]);
				} else {
					DiscriminedURL url = new DiscriminedURL();
					url.setUrl(pieces[i]);
					url_to_fetch.add(url);
				}
			}
			item.setUrls(url_to_fetch);
			items.add(item);
		}
	}

	static class LineItem {
		private String title;
		private List<DiscriminedURL> urls;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<DiscriminedURL> getUrls() {
			return urls;
		}

		public void setUrls(List<DiscriminedURL> urls) {
			this.urls = urls;
		}

	}

	static class DiscriminedURL {
		String url;
		String offer = "Unknown";
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getOffer() {
			return offer;
		}
		public void setOffer(String offer) {
			this.offer = offer;
		}
	}

}