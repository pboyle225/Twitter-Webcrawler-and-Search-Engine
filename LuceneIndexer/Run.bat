::  Directory of where "Tweets.txt" exists and where to store Lucene Indexes  ::
:: Default is set to current directory ::

set directory=.\\

java -jar TwitterIndexer.jar %directory%

::can change this directory to your own tomcat server::
call ..\TwitterWebpageSearch\apache-tomcat-8.0.20\bin\startup.bat
PAUSE
