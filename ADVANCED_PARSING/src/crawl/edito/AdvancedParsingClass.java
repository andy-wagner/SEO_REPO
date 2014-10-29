package crawl.edito;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AdvancedParsingClass {

	public static void main(String[] args) throws IOException {
		// Initialize Scanner object
		//        Scanner scan = new Scanner("test 12 13 15 123");
		//        // initialize the String pattern
		//        String pattern = "[a-zA-Z]*";
		//        // Printing the tokenized Strings
		//        while(scan.hasNext()){
		//            // skipping first occurrence of the Pattern
		//            scan.skip(pattern);
		//            System.out.println(scan.next());
		//        }
		//        // closing the scanner stream
		//        scan.close();
		//scan.next(pattern);

		//    String fileName = "/Users/pankaj/source.txt";
		String fileName = "C:\\My_Crawl_Edito\\truncatedCrawlFullSiteEDITO_Aout.csv";
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line="";
		StringBuilder current_word=new StringBuilder();;
		int word_counter=0;

		boolean escaped = false;
		int nb_quote = 0;
		boolean iseven = true;
		while ((line = br.readLine()) != null) {
			int char_counter=0;
			while (char_counter<line.length()){
				char current_char=line.charAt(char_counter);
				current_word.append(current_char);
				if (current_char == '\"'){
					nb_quote++;
					iseven= ((nb_quote & 1) == 0);
				}
				if (((current_char==',' )|| (char_counter == line.length()-1 ))  && iseven){
					word_counter++;
					System.out.println((word_counter%8 == 0? 8 : word_counter%8) +current_word.toString());
					
					// we had a very last word if the last line char is ,
					if ((current_char==',' )&&(char_counter == line.length()-1 )){
						word_counter++;
						System.out.println((word_counter%8 == 0? 8 : word_counter%8) +current_word.toString());

					}
					// testing breakpoint
					if (word_counter%8 ==1 && !("OUI,".equals(current_word.toString()) || "NON,".equals(current_word.toString()))){
						System.out.println("we have a problem");
					}
					if (word_counter%8 ==2 && !("OUI,".equals(current_word.toString()) || "NON,".equals(current_word.toString()))){
						System.out.println("we have a problem");
					}

					current_word = new StringBuilder();
				}
				char_counter++;
			}

		}
		//	
		//		Path path = Paths.get(fileName);
		//		Scanner scanner = new Scanner(path);
		////
		//		scanner.
		//		scanner.useDelimiter(".");
		//		while (scanner.hasNext()){
		//			System.out.println(scanner.next());
		//		}
		//		int counter=0;
		//		String text="";
		//		scanner.useDelimiter(",");
		//		while(scanner.hasNext()){      	
		//			text =scanner.next();
		//
		//			int count = text.length() - text.replace("\"", "").length();
		//			boolean iseven = ((count & 1) == 0);
		//			//			if (iseven){
		//			//				if (text.length()>=1 && text.charAt(text.length()-1) != '\"' && text.lastIndexOf("\"")>=0){
		//			//					String radical = text.substring(0, text.lastIndexOf("\""));
		//			//					counter++;
		//			//					System.out.println((counter%8 == 0? 8 : counter%8) +radical);
		//			//					String word = text.substring(text.lastIndexOf("\"")+1);
		//			//					counter++;
		//			//					System.out.println((counter%8 == 0? 8 : counter%8) +word);
		//			//				}else{
		//			//					counter++;
		//			//					System.out.println((counter%8 == 0? 8 : counter%8) +text);
		//			//				}
		//			//				text="";
		//			//			}
		//			//char upcoming = ',';
		//			//		//	while ( !iseven || upcoming == '\"' ){
		//			//			if (iseven && (count ==0)){
		//			//				counter++;
		//			//				System.out.println(counter+text);
		//			//				text="";
		//			//				
		//			//			} else if (iseven) {
		//			//				
		//			//			}
		//
		//			int addingseparator=0;
		//			boolean wasnoteven =!iseven;
		//			while (!iseven){
		//				scanner.useDelimiter("\"[^a-zA-Z\"]*(,|\n)");
		//				String finishingquote = scanner.next();
		//				text=text+finishingquote;
		//				count = text.length() - text.replace("\"", "").length()+1+addingseparator;
		//				addingseparator++;
		//				iseven = ((count & 1) == 0);
		//			}
		//			
		//
		//			if (wasnoteven && ((counter%8) != 7)){
		//				char c=scanner.findInLine(".").charAt(0);
		//				while (!((c==',' )||(c=='\n' )) ){//do nothing !
		//					String tofind=scanner.findInLine(".");
		//					c=tofind==null?'\n' : tofind.charAt(0);
		//				}
		//				
		//				
		//			//	char cc=scanner.findInLine(".").charAt(0);
		//			}
		////			if (wasnoteven && ((counter%8)  ==7)){
		////				char c=scanner.findInLine(".").charAt(0);
		////				while (!((c=='\n' )) ){//do nothing !
		////					//c=scanner.findInLine(".").charAt(0);
		////					String tofind=scanner.findInLine(".");
		////					c=tofind==null?'\n' : tofind.charAt(0);
		////				}
		////			}
		//
		//			// to confirm			
		//			//			char lastchar=',';
		//			//			while (!(iseven&&(lastchar==','))){
		//			//				scanner.useDelimiter("\"|,");
		//			//				String finishingquote = scanner.next();
		//			//				lastchar = scanner.findInLine(".").charAt(0);
		//			//				text=text+finishingquote+lastchar;
		//			//				// +1 because we count the very first beginning
		//			//				count = text.length() - text.replace("\"", "").length();
		//			//				iseven = ((count & 1) == 0);
		//			//				//upcoming = scanner.findInLine(".").charAt(0);
		//			//			}
		//			if (counter == 7){
		//				scanner.useDelimiter("\n");
		//			}else {
		//				scanner.useDelimiter(",");				
		//			}
		//			counter++;
		//			System.out.println((counter%8 == 0? 8 : counter%8) +text);
		//
		//
		//
		//			//			scanner.useDelimiter(",|\n");
		//			//			
		//			//			if (scanner.hasNext()){
		//			//				String lastquote = scanner.next();
		//			//				//System.out.println(lastquote);
		//			//				if ("\"".equals(lastquote)){
		//			//					// we here just drop the closing quote
		//			//				}
		//			//				if( (lastquote.length()>=1)&& !(lastquote.charAt(lastquote.length()-1) == '\"')){
		//			//					while (lastquote.contains("\"\"")){
		//			//						//another quote is opened : we had it to the current text
		//			//						scanner.useDelimiter("\"");
		//			//						if (scanner.hasNext()){
		//			//							String finishingquote = scanner.next();
		//			//							text=text+finishingquote;
		//			//						}
		//			//						scanner.useDelimiter(",|\n");
		//			//						if (scanner.hasNext()){
		//			//							lastquote = scanner.next();
		//			//							//							System.out.println(secdondlastquote);
		//			//							//							if ("\"".equals(secdondlastquote)){
		//			//							//								// we here just drop the closing quote
		//			//							//							}
		//			//						}
		//			//					}
		//			//				}else {
		//			//					text=text+lastquote;
		//			//				}
		//			//			}
		//			//			text=text.replaceAll("\"","");
		//			//			scanner.useDelimiter(",|\n");
		//			//		}
		//			//	scanner.useDelimiter(",|\n");
		//			//		counter++;
		//
		//			//			if (counter ==8){
		//			//				String[] both=text.split("\n");
		//			//				if (both.length >= 1){
		//			//					text =both[0];
		//			//					System.out.println(counter+text);
		//			//					counter=0;
		//			//				}
		//			//				if (both.length >= 2){
		//			//					text =both[1];
		//			//					counter++;
		//			//				}
		//			//			}
		//			//		System.out.println(counter+text);  
		//			//		if (counter == 8){
		//			//			counter=0;
		//			//		}
		//			//		if (counter ==1 && !("OUI".equals(text) || "NON".equals(text))){
		//			//			System.out.println("we have a problem");
		//			//		}
		//			//		if (counter ==2 && !("OUI".equals(text) || "NON".equals(text))){
		//			//			System.out.println("we have a problem");
		//			//		}
		//		}
		//
		//
		//		//		scanner.useDelimiter(",|\n");
		//		//		int counter=0;
		//		//		while(scanner.hasNext()){      	
		//		//			String text = scanner.next();
		//		//			while (text.contains("\"")){
		//		//				scanner.useDelimiter("\"");
		//		//				String finishingquote = scanner.next();
		//		//				text=text+finishingquote;
		//		//				text=text.replaceAll("\"","");
		//		//			    System.out.println(text);
		//		//			}
		//		//			scanner.useDelimiter(",|\n");
		//		//		    System.out.println(text);
		//		//		}
		//
		//
		//		//            // skipping first occurrence of the Pattern
		//		//            scan.skip(pattern);
		//            System.out.println(scan.next());
		//        }

		//read file line by line
		// scanner.skip("");
		//		//      scanner.useDelimiter(System.getProperty("line.separator"));
		//		while(scanner.hasNext()){
		//			// 	scanner.
		//			System.out.println("Lines: "+scanner.next());
		//		}
		//		scanner.close();
		//		//read CSV Files and parse it to object array
		//		/**
		//		 * Pankaj,28,Male
		//		 * Lisa,30,Female
		//		 * Mike,25,Male
		//		 */
		//		scanner = new Scanner(Paths.get("/Users/pankaj/data.csv"));
		//		scanner.useDelimiter(System.getProperty("line.separator"));
		//		while(scanner.hasNext()){
		//			//parse line to get Emp Object
		//			Employee emp = parseCSVLine(scanner.next());
		//			System.out.println(emp.toString());
		//		}
		//		scanner.close();
		//
		//		//read from system input
		//		System.out.println("Read from system input:");
		//		scanner = new Scanner(System.in);
		//		System.out.println("Input first word: "+scanner.next());
	}

	private static Employee parseCSVLine(String line) {
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter("\\s*,\\s*");
		String name = scanner.next();
		int age = scanner.nextInt();
		String gender = scanner.next();
		AdvancedParsingClass jfs = new AdvancedParsingClass();
		return jfs.new Employee(name, age, gender);
	}

	public class Employee{
		private String name;
		private int age;
		private String gender;

		public Employee(String n, int a, String gen){
			this.name = n;
			this.age = a;
			this.gender = gen;
		}

		@Override
		public String toString(){
			return "Name="+this.name+"::Age="+this.age+"::Gender="+this.gender;
		}
	}

}