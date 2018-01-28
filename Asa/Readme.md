Contains Azure Stream Analytics query along with javascript functions used for computing twitter analytics.

##Inputs
*EventHub*: Contains tweets in GZip compressed json format, sent using TwitterToEvent program. Instructions for sending events are in '../Readme.md'
*Reference Blob*: Contains stop words computed using instructions in '../Usql/Readme.md' . File produced is review and uploaded to a blob location. `StopWords.csv` is an example file produced using couple of hours of data.

##Output
*Sql Azure*: There are four outputs, all Sql tables. Schema for the tables is in 'DestinationSqlSchema.sql' . Output can be swapped for any other kind of output with minimal configuration changes.

##Functions
Functions\ folder contains the javascript functions used in the Query. 
`AddItemToArray.js` : This function adds an item to array. This is used to compute a global aggregate along with per group aggregates without writing yet another select query.
`GetDistinctWordsArray.js` : This function normalizes words and gets distinct words.

##Query
'AsaQuery.saql' computes 4 outputs. Comments in the script describes each one.
It uses following features
- Javascript functions to normalize words.
- Reference data join for removing stop words.
- CollectTop, Cross Apply for computing top rows within a window.
- Joins to compute absense of data in newer window.
