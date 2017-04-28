:: Must be an integer value >= 0 for program to run correctly ::
::               Default is set to 1000                       ::

set numOfTweets=100

::  Directory of where to store data   ::
:: Default is set to current directory ::

set directory=.

java -jar TwitterCrawler.jar %numOfTweets% %directory%
PAUSE
