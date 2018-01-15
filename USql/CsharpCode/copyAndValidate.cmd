copy /Y ..\TweetAnalytics.usql.cs .
dotnet build
IF %errorLevel% EQU 0  echo Built successfully. Code behind file is valid for U-SQL script.
