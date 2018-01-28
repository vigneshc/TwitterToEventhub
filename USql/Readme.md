This folder contains a U-SQL script to get top 1000 words in the given text corpus. It is used to identify stop words.
Stop words computed by this script is used as reference data for Stream Analytics query that computes top words per time window. 
Script assumes files referenced in the script exists.

Uses following features
- Code behind file for extensibility.
- Custom Extractor.
- Cross Apply that uses a C# function in code behind file.

Vs code Azure Datalake tools can be used for running the query.

Steps for running the script

1. Goto CsharpCode and run `copyAndValidate.cmd` to validate any changes.
2. Upload `Newtonsoft.Json.dll` to `/TwitterApp/CodeReferences/Newtonsoft.Json.dll`. 
3. Update `DECLARE @textCorpusFile string = "/TwitterApp/CricketTweets.json";` to appropriate input file.
4. Submit the script along with code behind file using https://docs.microsoft.com/en-us/azure/data-lake-analytics/data-lake-analytics-data-lake-tools-for-vscode or similar toolset.
5. File referenced in `DECLARE @topWordsOutputFile string = "/TwitterApp/Top1000Words.csv";` would have top 1000 words.