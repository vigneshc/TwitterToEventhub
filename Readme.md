Twitter streams [api](https://developer.twitter.com/en/docs/tutorials/consuming-streaming-data) is an interesting streaming source.
This project contains example code for running continuous queries over twitter streaming data.
Specifically it has

1. Scala code to read from twitter stream and send to either local file or Eventhub.
2. Azure Stream Analytics query to compute windowed aggregates(overview diagram later in this page), under Asa/ folder.
3. U-SQL query to compute stop words, under USql/ folder.

This folder contains example scala code to read from twitter stream [api](https://developer.twitter.com/en/docs/tutorials/consuming-streaming-data) and send to either eventhub or store in a local file.
Uses [Twitter Hosebird client](https://github.com/twitter/hbc) for reading from twitter.
Uses [Azure Eventhubs Java](https://github.com/Azure/azure-event-hubs-java) for sending events to EventHub.
Sends line separated json response to eventhub in GZip compressed format.
Local file is a json array and is optionally gzipped.

**Compiling**

Requires https://www.scala-sbt.org/ 

sbt
compile

**Usage**

`TwitterToEventHub <twitterConfigJsonFile> eventhub|console|gzipTextFile|textFile <eventHubConfigJsonFile>|<destFile> <secondsToRun>`

*Twitter Config Format:*
```javascript
{
    "trackTerms": "comma,separated,trace,terms",
    "languages": "comma,separated, language codes",
    "creds": {
        "consumerSecret": "<consumer secret>",
        "token": "<token>",
        "tokenSecret": "<token secret>",
        "consumerKey": "<consumer key>"
    }
}
```
Create keys from [twitter apps](https://apps.twitter.com/) site.

`trackTerms` and `languages` are according to the documentation [here](https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters)

*eventhub*     -> eventhub sends to Eventhub.

*console*      -> writes tweets to console.

*gzipTextFile* -> writes gzipped json array of tweet responses to local file.

*textFile*     -> writes json array of tweet responses to local file. 

For eventhub, next argument is a file containing eventhub config in below json format.
*Eventhub Config Format:*
```javascript
{
    "creds": {
        "namespace": "<namespace>",
        "eventhubName": "<eventhubname>",
        "sasKeyName": "<sas key name>",
        "sasKey": "<sas key>"
    },
    "maxBatchWaitTimeMs": 2000
}
```

*maxBatchWaitTimeMs* is the maximum amount of time to batch events locally before sending it to event hub. Increasing this increases decreases the number of EventHub messages at the cost of latency. 

For file, `destFile` is the file in which tweet responses are written to. Responses are written as json arrays.

USql/ folder and Asa/ folder has appropriate Readme.md files.

**Azure Stream Analytics Queries**
Stream Analytics queries computes the following. All outputs consider 15 minutes of data and is emitted every minute, i.e. They are all hopping window aggregates with a window size of 15 minutes and hop size of 1 minute.

1. Top 'x' HashTags that reached maximum users.
2. Top 'x' Users mentioned in tweets that reached maximum users.
3. Top 'x' retweets that reached maximum users.
4. Top 'x' tweets that were replied to.

![Streaming query overview](https://github.com/vigneshc/TwitterToEventhub/blob/master/Overview.svg)


There is no warranty of any kind for the items in repository.
