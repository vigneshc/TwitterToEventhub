create table HashTagUserMentionGlobalWindowCounts
(
    [WindowTime] Datetime2,
    [HashTag] nvarchar(100),
    [MentionedTwitterHandle] nvarchar(100),
    [TweetCount] bigint,
    [QueryVersion] tinyint
);

create table HashTagWordGlobalWindowCounts
(
    [WindowTime] Datetime2,
    [HashTag] nvarchar(100),
    [word] nvarchar(200),
    [TweetCount] bigint,
    [QueryVersion] tinyint
);

create table UserMentionWordGlobalWindowCounts
(
    [WindowTime] Datetime2,
    [MentionedTwitterHandle] nvarchar(100),
    [word] nvarchar(200),
    [TweetCount] bigint,
    [QueryVersion] tinyint
);

create table PopularHashTagsThatAreNoLongerMentioned
(
    [JoinTimeStamp] Datetime2,
    [OldWindowTime] Datetime2,
    [HashTag] nvarchar(100),
    [TweetCount] bigint,
    [QueryVersion] tinyint
);