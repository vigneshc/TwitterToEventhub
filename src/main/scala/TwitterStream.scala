package TwitterToEventHub

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

import com.google.common.collect.Lists

import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient
import com.twitter.hbc.httpclient.auth.OAuth1

class TwitterStream(config: TwitterStreamConfig, queueSize: Int) extends EventReceiver[String]
    {
    private var twitterClient: BasicClient = _
    private lazy val tweetQueue: LinkedBlockingQueue[String] = new LinkedBlockingQueue[String](queueSize)

    def Poll(waitTimeMs: Long): Option[String] = Option(this.tweetQueue.poll(waitTimeMs, TimeUnit.MILLISECONDS))
    
    def Open(): Unit = {
        val endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(Lists.newArrayList(this.config.trackTerms));
        endpoint.languages(Lists.newArrayList(this.config.languages.getOrElse("en")))
        val auth = new OAuth1(
            this.config.creds.consumerKey,
            this.config.creds.consumerSecret,
            this.config.creds.token,
            this.config.creds.tokenSecret)

        this.twitterClient = new ClientBuilder()
        .hosts(Constants.STREAM_HOST)
        .endpoint(endpoint)
        .authentication(auth)
        .processor(new StringDelimitedProcessor(this.tweetQueue))
        .build();
        
        this.twitterClient.connect()
        println("Connected to Twitter stream with trackWords: " + this.config.trackTerms)
    }

    def Close(): Unit = this.twitterClient.stop();
}