package proj;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class TwitterIndex {
	
	public static Document [] searchIndex(String indexDir, String q, String searchField) throws IOException, ParseException
	{
		System.out.println("Searching for: " + q);
		
		IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
		IndexSearcher is = new IndexSearcher(rdr);
		
		String two = "2";
		String three = "3";
		
		if(searchField.equals(two))
		{
			QueryParser parser = new QueryParser(Version.LUCENE_40, "hashtags", new StandardAnalyzer(Version.LUCENE_40));
			Query query = parser.parse(q);
			
			TopDocs hits = is.search(query, 10);
			
			Document [] topHits = new Document [hits.totalHits];
			
			int i = 0;
			
			for(ScoreDoc scoreDoc : hits.scoreDocs)
			{
				Document doc = is.doc(scoreDoc.doc);
				topHits[i++] = doc;
			}
			
			System.out.println("TOPHITS LENGTH: " + topHits.length);
			
			return topHits;
		}
		else if(searchField.equals(three))
		{
			QueryParser parser = new QueryParser(Version.LUCENE_40, "userName", new StandardAnalyzer(Version.LUCENE_40));
			Query query = parser.parse(QueryParser.escape(q));
			
			TopDocs hits = is.search(query, 10);
			
			Document [] topHits = new Document [hits.totalHits];
			
			int i = 0;
			
			for(ScoreDoc scoreDoc : hits.scoreDocs)
			{
				Document doc = is.doc(scoreDoc.doc);
				topHits[i++] = doc;
			}
			
			System.out.println("TOPHITS LENGTH: " + topHits.length);
			
			return topHits;
			
		}
		else
		{
			QueryParser parser = new QueryParser(Version.LUCENE_40, "text", new StandardAnalyzer(Version.LUCENE_40));
			Query query = parser.parse(QueryParser.escape(q));
			
			TopDocs hits = is.search(query, 10);
			
			Document [] topHits = new Document [hits.totalHits];
			
			int i = 0;
			
			for(ScoreDoc scoreDoc : hits.scoreDocs)
			{
				Document doc = is.doc(scoreDoc.doc);
				topHits[i++] = doc;
			}
			
			System.out.println("TOPHITS LENGTH: " + topHits.length);
			
			return topHits;
		}	
	}
}
