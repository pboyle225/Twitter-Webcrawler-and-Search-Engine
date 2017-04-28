package cs172part2;

import java.io.*;
import java.util.List;
import java.util.Set;

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

public class TextFileIndexer {
	
	IndexWriter getIndexWriter(String dir) throws IOException
	{
		Directory indexDir = FSDirectory.open(new File(dir));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		
		iwc.setOpenMode(OpenMode.CREATE);
		
		return (new IndexWriter(indexDir, iwc));
	}
	
    public JSONArray parseJSONFile(String file) throws IOException, org.json.simple.parser.ParseException
    {
    	
        InputStream jsonFile =  new FileInputStream(file);
        Reader readerJson = new InputStreamReader(jsonFile);

        JSONArray fileObjects= (JSONArray) JSONValue.parse(readerJson);

        return fileObjects;
    }
	
	@SuppressWarnings("deprecation")
	protected Document getDocument(JSONObject object) throws Exception
	{
		Document doc = new Document();	
		
		JSONObject user = (JSONObject) object.get("user");
		
		String userName = (String) user.get("screen_name");
		
		if(userName != null)
		{
			doc.add(new TextField("userName", userName, Field.Store.YES));
		}
		
		Long id = (Long) object.get("id");
		
		if(id != null)
		{
			doc.add(new LongField("id", (Long) object.get("id"), Field.Store.YES));
		}
		
		String created = (String) object.get("created_at");
		
		if(created != null)
		{
			doc.add(new StringField("created_at", (String) object.get("created_at"), Field.Store.YES));
		}
		
		JSONObject coords = (JSONObject) object.get("coordinates");
		
		if(coords != null)
		{
			doc.add(new StringField("coordinates", coords.toString(), Field.Store.YES));
		}
		
		String text = (String) object.get("text");
		
		if(text != null)
		{
			System.out.println("Text: " + text);
			doc.add(new TextField("text", (String) object.get("text"), Field.Store.YES));
		}
		
		JSONObject entities = (JSONObject) object.get("entities");
		
		JSONArray hashtags = (JSONArray) entities.get("hashtags");
		String totalHashtags = "";
		
		if(hashtags != null)
		{		
			for(JSONObject hashtag: (List<JSONObject>) hashtags)
			{
				String temp = (String) hashtag.get("text");
				
				if(temp != null)
				{
					totalHashtags += "#" + temp + " ";
				}
			}
		}
		
		if(totalHashtags != null)
		{
			doc.add(new TextField("hashtags", totalHashtags, Field.Store.YES));
		}

		return doc;
	}
	
	public void run(String dir) throws Exception{

		JSONArray jsonObjects = parseJSONFile("Tweets.txt");
		
		System.out.println(jsonObjects.size());

		IndexWriter indexWriter = getIndexWriter(dir);
		
		for(JSONObject object : (List<JSONObject>) jsonObjects)
		{
			Document doc = getDocument(object);
			indexWriter.addDocument(doc);
		}
		
		indexWriter.close();
	}
}
