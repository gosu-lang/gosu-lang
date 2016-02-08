package gw.internal.gosu.parser.classTests.gwtest.dynamic

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
          "structure Results {\n" +
          "  property get query(): query\n" +
          "  structure query {\n" +
          "    property get created(): String\n" +
          "    property get count(): Integer\n" +
          "    property get lang(): String\n" +
          "    property get results(): results\n" +
          "    structure results {\n" +
          "      property get quote(): List<quote>\n" +
          "      structure quote {\n" +
          "        property get symbol(): String\n" +
          "        property get PriceEPSEstimateNextYear(): String\n" +
          "        property get ChangeinPercent(): String\n" +
          "        property get EPSEstimateNextQuarter(): String\n" +
          "        property get EBITDA(): String\n" +
          "        property get PriceBook(): String\n" +
          "        property get PriceSales(): String\n" +
          "        property get DividendShare(): String\n" +
          "        property get PercebtChangeFromYearHigh(): String\n" +
          "        property get ExDividendDate(): String\n" +
          "        property get PreviousClose(): String\n" +
          "        property get Name(): String\n" +
          "        property get YearHigh(): String\n" +
          "        property get Currency(): String\n" +
          "        property get DividendPayDate(): String\n" +
          "        property get LastTradeDate(): String\n" +
          "        property get PriceEPSEstimateCurrentYear(): String\n" +
          "        property get StockExchange(): String\n" +
          "        property get AverageDailyVolume(): String\n" +
          "        property get PercentChange(): String\n" +
          "        property get ChangeFromYearHigh(): String\n" +
          "        property get FiftydayMovingAverage(): String\n" +
          "        property get TwoHundreddayMovingAverage(): String\n" +
          "        property get PercentChangeFromTwoHundreddayMovingAverage(): String\n" +
          "        property get LastTradeTime(): String\n" +
          "        property get Volume(): String\n" +
          "        property get MarketCapitalization(): String\n" +
          "        property get PercentChangeFromYearLow(): String\n" +
          "        property get DaysHigh(): String\n" +
          "        property get DaysRange(): String\n" +
          "        property get Symbol(): String\n" +
          "        property get EPSEstimateNextYear(): String\n" +
          "        property get PercentChangeFromFiftydayMovingAverage(): String\n" +
          "        property get Open(): String\n" +
          "        property get DaysLow(): String\n" +
          "        property get DividendYield(): String\n" +
          "        property get Ask(): String\n" +
          "        property get EPSEstimateCurrentYear(): String\n" +
          "        property get YearLow(): String\n" +
          "        property get ChangeFromFiftydayMovingAverage(): String\n" +
          "        property get Bid(): String\n" +
          "        property get LastTradeWithTime(): String\n" +
          "        property get ShortRatio(): String\n" +
          "        property get Change(): String\n" +
          "        property get ChangeFromTwoHundreddayMovingAverage(): String\n" +
          "        property get EarningsShare(): String\n" +
          "        property get BookValue(): String\n" +
          "        property get ChangeFromYearLow(): String\n" +
          "        property get OneyrTargetPrice(): String\n" +
          "        property get PERatio(): String\n" +
          "        property get YearRange(): String\n" +
          "        property get Change_PercentChange(): String\n" +
          "        property get PEGRatio(): String\n" +
          "        property get LastTradePriceOnly(): String\n" +
          "      }\n" +
          "    }\n" +
          "  }\n" +
          "}\n"
          
  function testStructureFromJson() {
    var results: Dynamic = Json.instance().fromJsonString( _yahoo_finance_quotes_json )
    var structr = results.toStructure( "Results" )
    assertEquals( _structure, structr )

    var structureResults = Json.instance().fromJsonString( _yahoo_finance_quotes_json ) as Results
    for( stock in structureResults.query.results.quote ) {
      var symbol = stock.Symbol
      var close = stock.Ask
      var change = stock.Change
      var volume = stock.Volume
      print( "${symbol} ${close} ${change} ${volume}" )
    }
  }

  structure Results {
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
}