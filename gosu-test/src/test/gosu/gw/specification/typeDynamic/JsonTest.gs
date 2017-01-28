package gw.specification.typeDynamic

uses dynamic.Dynamic
uses java.lang.*
uses gw.lang.reflect.IExpando
uses gw.lang.reflect.json.Json
uses java.util.Map
uses java.util.HashMap
uses java.math.BigInteger
uses java.math.BigDecimal


class JsonTest extends gw.BaseVerifyErrantTest {
  var _yahoo_finance_quotes_json: String =
      "{\n" +
      " \"query\": {\n" +
      "  \"count\": 4,\n" +
      "  \"created\": \"2016-02-08T08:28:22Z\",\n" +
      "  \"lang\": \"en-US\",\n" +
      "  \"results\": {\n" +
      "   \"quote\": [\n" +
      "    {\n" +
      "     \"symbol\": \"YHOO\",\n" +
      "     \"Ask\": \"28.26\",\n" +
      "     \"AverageDailyVolume\": \"16940500\",\n" +
      "     \"Bid\": \"27.32\",\n" +
      "     \"AskRealtime\": null,\n" +
      "     \"BidRealtime\": null,\n" +
      "     \"BookValue\": \"30.78\",\n" +
      "     \"Change_PercentChange\": \"-1.18 - -4.05%\",\n" +
      "     \"Change\": \"-1.18\",\n" +
      "     \"Commission\": null,\n" +
      "     \"Currency\": \"USD\",\n" +
      "     \"ChangeRealtime\": null,\n" +
      "     \"AfterHoursChangeRealtime\": null,\n" +
      "     \"DividendShare\": null,\n" +
      "     \"LastTradeDate\": \"2/5/2016\",\n" +
      "     \"TradeDate\": null,\n" +
      "     \"EarningsShare\": \"-4.64\",\n" +
      "     \"ErrorIndicationreturnedforsymbolchangedinvalid\": null,\n" +
      "     \"EPSEstimateCurrentYear\": \"0.54\",\n" +
      "     \"EPSEstimateNextYear\": \"0.62\",\n" +
      "     \"EPSEstimateNextQuarter\": \"0.12\",\n" +
      "     \"DaysLow\": \"27.73\",\n" +
      "     \"DaysHigh\": \"29.14\",\n" +
      "     \"YearLow\": \"26.57\",\n" +
      "     \"YearHigh\": \"46.17\",\n" +
      "     \"HoldingsGainPercent\": null,\n" +
      "     \"AnnualizedGain\": null,\n" +
      "     \"HoldingsGain\": null,\n" +
      "     \"HoldingsGainPercentRealtime\": null,\n" +
      "     \"HoldingsGainRealtime\": null,\n" +
      "     \"MoreInfo\": null,\n" +
      "     \"OrderBookRealtime\": null,\n" +
      "     \"MarketCapitalization\": \"26.39B\",\n" +
      "     \"MarketCapRealtime\": null,\n" +
      "     \"EBITDA\": \"474.68M\",\n" +
      "     \"ChangeFromYearLow\": \"1.40\",\n" +
      "     \"PercentChangeFromYearLow\": \"+5.27%\",\n" +
      "     \"LastTradeRealtimeWithTime\": null,\n" +
      "     \"ChangePercentRealtime\": null,\n" +
      "     \"ChangeFromYearHigh\": \"-18.20\",\n" +
      "     \"PercebtChangeFromYearHigh\": \"-39.42%\",\n" +
      "     \"LastTradeWithTime\": \"4:00pm - <b>27.97</b>\",\n" +
      "     \"LastTradePriceOnly\": \"27.97\",\n" +
      "     \"HighLimit\": null,\n" +
      "     \"LowLimit\": null,\n" +
      "     \"DaysRange\": \"27.73 - 29.14\",\n" +
      "     \"DaysRangeRealtime\": null,\n" +
      "     \"FiftydayMovingAverage\": \"30.85\",\n" +
      "     \"TwoHundreddayMovingAverage\": \"32.87\",\n" +
      "     \"ChangeFromTwoHundreddayMovingAverage\": \"-4.90\",\n" +
      "     \"PercentChangeFromTwoHundreddayMovingAverage\": \"-14.91%\",\n" +
      "     \"ChangeFromFiftydayMovingAverage\": \"-2.88\",\n" +
      "     \"PercentChangeFromFiftydayMovingAverage\": \"-9.33%\",\n" +
      "     \"Name\": \"Yahoo! Inc.\",\n" +
      "     \"Notes\": null,\n" +
      "     \"Open\": \"29.13\",\n" +
      "     \"PreviousClose\": \"29.15\",\n" +
      "     \"PricePaid\": null,\n" +
      "     \"ChangeinPercent\": \"-4.05%\",\n" +
      "     \"PriceSales\": \"5.54\",\n" +
      "     \"PriceBook\": \"0.95\",\n" +
      "     \"ExDividendDate\": null,\n" +
      "     \"PERatio\": null,\n" +
      "     \"DividendPayDate\": null,\n" +
      "     \"PERatioRealtime\": null,\n" +
      "     \"PEGRatio\": \"-6.99\",\n" +
      "     \"PriceEPSEstimateCurrentYear\": \"51.80\",\n" +
      "     \"PriceEPSEstimateNextYear\": \"45.11\",\n" +
      "     \"Symbol\": \"YHOO\",\n" +
      "     \"SharesOwned\": null,\n" +
      "     \"ShortRatio\": \"4.48\",\n" +
      "     \"LastTradeTime\": \"4:00pm\",\n" +
      "     \"TickerTrend\": null,\n" +
      "     \"OneyrTargetPrice\": \"37.61\",\n" +
      "     \"Volume\": \"16379585\",\n" +
      "     \"HoldingsValue\": null,\n" +
      "     \"HoldingsValueRealtime\": null,\n" +
      "     \"YearRange\": \"26.57 - 46.17\",\n" +
      "     \"DaysValueChange\": null,\n" +
      "     \"DaysValueChangeRealtime\": null,\n" +
      "     \"StockExchange\": \"NMS\",\n" +
      "     \"DividendYield\": null,\n" +
      "     \"PercentChange\": \"-4.05%\"\n" +
      "    },\n" +
      "    {\n" +
      "     \"symbol\": \"AAPL\",\n" +
      "     \"Ask\": \"93.99\",\n" +
      "     \"AverageDailyVolume\": \"48363100\",\n" +
      "     \"Bid\": \"93.90\",\n" +
      "     \"AskRealtime\": null,\n" +
      "     \"BidRealtime\": null,\n" +
      "     \"BookValue\": \"23.13\",\n" +
      "     \"Change_PercentChange\": \"-2.58 - -2.67%\",\n" +
      "     \"Change\": \"-2.58\",\n" +
      "     \"Commission\": null,\n" +
      "     \"Currency\": \"USD\",\n" +
      "     \"ChangeRealtime\": null,\n" +
      "     \"AfterHoursChangeRealtime\": null,\n" +
      "     \"DividendShare\": \"2.08\",\n" +
      "     \"LastTradeDate\": \"2/5/2016\",\n" +
      "     \"TradeDate\": null,\n" +
      "     \"EarningsShare\": \"9.40\",\n" +
      "     \"ErrorIndicationreturnedforsymbolchangedinvalid\": null,\n" +
      "     \"EPSEstimateCurrentYear\": \"9.08\",\n" +
      "     \"EPSEstimateNextYear\": \"10.02\",\n" +
      "     \"EPSEstimateNextQuarter\": \"1.77\",\n" +
      "     \"DaysLow\": \"93.69\",\n" +
      "     \"DaysHigh\": \"96.92\",\n" +
      "     \"YearLow\": \"92.00\",\n" +
      "     \"YearHigh\": \"134.54\",\n" +
      "     \"HoldingsGainPercent\": null,\n" +
      "     \"AnnualizedGain\": null,\n" +
      "     \"HoldingsGain\": null,\n" +
      "     \"HoldingsGainPercentRealtime\": null,\n" +
      "     \"HoldingsGainRealtime\": null,\n" +
      "     \"MoreInfo\": null,\n" +
      "     \"OrderBookRealtime\": null,\n" +
      "     \"MarketCapitalization\": \"521.30B\",\n" +
      "     \"MarketCapRealtime\": null,\n" +
      "     \"EBITDA\": \"82.79B\",\n" +
      "     \"ChangeFromYearLow\": \"2.02\",\n" +
      "     \"PercentChangeFromYearLow\": \"+2.20%\",\n" +
      "     \"LastTradeRealtimeWithTime\": null,\n" +
      "     \"ChangePercentRealtime\": null,\n" +
      "     \"ChangeFromYearHigh\": \"-40.52\",\n" +
      "     \"PercebtChangeFromYearHigh\": \"-30.12%\",\n" +
      "     \"LastTradeWithTime\": \"4:00pm - <b>94.02</b>\",\n" +
      "     \"LastTradePriceOnly\": \"94.02\",\n" +
      "     \"HighLimit\": null,\n" +
      "     \"LowLimit\": null,\n" +
      "     \"DaysRange\": \"93.69 - 96.92\",\n" +
      "     \"DaysRangeRealtime\": null,\n" +
      "     \"FiftydayMovingAverage\": \"100.41\",\n" +
      "     \"TwoHundreddayMovingAverage\": \"111.74\",\n" +
      "     \"ChangeFromTwoHundreddayMovingAverage\": \"-17.72\",\n" +
      "     \"PercentChangeFromTwoHundreddayMovingAverage\": \"-15.86%\",\n" +
      "     \"ChangeFromFiftydayMovingAverage\": \"-6.39\",\n" +
      "     \"PercentChangeFromFiftydayMovingAverage\": \"-6.36%\",\n" +
      "     \"Name\": \"Apple Inc.\",\n" +
      "     \"Notes\": null,\n" +
      "     \"Open\": \"96.51\",\n" +
      "     \"PreviousClose\": \"96.60\",\n" +
      "     \"PricePaid\": null,\n" +
      "     \"ChangeinPercent\": \"-2.67%\",\n" +
      "     \"PriceSales\": \"2.28\",\n" +
      "     \"PriceBook\": \"4.18\",\n" +
      "     \"ExDividendDate\": \"2/4/2016\",\n" +
      "     \"PERatio\": \"10.01\",\n" +
      "     \"DividendPayDate\": \"2/11/2016\",\n" +
      "     \"PERatioRealtime\": null,\n" +
      "     \"PEGRatio\": \"0.87\",\n" +
      "     \"PriceEPSEstimateCurrentYear\": \"10.35\",\n" +
      "     \"PriceEPSEstimateNextYear\": \"9.38\",\n" +
      "     \"Symbol\": \"AAPL\",\n" +
      "     \"SharesOwned\": null,\n" +
      "     \"ShortRatio\": \"1.26\",\n" +
      "     \"LastTradeTime\": \"4:00pm\",\n" +
      "     \"TickerTrend\": null,\n" +
      "     \"OneyrTargetPrice\": \"136.25\",\n" +
      "     \"Volume\": \"46418064\",\n" +
      "     \"HoldingsValue\": null,\n" +
      "     \"HoldingsValueRealtime\": null,\n" +
      "     \"YearRange\": \"92.00 - 134.54\",\n" +
      "     \"DaysValueChange\": null,\n" +
      "     \"DaysValueChangeRealtime\": null,\n" +
      "     \"StockExchange\": \"NMS\",\n" +
      "     \"DividendYield\": \"2.21\",\n" +
      "     \"PercentChange\": \"-2.67%\"\n" +
      "    },\n" +
      "    {\n" +
      "     \"symbol\": \"GOOG\",\n" +
      "     \"Ask\": \"682.50\",\n" +
      "     \"AverageDailyVolume\": \"2330210\",\n" +
      "     \"Bid\": \"680.50\",\n" +
      "     \"AskRealtime\": null,\n" +
      "     \"BidRealtime\": null,\n" +
      "     \"BookValue\": \"175.07\",\n" +
      "     \"Change_PercentChange\": \"-24.44 - -3.45%\",\n" +
      "     \"Change\": \"-24.44\",\n" +
      "     \"Commission\": null,\n" +
      "     \"Currency\": \"USD\",\n" +
      "     \"ChangeRealtime\": null,\n" +
      "     \"AfterHoursChangeRealtime\": null,\n" +
      "     \"DividendShare\": null,\n" +
      "     \"LastTradeDate\": \"2/5/2016\",\n" +
      "     \"TradeDate\": null,\n" +
      "     \"EarningsShare\": \"23.78\",\n" +
      "     \"ErrorIndicationreturnedforsymbolchangedinvalid\": null,\n" +
      "     \"EPSEstimateCurrentYear\": \"34.53\",\n" +
      "     \"EPSEstimateNextYear\": \"40.25\",\n" +
      "     \"EPSEstimateNextQuarter\": \"8.19\",\n" +
      "     \"DaysLow\": \"680.15\",\n" +
      "     \"DaysHigh\": \"703.99\",\n" +
      "     \"YearLow\": \"515.18\",\n" +
      "     \"YearHigh\": \"789.87\",\n" +
      "     \"HoldingsGainPercent\": null,\n" +
      "     \"AnnualizedGain\": null,\n" +
      "     \"HoldingsGain\": null,\n" +
      "     \"HoldingsGainPercentRealtime\": null,\n" +
      "     \"HoldingsGainRealtime\": null,\n" +
      "     \"MoreInfo\": null,\n" +
      "     \"OrderBookRealtime\": null,\n" +
      "     \"MarketCapitalization\": \"469.85B\",\n" +
      "     \"MarketCapRealtime\": null,\n" +
      "     \"EBITDA\": \"24.42B\",\n" +
      "     \"ChangeFromYearLow\": \"168.39\",\n" +
      "     \"PercentChangeFromYearLow\": \"+32.69%\",\n" +
      "     \"LastTradeRealtimeWithTime\": null,\n" +
      "     \"ChangePercentRealtime\": null,\n" +
      "     \"ChangeFromYearHigh\": \"-106.30\",\n" +
      "     \"PercebtChangeFromYearHigh\": \"-13.46%\",\n" +
      "     \"LastTradeWithTime\": \"4:00pm - <b>683.57</b>\",\n" +
      "     \"LastTradePriceOnly\": \"683.57\",\n" +
      "     \"HighLimit\": null,\n" +
      "     \"LowLimit\": null,\n" +
      "     \"DaysRange\": \"680.15 - 703.99\",\n" +
      "     \"DaysRangeRealtime\": null,\n" +
      "     \"FiftydayMovingAverage\": \"730.04\",\n" +
      "     \"TwoHundreddayMovingAverage\": \"685.60\",\n" +
      "     \"ChangeFromTwoHundreddayMovingAverage\": \"-2.03\",\n" +
      "     \"PercentChangeFromTwoHundreddayMovingAverage\": \"-0.30%\",\n" +
      "     \"ChangeFromFiftydayMovingAverage\": \"-46.47\",\n" +
      "     \"PercentChangeFromFiftydayMovingAverage\": \"-6.37%\",\n" +
      "     \"Name\": \"Alphabet Inc.\",\n" +
      "     \"Notes\": null,\n" +
      "     \"Open\": \"703.87\",\n" +
      "     \"PreviousClose\": \"708.01\",\n" +
      "     \"PricePaid\": null,\n" +
      "     \"ChangeinPercent\": \"-3.45%\",\n" +
      "     \"PriceSales\": \"6.49\",\n" +
      "     \"PriceBook\": \"4.04\",\n" +
      "     \"ExDividendDate\": null,\n" +
      "     \"PERatio\": \"28.74\",\n" +
      "     \"DividendPayDate\": null,\n" +
      "     \"PERatioRealtime\": null,\n" +
      "     \"PEGRatio\": \"1.19\",\n" +
      "     \"PriceEPSEstimateCurrentYear\": \"19.80\",\n" +
      "     \"PriceEPSEstimateNextYear\": \"16.98\",\n" +
      "     \"Symbol\": \"GOOG\",\n" +
      "     \"SharesOwned\": null,\n" +
      "     \"ShortRatio\": \"1.63\",\n" +
      "     \"LastTradeTime\": \"4:00pm\",\n" +
      "     \"TickerTrend\": null,\n" +
      "     \"OneyrTargetPrice\": \"924.42\",\n" +
      "     \"Volume\": \"5105725\",\n" +
      "     \"HoldingsValue\": null,\n" +
      "     \"HoldingsValueRealtime\": null,\n" +
      "     \"YearRange\": \"515.18 - 789.87\",\n" +
      "     \"DaysValueChange\": null,\n" +
      "     \"DaysValueChangeRealtime\": null,\n" +
      "     \"StockExchange\": \"NMS\",\n" +
      "     \"DividendYield\": null,\n" +
      "     \"PercentChange\": \"-3.45%\"\n" +
      "    },\n" +
      "    {\n" +
      "     \"symbol\": \"MSFT\",\n" +
      "     \"Ask\": \"50.20\",\n" +
      "     \"AverageDailyVolume\": \"40536600\",\n" +
      "     \"Bid\": \"50.10\",\n" +
      "     \"AskRealtime\": null,\n" +
      "     \"BidRealtime\": null,\n" +
      "     \"BookValue\": \"9.69\",\n" +
      "     \"Change_PercentChange\": \"-1.84 - -3.54%\",\n" +
      "     \"Change\": \"-1.84\",\n" +
      "     \"Commission\": null,\n" +
      "     \"Currency\": \"USD\",\n" +
      "     \"ChangeRealtime\": null,\n" +
      "     \"AfterHoursChangeRealtime\": null,\n" +
      "     \"DividendShare\": \"1.44\",\n" +
      "     \"LastTradeDate\": \"2/5/2016\",\n" +
      "     \"TradeDate\": null,\n" +
      "     \"EarningsShare\": \"1.41\",\n" +
      "     \"ErrorIndicationreturnedforsymbolchangedinvalid\": null,\n" +
      "     \"EPSEstimateCurrentYear\": \"2.76\",\n" +
      "     \"EPSEstimateNextYear\": \"3.07\",\n" +
      "     \"EPSEstimateNextQuarter\": \"0.67\",\n" +
      "     \"DaysLow\": \"49.56\",\n" +
      "     \"DaysHigh\": \"52.00\",\n" +
      "     \"YearLow\": \"39.72\",\n" +
      "     \"YearHigh\": \"56.85\",\n" +
      "     \"HoldingsGainPercent\": null,\n" +
      "     \"AnnualizedGain\": null,\n" +
      "     \"HoldingsGain\": null,\n" +
      "     \"HoldingsGainPercentRealtime\": null,\n" +
      "     \"HoldingsGainRealtime\": null,\n" +
      "     \"MoreInfo\": null,\n" +
      "     \"OrderBookRealtime\": null,\n" +
      "     \"MarketCapitalization\": \"396.73B\",\n" +
      "     \"MarketCapRealtime\": null,\n" +
      "     \"EBITDA\": \"30.46B\",\n" +
      "     \"ChangeFromYearLow\": \"10.44\",\n" +
      "     \"PercentChangeFromYearLow\": \"+26.28%\",\n" +
      "     \"LastTradeRealtimeWithTime\": null,\n" +
      "     \"ChangePercentRealtime\": null,\n" +
      "     \"ChangeFromYearHigh\": \"-6.69\",\n" +
      "     \"PercebtChangeFromYearHigh\": \"-11.77%\",\n" +
      "     \"LastTradeWithTime\": \"3:59pm - <b>50.16</b>\",\n" +
      "     \"LastTradePriceOnly\": \"50.16\",\n" +
      "     \"HighLimit\": null,\n" +
      "     \"LowLimit\": null,\n" +
      "     \"DaysRange\": \"49.56 - 52.00\",\n" +
      "     \"DaysRangeRealtime\": null,\n" +
      "     \"FiftydayMovingAverage\": \"53.27\",\n" +
      "     \"TwoHundreddayMovingAverage\": \"49.72\",\n" +
      "     \"ChangeFromTwoHundreddayMovingAverage\": \"0.44\",\n" +
      "     \"PercentChangeFromTwoHundreddayMovingAverage\": \"+0.88%\",\n" +
      "     \"ChangeFromFiftydayMovingAverage\": \"-3.11\",\n" +
      "     \"PercentChangeFromFiftydayMovingAverage\": \"-5.83%\",\n" +
      "     \"Name\": \"Microsoft Corporation\",\n" +
      "     \"Notes\": null,\n" +
      "     \"Open\": \"51.94\",\n" +
      "     \"PreviousClose\": \"52.00\",\n" +
      "     \"PricePaid\": null,\n" +
      "     \"ChangeinPercent\": \"-3.54%\",\n" +
      "     \"PriceSales\": \"4.67\",\n" +
      "     \"PriceBook\": \"5.37\",\n" +
      "     \"ExDividendDate\": \"11/17/2015\",\n" +
      "     \"PERatio\": \"35.50\",\n" +
      "     \"DividendPayDate\": \"3/10/2016\",\n" +
      "     \"PERatioRealtime\": null,\n" +
      "     \"PEGRatio\": \"1.91\",\n" +
      "     \"PriceEPSEstimateCurrentYear\": \"18.17\",\n" +
      "     \"PriceEPSEstimateNextYear\": \"16.34\",\n" +
      "     \"Symbol\": \"MSFT\",\n" +
      "     \"SharesOwned\": null,\n" +
      "     \"ShortRatio\": \"1.08\",\n" +
      "     \"LastTradeTime\": \"3:59pm\",\n" +
      "     \"TickerTrend\": null,\n" +
      "     \"OneyrTargetPrice\": \"58.89\",\n" +
      "     \"Volume\": \"62008994\",\n" +
      "     \"HoldingsValue\": null,\n" +
      "     \"HoldingsValueRealtime\": null,\n" +
      "     \"YearRange\": \"39.72 - 56.85\",\n" +
      "     \"DaysValueChange\": null,\n" +
      "     \"DaysValueChangeRealtime\": null,\n" +
      "     \"StockExchange\": \"NMS\",\n" +
      "     \"DividendYield\": \"2.87\",\n" +
      "     \"PercentChange\": \"-3.54%\"\n" +
      "    }\n" +
      "   ]\n" +
      "  }\n" +
      " }\n" +
      "}"
        
  var _structure: String =
      "structure YahooQuotes {\n" +
      "  static function fromJson( jsonText: String ): YahooQuotes {\n" +
      "    return gw.lang.reflect.json.Json.fromJson( jsonText ) as YahooQuotes\n" +
      "  }\n" +
      "  static function fromJsonUrl( url: String ): YahooQuotes {\n" +
      "    return new java.net.URL( url ).JsonContent\n" +
      "  }\n" +
      "  static function fromJsonUrl( url: java.net.URL ): YahooQuotes {\n" +
      "    return url.JsonContent\n" +
      "  }\n" +
      "  static function fromJsonFile( file: java.io.File ) : YahooQuotes {\n" +
      "    return fromJsonUrl( file.toURI().toURL() )\n" +
      "  }\n" +
      "  property get query(): query\n" +
      "  property set query( $value: query )\n" +
      "  structure query {\n" +
      "    property get created(): String\n" +
      "    property set created( $value: String )\n" +
      "    property get count(): Integer\n" +
      "    property set count( $value: Integer )\n" +
      "    property get lang(): String\n" +
      "    property set lang( $value: String )\n" +
      "    property get results(): results\n" +
      "    property set results( $value: results )\n" +
      "    structure results {\n" +
      "      property get quote(): List<quote>\n" +
      "      property set quote( $value: List<quote> )\n" +
      "      structure quote {\n" +
      "        property get HoldingsGainPercent(): Dynamic\n" +
      "        property set HoldingsGainPercent( $value: Dynamic )\n" +
      "        property get symbol(): String\n" +
      "        property set symbol( $value: String )\n" +
      "        property get PriceEPSEstimateNextYear(): String\n" +
      "        property set PriceEPSEstimateNextYear( $value: String )\n" +
      "        property get ChangeinPercent(): String\n" +
      "        property set ChangeinPercent( $value: String )\n" +
      "        property get EPSEstimateNextQuarter(): String\n" +
      "        property set EPSEstimateNextQuarter( $value: String )\n" +
      "        property get DaysValueChange(): Dynamic\n" +
      "        property set DaysValueChange( $value: Dynamic )\n" +
      "        property get HoldingsValue(): Dynamic\n" +
      "        property set HoldingsValue( $value: Dynamic )\n" +
      "        property get EBITDA(): String\n" +
      "        property set EBITDA( $value: String )\n" +
      "        property get PriceBook(): String\n" +
      "        property set PriceBook( $value: String )\n" +
      "        property get Commission(): Dynamic\n" +
      "        property set Commission( $value: Dynamic )\n" +
      "        property get DividendShare(): String\n" +
      "        property set DividendShare( $value: String )\n" +
      "        property get PriceSales(): String\n" +
      "        property set PriceSales( $value: String )\n" +
      "        property get MoreInfo(): Dynamic\n" +
      "        property set MoreInfo( $value: Dynamic )\n" +
      "        property get PercebtChangeFromYearHigh(): String\n" +
      "        property set PercebtChangeFromYearHigh( $value: String )\n" +
      "        property get ExDividendDate(): String\n" +
      "        property set ExDividendDate( $value: String )\n" +
      "        property get HighLimit(): Dynamic\n" +
      "        property set HighLimit( $value: Dynamic )\n" +
      "        property get PreviousClose(): String\n" +
      "        property set PreviousClose( $value: String )\n" +
      "        property get Name(): String\n" +
      "        property set Name( $value: String )\n" +
      "        property get PERatioRealtime(): Dynamic\n" +
      "        property set PERatioRealtime( $value: Dynamic )\n" +
      "        property get YearHigh(): String\n" +
      "        property set YearHigh( $value: String )\n" +
      "        property get AnnualizedGain(): Dynamic\n" +
      "        property set AnnualizedGain( $value: Dynamic )\n" +
      "        property get HoldingsGain(): Dynamic\n" +
      "        property set HoldingsGain( $value: Dynamic )\n" +
      "        property get Currency(): String\n" +
      "        property set Currency( $value: String )\n" +
      "        property get ChangePercentRealtime(): Dynamic\n" +
      "        property set ChangePercentRealtime( $value: Dynamic )\n" +
      "        property get DividendPayDate(): String\n" +
      "        property set DividendPayDate( $value: String )\n" +
      "        property get LastTradeDate(): String\n" +
      "        property set LastTradeDate( $value: String )\n" +
      "        property get PriceEPSEstimateCurrentYear(): String\n" +
      "        property set PriceEPSEstimateCurrentYear( $value: String )\n" +
      "        property get BidRealtime(): Dynamic\n" +
      "        property set BidRealtime( $value: Dynamic )\n" +
      "        property get StockExchange(): String\n" +
      "        property set StockExchange( $value: String )\n" +
      "        property get SharesOwned(): Dynamic\n" +
      "        property set SharesOwned( $value: Dynamic )\n" +
      "        property get AverageDailyVolume(): String\n" +
      "        property set AverageDailyVolume( $value: String )\n" +
      "        property get PercentChange(): String\n" +
      "        property set PercentChange( $value: String )\n" +
      "        property get ChangeFromYearHigh(): String\n" +
      "        property set ChangeFromYearHigh( $value: String )\n" +
      "        property get FiftydayMovingAverage(): String\n" +
      "        property set FiftydayMovingAverage( $value: String )\n" +
      "        property get AskRealtime(): Dynamic\n" +
      "        property set AskRealtime( $value: Dynamic )\n" +
      "        property get TwoHundreddayMovingAverage(): String\n" +
      "        property set TwoHundreddayMovingAverage( $value: String )\n" +
      "        property get PercentChangeFromTwoHundreddayMovingAverage(): String\n" +
      "        property set PercentChangeFromTwoHundreddayMovingAverage( $value: String )\n" +
      "        property get PricePaid(): Dynamic\n" +
      "        property set PricePaid( $value: Dynamic )\n" +
      "        property get LastTradeTime(): String\n" +
      "        property set LastTradeTime( $value: String )\n" +
      "        property get Volume(): String\n" +
      "        property set Volume( $value: String )\n" +
      "        property get OrderBookRealtime(): Dynamic\n" +
      "        property set OrderBookRealtime( $value: Dynamic )\n" +
      "        property get MarketCapitalization(): String\n" +
      "        property set MarketCapitalization( $value: String )\n" +
      "        property get LastTradeRealtimeWithTime(): Dynamic\n" +
      "        property set LastTradeRealtimeWithTime( $value: Dynamic )\n" +
      "        property get PercentChangeFromYearLow(): String\n" +
      "        property set PercentChangeFromYearLow( $value: String )\n" +
      "        property get DaysHigh(): String\n" +
      "        property set DaysHigh( $value: String )\n" +
      "        property get AfterHoursChangeRealtime(): Dynamic\n" +
      "        property set AfterHoursChangeRealtime( $value: Dynamic )\n" +
      "        property get DaysRange(): String\n" +
      "        property set DaysRange( $value: String )\n" +
      "        property get Symbol(): String\n" +
      "        property set Symbol( $value: String )\n" +
      "        property get EPSEstimateNextYear(): String\n" +
      "        property set EPSEstimateNextYear( $value: String )\n" +
      "        property get HoldingsGainRealtime(): Dynamic\n" +
      "        property set HoldingsGainRealtime( $value: Dynamic )\n" +
      "        property get PercentChangeFromFiftydayMovingAverage(): String\n" +
      "        property set PercentChangeFromFiftydayMovingAverage( $value: String )\n" +
      "        property get Open(): String\n" +
      "        property set Open( $value: String )\n" +
      "        property get ChangeRealtime(): Dynamic\n" +
      "        property set ChangeRealtime( $value: Dynamic )\n" +
      "        property get DaysLow(): String\n" +
      "        property set DaysLow( $value: String )\n" +
      "        property get DividendYield(): String\n" +
      "        property set DividendYield( $value: String )\n" +
      "        property get Ask(): String\n" +
      "        property set Ask( $value: String )\n" +
      "        property get EPSEstimateCurrentYear(): String\n" +
      "        property set EPSEstimateCurrentYear( $value: String )\n" +
      "        property get YearLow(): String\n" +
      "        property set YearLow( $value: String )\n" +
      "        property get MarketCapRealtime(): Dynamic\n" +
      "        property set MarketCapRealtime( $value: Dynamic )\n" +
      "        property get TradeDate(): Dynamic\n" +
      "        property set TradeDate( $value: Dynamic )\n" +
      "        property get ChangeFromFiftydayMovingAverage(): String\n" +
      "        property set ChangeFromFiftydayMovingAverage( $value: String )\n" +
      "        property get Bid(): String\n" +
      "        property set Bid( $value: String )\n" +
      "        property get LastTradeWithTime(): String\n" +
      "        property set LastTradeWithTime( $value: String )\n" +
      "        property get ErrorIndicationreturnedforsymbolchangedinvalid(): Dynamic\n" +
      "        property set ErrorIndicationreturnedforsymbolchangedinvalid( $value: Dynamic )\n" +
      "        property get Notes(): Dynamic\n" +
      "        property set Notes( $value: Dynamic )\n" +
      "        property get DaysValueChangeRealtime(): Dynamic\n" +
      "        property set DaysValueChangeRealtime( $value: Dynamic )\n" +
      "        property get TickerTrend(): Dynamic\n" +
      "        property set TickerTrend( $value: Dynamic )\n" +
      "        property get DaysRangeRealtime(): Dynamic\n" +
      "        property set DaysRangeRealtime( $value: Dynamic )\n" +
      "        property get ShortRatio(): String\n" +
      "        property set ShortRatio( $value: String )\n" +
      "        property get Change(): String\n" +
      "        property set Change( $value: String )\n" +
      "        property get ChangeFromTwoHundreddayMovingAverage(): String\n" +
      "        property set ChangeFromTwoHundreddayMovingAverage( $value: String )\n" +
      "        property get EarningsShare(): String\n" +
      "        property set EarningsShare( $value: String )\n" +
      "        property get BookValue(): String\n" +
      "        property set BookValue( $value: String )\n" +
      "        property get ChangeFromYearLow(): String\n" +
      "        property set ChangeFromYearLow( $value: String )\n" +
      "        property get OneyrTargetPrice(): String\n" +
      "        property set OneyrTargetPrice( $value: String )\n" +
      "        property get PERatio(): String\n" +
      "        property set PERatio( $value: String )\n" +
      "        property get YearRange(): String\n" +
      "        property set YearRange( $value: String )\n" +
      "        property get LowLimit(): Dynamic\n" +
      "        property set LowLimit( $value: Dynamic )\n" +
      "        property get HoldingsValueRealtime(): Dynamic\n" +
      "        property set HoldingsValueRealtime( $value: Dynamic )\n" +
      "        property get Change_PercentChange(): String\n" +
      "        property set Change_PercentChange( $value: String )\n" +
      "        property get PEGRatio(): String\n" +
      "        property set PEGRatio( $value: String )\n" +
      "        property get HoldingsGainPercentRealtime(): Dynamic\n" +
      "        property set HoldingsGainPercentRealtime( $value: Dynamic )\n" +
      "        property get LastTradePriceOnly(): String\n" +
      "        property set LastTradePriceOnly( $value: String )\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}\n";

  var _amazon_ecs_json: String =
       "{\n" +
      " 'query': {\n" +
      "  'count': 1,\n" +
      "  'created': '2016-02-08T20:19:10Z',\n" +
      "  'lang': 'en-US',\n" +
      "  'results': {\n" +
      "   'table': {\n" +
      "    'hash': '750e8c9e5eebaeb61e4af3b71ee2922e',\n" +
      "    'name': 'amazon.ecs',\n" +
      "    'security': 'ANY',\n" +
      "    'src': 'http://www.datatables.org/amazon/amazon.ecs.xml',\n" +
      "    'meta': {\n" +
      "     'author': 'Nagesh Susarla',\n" +
      "     'documentationURL': 'http://docs.amazonwebservices.com/AWSECommerceService/2009-01-06/GSG/\\n        '\n" +
      "    },\n" +
      "    'request': {\n" +
      "     'select': [\n" +
      "      {\n" +
      "       'usesRemoteLimit': 'true',\n" +
      "       'key': [\n" +
      "        {\n" +
      "         'default': 'ItemSearch',\n" +
      "         'name': 'Operation',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'Title',\n" +
      "         'required': 'true',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'AWSAccessKeyId',\n" +
      "         'required': 'true',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'secret',\n" +
      "         'required': 'true',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'default': 'Books',\n" +
      "         'name': 'SearchIndex',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'ResponseGroup',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'AssociateTag',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'Sort',\n" +
      "         'type': 'xs:string'\n" +
      "        }\n" +
      "       ]\n" +
      "      },\n" +
      "      {\n" +
      "       'usesRemoteLimit': 'true',\n" +
      "       'key': [\n" +
      "        {\n" +
      "         'default': 'ItemSearch',\n" +
      "         'name': 'Operation',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'Keywords',\n" +
      "         'required': 'true',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'AWSAccessKeyId',\n" +
      "         'required': 'true',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'secret',\n" +
      "         'required': 'true',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'default': 'Books',\n" +
      "         'name': 'SearchIndex',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'ResponseGroup',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'AssociateTag',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'Sort',\n" +
      "         'type': 'xs:string'\n" +
      "        }\n" +
      "       ]\n" +
      "      },\n" +
      "      {\n" +
      "       'usesRemoteLimit': 'true',\n" +
      "       'key': [\n" +
      "        {\n" +
      "         'default': 'ItemLookup',\n" +
      "         'name': 'Operation',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'ItemId',\n" +
      "         'required': 'true',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'MerchantId',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'AWSAccessKeyId',\n" +
      "         'required': 'true',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'secret',\n" +
      "         'required': 'true',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'ResponseGroup',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'AssociateTag',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'name': 'Sort',\n" +
      "         'type': 'xs:string'\n" +
      "        },\n" +
      "        {\n" +
      "         'default': '2009-03-31',\n" +
      "         'name': 'Version',\n" +
      "         'type': 'xs:string'\n" +
      "        }\n" +
      "       ]\n" +
      "      }\n" +
      "     ]\n" +
      "    }\n" +
      "   }\n" +
      "  }\n" +
      " }\n" +
      "}"

  var _amazon_ecs_structure: String =
      "structure Amazon {\n" +
      "  static function fromJson( jsonText: String ): Amazon {\n" +
      "    return gw.lang.reflect.json.Json.fromJson( jsonText ) as Amazon\n" +
      "  }\n" +
      "  static function fromJsonUrl( url: String ): Amazon {\n" +
      "    return new java.net.URL( url ).JsonContent\n" +
      "  }\n" +
      "  static function fromJsonUrl( url: java.net.URL ): Amazon {\n" +
      "    return url.JsonContent\n" +
      "  }\n" +
      "  static function fromJsonFile( file: java.io.File ) : Amazon {\n" +
      "    return fromJsonUrl( file.toURI().toURL() )\n" +
      "  }\n" +
      "  property get query(): query\n" +
      "  structure query {\n" +
      "    property get created(): String\n" +
      "    property get count(): Integer\n" +
      "    property get lang(): String\n" +
      "    property get results(): results\n" +
      "    structure results {\n" +
      "      property get table(): table\n" +
      "      structure table {\n" +
      "        property get request(): request\n" +
      "        property get security(): String\n" +
      "        property get src(): String\n" +
      "        property get meta(): meta\n" +
      "        property get name(): String\n" +
      "        property get hash(): String\n" +
      "        structure request {\n" +
      "          property get select(): List<select>\n" +
      "          structure select {\n" +
      "            property get usesRemoteLimit(): String\n" +
      "            property get key(): List<key>\n" +
      "            structure key {\n" +
      "              @gw.lang.reflect.ActualName( \"default\" )\n" +
      "              property get Default(): String\n" +
      "              property get name(): String\n" +
      "              property get type(): String\n" +
      "              property get required(): String\n" +
      "            }\n" +
      "          }\n" +
      "        }\n" +
      "        structure meta {\n" +
      "          property get documentationURL(): String\n" +
      "          property get author(): String\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}\n"

  function testStructureFromJson() {
    var results: Dynamic = Json.fromJson( _yahoo_finance_quotes_json )
    var structr = results.toStructure( "YahooQuotes", true )
    assertEquals( _structure, structr )

    var structureResults = YahooQuotes.fromJson( _yahoo_finance_quotes_json )
    for( stock in structureResults.query.results.quote ) {
      var symbol = stock.Symbol
      var close = stock.Ask
      var change = stock.Change
      var volume = stock.Volume
      print( "${symbol} ${close} ${change} ${volume}" )
    }
  }

  function testStructureFromJson_AmazonEcs() {
    var results: Dynamic = Json.fromJson( _amazon_ecs_json )
    var structr = results.toStructure( "Amazon", false )
    assertEquals( _amazon_ecs_structure, structr )

    var amazon = Amazon.fromJson( _amazon_ecs_json )
    var output: String = ""
    for( sel in amazon.query.results.table.request.select )
    {
      for( k in sel.key )
      {
        output += k.Default + "\n"
      }
    }
    var expectedOutput =
      "ItemSearch\n" +
      "null\n" +
      "null\n" +
      "null\n" +
      "Books\n" +
      "null\n" +
      "null\n" +
      "null\n" +
      "ItemSearch\n" +
      "null\n" +
      "null\n" +
      "null\n" +
      "Books\n" +
      "null\n" +
      "null\n" +
      "null\n" +
      "ItemLookup\n" +
      "null\n" +
      "null\n" +
      "null\n" +
      "null\n" +
      "null\n" +
      "null\n" +
      "null\n" +
      "2009-03-31\n"

    assertEquals( expectedOutput, output )
  }

  function testTopLevelList() {
    var x: Dynamic = Json.fromJson( "[{'a': 1}, {'b': 2}]" )
    var list: TopLevelList = x

    // Note the coelescing of the list's component type "value", it is the union of both value, hence both members 'a' and 'b'
    assertEquals( 1, list.value[0].a )
    assertEquals( null, list.value[0].b )
    assertEquals( null, list.value[1].a )
    assertEquals( 2, list.value[1].b )
  }

  structure TopLevelList {
    static function fromJson( jsonText: String ): TopLevelList {
      return gw.lang.reflect.json.Json.fromJson( jsonText ) as TopLevelList
    }
    static function fromJsonUrl( url: String ): TopLevelList {
      return new java.net.URL( url ).JsonContent
    }
    static function fromJsonUrl( url: java.net.URL ): TopLevelList {
      return url.JsonContent
    }
    static function fromJsonFile( file: java.io.File ) : TopLevelList {
      return fromJsonUrl( file.toURI().toURL() )
    }
    property get value(): List<value>
    structure value {
      property get a(): Integer
      property get b(): Integer
    }
  }

  function testTopLevelValue() {
    var x: Dynamic = Json.fromJson( "72" )
    var topvalue: TopLevelValue = x
    assertEquals( 72, topvalue.value )
  }

  structure TopLevelValue {
    static function fromJson( jsonText: String ): TopLevelValue {
      return gw.lang.reflect.json.Json.fromJson( jsonText ) as TopLevelValue
    }
    static function fromJsonUrl( url: String ): TopLevelValue {
      return new java.net.URL( url ).JsonContent
    }
    static function fromJsonUrl( url: java.net.URL ): TopLevelValue {
      return url.JsonContent
    }
    static function fromJsonFile( file: java.io.File ) : TopLevelValue {
      return fromJsonUrl( file.toURI().toURL() )
    }
    property get value(): Integer
  }

  function testNestedList() {
    var json: Dynamic = Json.fromJson( "{'hi': [[1,2],[4,5]]}" )
    assertEquals( 1, json.hi[0][0] )
    assertEquals( 2, json.hi[0][1] )
    assertEquals( 4, json.hi[1][0] )
    assertEquals( 5, json.hi[1][1] )

    json = Json.fromJson( "{'hi': [{'hi': [[1,2],[4,5]]}, {'bye': [[11,21],[41,51]]}]}" )
    assertEquals( 1, json.hi[0].hi[0][0] )
    assertEquals( 2, json.hi[0].hi[0][1] )
    assertEquals( 4, json.hi[0].hi[1][0] )
    assertEquals( 5, json.hi[0].hi[1][1] )
    assertEquals( 11, json.hi[1].bye[0][0] )
    assertEquals( 21, json.hi[1].bye[0][1] )
    assertEquals( 41, json.hi[1].bye[1][0] )
    assertEquals( 51, json.hi[1].bye[1][1] )
  }

  // Generated
  structure YahooQuotes {
    static function fromJson( jsonText: String ): YahooQuotes {
      return gw.lang.reflect.json.Json.fromJson( jsonText ) as YahooQuotes
    }
    static function fromJsonUrl( url: String ): YahooQuotes {
      return new java.net.URL( url ).JsonContent
    }
    static function fromJsonUrl( url: java.net.URL ): YahooQuotes {
      return url.JsonContent
    }
    static function fromJsonFile( file: java.io.File ) : YahooQuotes {
      return fromJsonUrl( file.toURI().toURL() )
    }
    property get query(): query
    structure query {
      property get created(): String
      property get count(): Integer
      property get lang(): String
      property get results(): results
      structure results {
        property get quote(): List<quote>
        structure quote {
          property get symbol(): String
          property get PriceEPSEstimateNextYear(): String
          property get ChangeinPercent(): String
          property get EPSEstimateNextQuarter(): String
          property get EBITDA(): String
          property get PriceBook(): String
          property get PriceSales(): String
          property get DividendShare(): String
          property get PercebtChangeFromYearHigh(): String
          property get ExDividendDate(): String
          property get PreviousClose(): String
          property get Name(): String
          property get YearHigh(): String
          property get Currency(): String
          property get DividendPayDate(): String
          property get LastTradeDate(): String
          property get PriceEPSEstimateCurrentYear(): String
          property get StockExchange(): String
          property get AverageDailyVolume(): String
          property get PercentChange(): String
          property get ChangeFromYearHigh(): String
          property get FiftydayMovingAverage(): String
          property get TwoHundreddayMovingAverage(): String
          property get PercentChangeFromTwoHundreddayMovingAverage(): String
          property get LastTradeTime(): String
          property get Volume(): String
          property get MarketCapitalization(): String
          property get PercentChangeFromYearLow(): String
          property get DaysHigh(): String
          property get DaysRange(): String
          property get Symbol(): String
          property get EPSEstimateNextYear(): String
          property get PercentChangeFromFiftydayMovingAverage(): String
          property get Open(): String
          property get DaysLow(): String
          property get DividendYield(): String
          property get Ask(): String
          property get EPSEstimateCurrentYear(): String
          property get YearLow(): String
          property get ChangeFromFiftydayMovingAverage(): String
          property get Bid(): String
          property get LastTradeWithTime(): String
          property get ShortRatio(): String
          property get Change(): String
          property get ChangeFromTwoHundreddayMovingAverage(): String
          property get EarningsShare(): String
          property get BookValue(): String
          property get ChangeFromYearLow(): String
          property get OneyrTargetPrice(): String
          property get PERatio(): String
          property get YearRange(): String
          property get Change_PercentChange(): String
          property get PEGRatio(): String
          property get LastTradePriceOnly(): String
        }
      }
    }
  }

  // Generated
  structure Amazon {
    static function fromJson( jsonText: String ): Amazon {
      return gw.lang.reflect.json.Json.fromJson( jsonText ) as Amazon
    }
    static function fromJsonUrl( url: String ): Amazon {
      return new java.net.URL( url ).JsonContent
    }
    static function fromJsonUrl( url: java.net.URL ): Amazon {
      return url.JsonContent
    }
    static function fromJsonFile( file: java.io.File ) : Amazon {
      return fromJsonUrl( file.toURI().toURL() )
    }
    property get query(): query
    structure query {
      property get created(): String
      property get count(): Integer
      property get lang(): String
      property get results(): results
      structure results {
        property get table(): table
        structure table {
          property get request(): request
          property get security(): String
          property get src(): String
          property get meta(): meta
          property get name(): String
          property get hash(): String
          structure request {
            property get select(): List<select>
            structure select {
              property get usesRemoteLimit(): String
              property get key(): List<key>
              structure key {
                @gw.lang.reflect.ActualName( "default" )
                property get Default(): String
                property get name(): String
                property get type(): String
                property get required(): String
              }
            }
          }
          structure meta {
            property get documentationURL(): String
            property get author(): String
          }
        }
      }
    }
  }

}