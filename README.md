# CS_Assignment
This app compares given url of a web page to give the best matching web page from the web pages in the Database
Web page with the best ranking is given as the match and TF-IDF statistics are calculated and used to compare web pages

What happens under the hood

- Reading web pages over http and creating local cache files
- Cache data of pages stored in Custom HashTable
-Calculating tf-idf with the help of Regular Expression
	
	TF = (Number of times term w appears in a document) / (Total number of terms in the document)
	IDF = log_e(Total number of documents / Number of documents with term w in it)

-Web page stats are displayed in GUI
-calulating and comparing user enterd url with local cache data stats



