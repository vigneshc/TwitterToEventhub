package TwitterToEventHub

case class TwitterCreds(consumerSecret: String, token: String, tokenSecret: String, consumerKey: String){
}

case class TwitterStreamConfig(creds: TwitterCreds, trackTerms: String, languages:Option[String])
{
}
