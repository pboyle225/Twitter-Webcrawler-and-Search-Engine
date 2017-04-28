import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.*;
import org.jsoup.nodes.*;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterS {
	private int numOfTweets;
	private String directory;
	private int numOfFiles;
	
	private final static Object lock = new Object();
	
	final List<Status> statuses = new ArrayList();
	
	private Queue<String> websites = new LinkedList ();
	
	TwitterS()
	{
		numOfTweets = 0;
		directory = ".";
		numOfFiles = 0;
	}
	
	TwitterS(int numTweets, String directory)
	{
		numOfTweets = numTweets;
		String parsedDirectory = directory;
		parsedDirectory = parsedDirectory.replace("\\", "\\\\");
		this.directory = parsedDirectory;
		numOfFiles = 0;
	}
	
	StatusListener createListener()
	{
		StatusListener listener = new StatusListener()
		{

			public void onException(Exception arg0) {
				
			}

			public void onDeletionNotice(StatusDeletionNotice arg0) {
				
			}

			public void onScrubGeo(long arg0, long arg1) {
				
			}

			public void onStallWarning(StallWarning arg0) {
				
			}

			public void onStatus(Status status) {
				
				if(statuses.size() < numOfTweets)
				{
					if(status.getGeoLocation() != null)
					{
						statuses.add(status);
						
						if(statuses.size() % 50 == 0)
						{
							System.out.println("Collected " + statuses.size() + " Tweets So Far...");
						}
					}
				}
				else
				{
		            synchronized (lock) 
		            {
		            	lock.notify();
			        }
				}			
			}

			public void onTrackLimitationNotice(int arg0) {
				
			}	
		};
		
		return listener;	
	}
	
	FilterQuery createFilter() //Only collect tweets in the US by filtering query
	{		
		double lat = 39.345311;
		double longitude = -99.0767;
		double lat1 = lat - 7;
		double longitude1 = longitude - 25;
		double lat2 = lat + 8;
		double longitude2 = longitude + 30;
		
		double[][] locs= {{longitude1, lat1}, {longitude2, lat2}};
		FilterQuery filter = new FilterQuery();
		filter.locations(locs);
		
		return filter;
	}
	
	void saveAsFile(String fileName, String htmlContent)
	{
		try {
			String files = fileName.replaceAll("/", "]");		
			files = files.replaceAll(":", ",");
			files = files.replaceAll("\\?", "_");
			String newFile = directory + "\\Websites\\" + files;
			File file = new File(newFile);
			
			FileWriter fw = new FileWriter(file);
			
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(htmlContent);
			bw.close();
		} catch (IOException e) {
			System.err.println("ERROR Saving Tweet To Disk!");
			e.printStackTrace();
		}		
	}
	
	boolean downloadFile(String url) throws IOException
	{
		try
		{
			Connection connection = Jsoup.connect(url).ignoreContentType(true).timeout(10*1000);
			connection.ignoreHttpErrors(true);
			Document doc = connection.get();
			
			String htmlContent = doc.html();
			
			String fileName = url.toString() + ".html";
			
			saveAsFile(fileName, htmlContent);
			return true;
		}
		catch(Exception e)
		{
			System.err.println("Error Connecting To Website: " + url + " Message: " + e.getMessage());
			return false;
		}		
	}
	
	void saveTweetsToDisk()
	{		
		try {
			File file = new File((directory + "\\Tweets.txt"));
			File dir = new File((directory + "\\Websites"));
			
			if(!dir.exists())
			{
				try{
			        dir.mkdir();
			     } catch(SecurityException e){
			        System.err.println("Could Not Make Directory!");
			     }
			}
			

			if (!file.exists()) 
			{
				file.createNewFile();
			}
			else
			{
				double bytes = file.length();
				double kilobytes = (bytes / 1024);
				double megabytes = (kilobytes / 1024);
				
				while(megabytes >= 10)//Only write to the file if 10MB has not been reached
				{								
					System.out.println(file.toString() + " Has reached 10MB Of Data. Writing To New File: Tweets" + numOfFiles + ".txt");
					String newFileName = ("\\Tweets" + numOfFiles + ".txt");
					file = new File(directory + newFileName);
					numOfFiles++;
					
					bytes = file.length();
					kilobytes = (bytes / 1024);
					megabytes = (kilobytes / 1024);
				}
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i = 0; i < statuses.size(); i++)
			{
				User currentUser = statuses.get(i).getUser();
				bw.write(currentUser.getName());
				bw.write(":::");
				bw.write(statuses.get(i).getText());
				bw.write(":::");
				bw.write(statuses.get(i).getCreatedAt().toString());
				bw.write(":::");
				bw.write(statuses.get(i).getGeoLocation().toString());
				bw.write(":::");
				
				
				for(URLEntity urle : statuses.get(i).getURLEntities())
				{
					try {						
						URL url = new URL(urle.getExpandedURL());
						
						websites.add(url.toString()); //Add Websites to Queue
						
						bw.write(url.toString());
						bw.write(":::"); //Separate tweet fields by 3 colons in txt file
					} catch (MalformedURLException e) {
						System.out.println("Error: Malformed URL Exception!");
						e.printStackTrace();
					}
				}
				
				bw.newLine();
			}				
			
			bw.close();
			
			} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	void downloadWebsitesToDisk()
	{
		while(websites.size() != 0)
		{
			String currWebsite = websites.remove();
			
			try {
				System.out.println("Downloading The Website: " + currWebsite);
				downloadFile(currWebsite);
			} catch (IOException e) {
				System.out.println("Error: Malformed URL Exception!");
				e.printStackTrace();
			}
		}
	}
	
	public void run() //Main engine for program
	{
		ConfigurationBuilder cb = new ConfigurationBuilder ();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("ONTw8Tv7ItNlYE8aF6TVKBkNa");
		cb.setOAuthConsumerSecret("6IoHUPrBkEqBUoD05x0rvolbmu8VTs8Rb0l9qUC1KIq0aaB28d");
		cb.setOAuthAccessToken("342422628-VoRuExr2IsVtn6M52N08nJ2jPDrWqWEfBfvolxmp");
		cb.setOAuthAccessTokenSecret("gf5qYP5p34sLztzWWpuRGQgBdLcXnJNU7v8eavo8CQmfG");
		
		final TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		
		StatusListener listener = createListener();
		
		FilterQuery filter = createFilter();
		
		twitterStream.addListener(listener);
		twitterStream.filter(filter);
		
		System.out.println("Downloading " + numOfTweets + " Tweets...");
		
		 try {
		      synchronized (lock) {
		        lock.wait();
		      }
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		 
		twitterStream.shutdown();
		
		System.out.println("Now Saving " + numOfTweets + " Tweets To Disk...");
		saveTweetsToDisk();
		System.out.println("Finished Writing Tweets To Disk");
		
		System.out.println("Now Downloading " + websites.size() + " Webpages...");
		downloadWebsitesToDisk();

		System.out.println("Program Is Complete!");
		System.out.println("Tweets Are Stored In" + directory + "Tweets.txt And Websites Are Stored In " + directory + "Websites\\");
	}

}
