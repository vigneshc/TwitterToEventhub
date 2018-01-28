Project to build U-SQL code behind file. 
`copyAndValidate.cmd` copies TweetAnalytics.usql.cs from ..\ and uses dotnet core to compile.
To validate changes made to code behind file with only dotnet core installed and no other tooling, run `copyAndValidate.cmd`. If the file is valid, it will print *Success*.

Note that this is a very rudimentary, it at least has following issues
- Package versions and .Net assemblies that this builds against is probably different.
- Packages used here may not be created as assemblies in U-Sql script, so the script can fail compilation while compilation using dotnet core works.


