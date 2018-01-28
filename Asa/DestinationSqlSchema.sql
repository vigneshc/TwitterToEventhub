create table HashTagUserMentionGlobalWindowCounts
(
    [WindowTime] Datetime2,
    [HashTag] nvarchar(100),
    [MentionedTwitterHandle] nvarchar(100),
    [TweetCount] bigint,
    [QueryVersion] tinyint
);

create index HashTagUserMentionGlobalWindowCounts_WindowTime on HashTagUserMentionGlobalWindowCounts(WindowTime);

create table HashTagWordGlobalWindowCounts
(
    [WindowTime] Datetime2,
    [HashTag] nvarchar(100),
    [word] nvarchar(200),
    [TweetCount] bigint,
    [QueryVersion] tinyint
);
create index HashTagWordGlobalWindowCounts_WindowTime on HashTagWordGlobalWindowCounts(WindowTime);

create table UserMentionWordGlobalWindowCounts
(
    [WindowTime] Datetime2,
    [MentionedTwitterHandle] nvarchar(100),
    [word] nvarchar(200),
    [TweetCount] bigint,
    [QueryVersion] tinyint
);
create index UserMentionWordGlobalWindowCounts_WindowTime on UserMentionWordGlobalWindowCounts(WindowTime);

create table PopularHashTagsThatAreNoLongerMentioned
(
    [JoinTimeStamp] Datetime2,
    [OldWindowTime] Datetime2,
    [HashTag] nvarchar(100),
    [TweetCount] bigint,
    [QueryVersion] tinyint
);

create index PopularHashTagsThatAreNoLongerMentioned_JoinTimeStamp on PopularHashTagsThatAreNoLongerMentioned(JoinTimeStamp);