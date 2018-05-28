create table Top5RetweetedTweets
(
    [WindowTime] Datetime2,
    [TweetId] nvarchar(30),
    [Text] nvarchar(1000),
    [RetweetCount] bigint
);

create index Top5RetweetedTweets_WindowTime on Top5RetweetedTweets(WindowTime);

create table TopUserMentions
(
    [RecordNumber] bigint IDENTITY(1, 1),
    [WindowTime] Datetime2,
    [TopUserMentions] nvarchar(max)
);
create index TopUserMentions_WindowTime on TopUserMentions(WindowTime);

create table TopHashTags
(
    [RecordNumber] bigint IDENTITY(1, 1),
    [WindowTime] Datetime2,
    [TopHashTags] nvarchar(max)
);
create index TopHashTags_WindowTime on TopHashTags(WindowTime);

create table TopReplies
(
    [RecordNumber] bigint IDENTITY(1, 1),
    [WindowTime] Datetime2,
    [TopRepliedTweets] nvarchar(max)
);
create index TopReplies_WindowTime on TopReplies(WindowTime);
