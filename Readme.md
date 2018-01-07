Example scala code to read from twitter stream [api](https://developer.twitter.com/en/docs/tutorials/consuming-streaming-data) and send to eventhub.
Uses [Twitter Hosebird client](https://github.com/twitter/hbc) for reading from twitter.
Uses [Azure Eventhubs Java](https://github.com/Azure/azure-event-hubs-java) for sending events to EventHub.
Sends line separated json response to eventhub in GZip compressed format.

**Compiling**
Requires https://www.scala-sbt.org/ 
sbt
compile
run  <twitterConfigJsonFile> <eventHubConfigJsonFile> <secondsToRun>

Packaged jar file can also be run directly.

**Usage**
TwitterToEventHub  <twitterConfigJsonFile> <eventHubConfigJsonFile> <secondsToRun>

*Twitter Config Format:*
```javascript
{
    "trackTerms": "comma,separated,trace,terms",
    "creds": {
        "consumerSecret": "<consumer secret>",
        "token": "<token>",
        "tokenSecret": "<token secret>",
        "consumerKey": "<consumer key>"
    }
}
```

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

There is no warranty of any kind for the items in repository.