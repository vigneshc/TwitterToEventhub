Contains Azure Stream Analytics query along with javascript functions used for running continuous queries over twitter stream.
Following aggregates are computed every minute considering 15 minutes of data, i.e. They are all hopping window aggregates with a window size of 15 minutes and hop size of 1 minute.

1. Top 'x' HashTags that reached maximum users.
2. Top 'x' Users mentioned in tweets that reached maximum users.
3. Top 'x' retweets that reached maximum users.
4. Top 'x' tweets that were replied to.

*Input EventHub*: Contains tweets in GZip compressed json format, sent using TwitterToEvent program. Instructions for sending events are in '../Readme.md'

*Output Sql Azure*: There are four outputs, all Sql tables. Schema for the tables is in 'DestinationSqlSchema.sql' . Output can be swapped for any other kind of output with minimal configuration changes.

*Functions*
Functions\ folder contains the javascript functions used in the Query. 
`Stringify.js` : Serializes object as json.

**Query**
'AllQueriesCombined.saql' computes 4 outputs, logic is also split into four different files for readability

1. *TopHashTags.saql*
- Computes count of each hashtag in 15 minute window.
- Collects top 'x' from each window and uses javascript function to serialize array to string, to be able to insert to sql.
- Collect top uses follower count sum as proxy for tweet's reach.

2. *TopUserMentions.saql*
- Computes count of user mentions in 15 minute window.
- Rest of the logic is similar to `TopHashTags.saql`.

3. *TopRepliedToTweets.saql*
- Uses join to combine tweet reply with original tweet.
- Computes tweets that are replied to the most in 15 minute window.
- Rest of the logic is similar to `TopHashTags.saql`.

4. *TopRetweets.saql*
- Computes retweeted count for tweets in 15 minute window.
- Collects top 'x' from above count,.
- Uses Cross apply to flatten out result of CollectTop, this is another way to insert arrays into a tabular destination like SQL.

![Query overview](https://github.com/vigneshc/TwitterToEventhub/blob/master/Overview.svg)

