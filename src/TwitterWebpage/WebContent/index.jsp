<%@page import="java.io.*,java.util.*, java.util.regex.*, proj.TwitterIndex, org.apache.lucene.search.TopDocs, org.apache.lucene.search.ScoreDoc, org.apache.lucene.document.Document" %>
<%@page session="true"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> CS172 Project Demo </title>
<style type="text/css">
.cursorHand{  cursor: pointer;   cursor: hand; }	
#container {    margin:0 auto;    width: 50%;	float: center;	text-align: center;	postion: relative;}
#contents {    width: 100%;	text-align: left;	float: center;}	
#masthead {width: 100%;height: 60px;text-align: center;background-color: #0099cc;float: center;margin:0 auto;}
#results_area {margin-top: 30px;}
#form {	float: center;margin-top: 30px;}
</style>
</head>
<body topmargin="0" leftmargin="10" rightmargin="10" bottommargin="20">
<% 
String queryString = "";
String dir = "..\\..\\LuceneIndexer"; //Directory of where Lucene indexes are stored.
String searchField = "1";
Integer answerSize = 10;
%>
<%
	if (request.getParameter("search_submit") != null) 
	{
		queryString = request.getParameter("queryString");
		searchField = request.getParameter("searchField");
	}
%>
<div id="page">
	<div id="masthead">
		<font size="20" color="#ffffff">CS172 Twitter Search</font>
	</div>
	<div id="container">
		<div id="form">
			<form action="index.jsp" method="POST">
				<br />
				<input type="text" name="queryString" value="<% out.println(queryString); %>" size="40" /> 
				<input type="submit" value="Search" name="search_submit" style="cursor: pointer; cursor: hand;" /> 
  				
   				<select name="searchField">
        		<option value="1">Text Of Tweet</option>
        		<option value="2">Hash Tags</option>
        		<option value="3">User Names</option>
    			</select>	
			</form>
		</div>
		<div id="contents">		
			<div id="results_area">
					<%
					System.out.println("Selection: " + searchField);
					
					if(queryString.trim().length() > 0)
					{
						Document[] hits = TwitterIndex.searchIndex(dir, queryString, searchField);
						String [] queryList = queryString.split("\\s+");
						
							if (answerSize > 1) {
								out.print("<hr />");
							}
							
							for(int i = 0; i < hits.length; i++)
							{									
								if(hits[i] != null)
								{
									String url = "<div class=\"title\"><a href=\"http://twitter.com/" + hits[i].get("userName") + "\"" + "target=\"_blank\">" + "@" + hits[i].get("userName") + "</a></div>";
									
									String tweetText = "";
									String two = "2";

									tweetText = hits[i].get("text");
									
									out.print("<div class=\"results\">");
									out.print(url);
									
									String outputText = "";
									String [] tweetTextParsed = tweetText.split("\\s+");
									
									for(int j = 0; j < tweetTextParsed.length; j++)
									{
										String originalLower = tweetTextParsed[j].toLowerCase();
										boolean wordInQuery = false;
										
										for(int k = 0; k < queryList.length; k++)
										{
											String queryLower = queryList[k].toLowerCase();
											
											if(originalLower.indexOf(queryLower) != -1)
											{
												wordInQuery = true;
												break;
											}
										}
										
										if(wordInQuery)
										{
											String start = "<b>";
											String middle = tweetTextParsed[j];
											outputText += (start + middle + "</b> ");
										}
										else
										{
											outputText += (tweetTextParsed[j] + " ");
										}
									}
								
								
									out.print("<div class=\"snippet\">" + outputText +  "</div>");
									out.print("</div><br />");	
								}
							}
						
							
							if(hits.length == 0)
							{
								out.print("<div class=\"results\">");
								out.print("<div class=\"snippet\">" + "Sorry No Search Results :(" +  "</div>");
								out.print("</div><br />");	
							}
					}
					else
					{
						out.print("<div class=\"results\">");
						out.print("<div class=\"snippet\">" + "Input A Search!" +  "</div>");
						out.print("</div><br />");	
					}
					%>
			</div>
		</div>
	</div>
</div>
</body>
</html>
