Instructions
-------------

1. Open up the folder "LuceneIndexer" and open up the "Run.bat" file and edit the variable "directory" if desired. (DEFAULT values will work)

2. The directory variable is where the "Tweets.txt" file exists. We assume that we already have a Tweets.txt file from the first part of the project.
A "Tweets.txt" file already exists in the folder given.

NOTE: We had to change the format of our text files in order to get this part working so our old part 1 project no longer outputs data in the correct way.

3. After editing the variable in the .bat file save "Run.bat".

4. Double click "Run.bat" to run the program.

5. After program is done indexing tweets, it will try to run a tomcat server. If it cannot, the Part2Web.war can
be placed in a working tomcat server webapps folder to run.

6. After running the tomcat server, the webpage can be accessed by the following url:  http://localhost:8080/Part2Web
