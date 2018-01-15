Project to build U-SQL code behind file. 
`copyAndValidate.cmd` copies s TweetAnalytics.usql.cs from ..\ and uses dotnet core to compile.

Note that this is a very rudimentary, it at least has following issues
- Package versions and .Net assemblies that this builds against is probably different.
- Packages used here may not be created as assemblies in U-Sql script, so the script can fail compilation while compilation using dotnet core works.


