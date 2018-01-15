using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Microsoft.Analytics.Interfaces;
using Microsoft.Analytics.Types.Sql;
using Newtonsoft.Json;

namespace TweetAnalytics {
    public class Tweet {

        public string id_str { get; set; }
        public string text { get; set; }
        public TweetEntities entities { get; set; }
        public string lang { get; set; }
        public string timestamp_ms { get; set; }

        public class TweetEntities {
            public HashTag[] hashtags { get; set; }
            public UserMention[] user_mentions { get; set; }

            public class UserMention {
                public string screen_name { get; set; }
            }

            public class HashTag {
                public string text { get; set; }
            }
        }
    }

    public static class TweetCleanup {
        static readonly HashSet<char> punctuations = new HashSet<char> (new char[] { ',', '.', '@', '#', ';', '&', '!', ':', '-' });
        static readonly HashSet<char> specialChars = new HashSet<char> (new char[] { '@', '#' });

        public static IEnumerable<string> GetNormalizedWords (string text) {
            return text.Split (' ')
                .Where (s => !string.IsNullOrEmpty (s) && !specialChars.Contains (s[0]))
                .Select (s => s.Trim (',', '.', '@', '#', ';', '&', '!', ':', '-', '"', '\'').ToLowerInvariant ())
                .Select (s => s.Trim (',', '.', '@', '#', ';', '&', '!', ':', '-', '"', '\''))
                .Where (s => !string.IsNullOrEmpty (s))
                .Distinct ();
        }
    }

    public class JsonExtractor : IExtractor {
        public override IEnumerable<IRow> Extract (IUnstructuredReader input, IUpdatableRow output) {
            using (var streamReader = new StreamReader (input.BaseStream)) {
                // assumes each line is an independent json object.
                var recordLine = streamReader.ReadLine ();
                while (!string.IsNullOrEmpty (recordLine)) {
                    Tweet tweet = Newtonsoft.Json.JsonConvert.DeserializeObject<Tweet> (recordLine);
                    output.Set<string> ("tweetText", tweet.text);
                    output.Set<string> ("tweetId", tweet.id_str);
                    output.Set<string> ("timestampMs", tweet.timestamp_ms);
                    output.Set<string> ("language", tweet.id_str);

                    SqlArray<string> hashtags = new SqlArray<string> (tweet.entities.hashtags.Select (t => t.text));
                    SqlArray<string> usermentions = new SqlArray<string> (tweet.entities.user_mentions.Select (t => t.screen_name));

                    output.Set<SqlArray<string>> ("hashTags", hashtags);
                    output.Set<SqlArray<string>> ("userMentions", usermentions);

                    yield return output.AsReadOnly ();
                    recordLine = streamReader.ReadLine ();
                }
            }

            yield break;
        }
    }
}