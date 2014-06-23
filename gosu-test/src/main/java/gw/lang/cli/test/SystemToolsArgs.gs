package gw.lang.cli.test

uses gw.lang.cli.*


/**
 * Command line arguments for SystemToolsAPI.gs. The 'javadoc' and annotations will be used for -usage and -help output. Please put them into one line without line breaks like this line.
 */
class SystemToolsArgs {

  /**
   * The root server URL to access
   * XXX: Newline added as a workaround to Maven filtering.
   */
  @DefaultValue(
          "http://localhost:33" )
//                "http://for.testing/cc")
  @ShortName( "server" )
  @LongName( "server" )
  static var _server : String as Server

  /**
   * The user to log in as
   */
  @DefaultValue( "su" )
  @ShortName( "user" )
  @LongName( "user" )
  static var _user : String as User

  /**
   * The password to use
   */
  @ShortName( "password" )
  @LongName( "password" )
  @Required
  static var _password : String as Password

  /**
   * Print the runlevel of the server
   */
  static var _ping : Boolean as Ping

  /**
   * Run the database consistency checks as a batch job.
   */
  @ShortName( "checkdbconsistency" )
  @LongName( "checkdbconsistency" )
  @ArgNames( {"tables", "checktypes"} )
  @ArgOptional
  static var _checkDBConsistencyAsBatch : String[] as CheckDBConsistency

  /**
   * Checks whether consistency checks are currently running.
   */
  @ShortName( "getdbccstate" )
  @LongName( "getdbccstate" )
  @ArgNames( {"dummy"} )
  @ArgOptional
  static var _dbccState : String[] as GetDBCCState

  /**
   * Update statistics as a batch job.
   */
  @ShortName( "updatestatistics" )
  @LongName( "updatestatistics" )
  @ArgNames( {"description", "incremental"} )
  @ArgOptional
  static var _updateStatistics : String[] as UpdateStatistics

  /**
   * Cancel the update stats process if running. Use -getupdatestatsstate to check on the state.
   */
  @ShortName( "cancelupdatestats" )
  @LongName( "cancelupdatestats" )
  @ArgNames( {"dummy"} )
  @ArgOptional
  static var _cancelUpdateStats : String[] as CancelUpdateStats

  /**
   * Checks whether the update stats process is currently running.
   */
  @ShortName( "getupdatestatsstate" )
  @LongName( "getupdatestatsstate" )
  @ArgNames( {"dummy"} )
  @ArgOptional
  static var _getUpdateStatsState : String[] as GetUpdateStatsState

  /**
   * Run SQL Server DMV Perf Report.  Specify one arguments, or none to take the default (true).
   */
  @ShortName( "mssqlPerfRpt" )
  @LongName( "runSqlServerPerfReport" )
  @ArgNames( {"collectstatistics"} )
  @ArgOptional
  static var _sqlServerPerfReport : String[] as SqlServerPerfReport

  /**
   * List the N most recent Oracle AWR snapshots.
   */
  @ShortName( "oraListSnaps" )
  @LongName( "listOracleAwrSnapshots" )
  @ArgNames( {"numSnapshots"} )
  static var _awrSnapshots : String[] as AwrSnapshots

  /**
   * Run the Oracle AWR performance report.
   */
  @ShortName( "oraPerfRpt" )
  @LongName( "runOracleAwrPerfReport" )
  @ArgNames( {"beginsnapshotid", "endsnapshotid", "probevdollartables"} )
  static var _awrReport : String[] as RunAwrReport

  /**
   * List the N most recent available database perf reports.
   */
  @ShortName( "listPerfReports" )
  @LongName( "listDatabasePerformanceReports" )
  @ArgNames( {"numReports"} )
  @ArgOptional
  static var _listPerfDownloads : String[] as ListPerfReports

  /**
   * Download a database performance report.  Can use the -filepath option.
   */
  @ShortName( "getPerfReport" )
  @LongName( "downloadDatabasePerfReport" )
  @ArgNames( {"id"} )
  static var _downloadDbReport : String[] as DownloadPerfReport

  /**
   * Download zip of database catalog statistics report. Optional args 'tables' 'stagingtables' 'typelisttables'.  Can use the -filepath option.
   */
  @ShortName( "dbcatstats" )
  @LongName( "dbcatstats" )
  @ArgNames( {"tables", "stagingtables", "typelisttables"} )
  @ArgOptional
  static var _reportDBStats : String[] as ReportDBStats

  /**
   * Path for location of downloaded report (optional)
   */
  @ShortName( "filepath" )
  @LongName( "filepath" )
  static var _filepath : String as FilePath

  /**
   * Return the version of the server
   */
  @ShortName( "version" )
  @LongName( "version" )
  static var _version : Boolean as Version

  /**
   * Set the server to maintenance mode
   */
  @ShortName( "maintenance" )
  @LongName( "maintenance" )
  static var _maint : Boolean as Maintenance

  /**
   * Set the server to daemons mode
   */
  @ShortName( "daemons" )
  @LongName( "daemons" )
  @ArgOptional
  static var _daem : Boolean as Daemons

  /**
   * Set the server to multiuser mode
   */
  @ShortName( "multiuser" )
  @LongName( "multiuser" )
  static var _multiUser : Boolean as MultiUser

  /**
   * Get the session information of the server
   */
  @ShortName( "sessioninfo" )
  @LongName( "sessioninfo" )
  static var _sessionInfo : Boolean as SessionInfo

  /**
   * Update the logging level of logger with the given name. For root logger, use RootLogger as the name. Two arguments, loggerName and level
   */
  @ShortName( "updatelogginglevel" )
  @LongName( "updatelogginglevel" )
  static var _updateLoggingLevel : String[] as UpdateLoggingLevel

  /**
   * Get the logger categories available in the system
   */
  @ShortName( "loggercats" )
  @LongName( "loggercats" )
  static var _loggerCategories : Boolean as LoggerCategories

  /**
   * Tell the server to reload it's logging configuration file.
   */
  @ShortName( "reloadloggingconfig" )
  @LongName( "reloadloggingconfig" )
  static var _reloadLoggingConfig : Boolean as ReloadLoggingConfig

  /**
   * Recalculates file checksums used for clustered configuration verification.
   */
  @ShortName( "recalcchecksums" )
  @LongName( "recalcchecksums" )
  static var _recalcChecksums : Boolean as RecalculateChecksums

  /**
   * Verify the data model matches the underlying physical database
   */
  @ShortName( "verifydbschema" )
  @LongName( "verifydbschema" )
  static var _verifyDBSchema : Boolean as VerifyDBSchema

  /**
   * Verify new config against existing config to figure out if
   */
  @ShortName( "verifyconfig" )
  @LongName( "verifyconfig" )
  static var _verifyconfig : String as VerifyConfig

  /**
   * Sets the intention to perform a FULL upgrade. Please refer to the docs for more details.
   */
  @ShortName( "setupgradeintentionindb" )
  @LongName( "setupgradeintentionindb" )
  static var _setUpgradeIntentionInDB : Boolean as SetUpgradeIntentionInDB
}
