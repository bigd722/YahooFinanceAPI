/*
 * Copyright (C) 2012 Jim Merrell - JDRL Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.jdrlsoftware.yahoofinance.YFApi;
import com.jdrlsoftware.yahoofinance.YFField;
import com.jdrlsoftware.yahoofinance.YFQuote;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jim Merrell - <www.jmerrell.com>
 */
public class TestYahooFinanceAPI {

    public static void main(String[] args) {

        Map<String, YFQuote> quoteMap = new HashMap<String, YFQuote>();

        // Note: It is no longer possible to retrieve the DOW info from 
        // Yahoo! Finance. The ticker is ^DJI, but Yahoo! no longer allows
        // people to retrieve it via csv method.  If the ^DJI ticker is passed 
        // to Yahoo!, it's ignored and Yahoo! doesn't return it back in the
        // result. This is different than if you sent an invalid ticker.  Ticker 
        // "A443" is not a valid ticker and Yahoo returns it back with "N/A".
        String[] tickers = new String[]{"^DJI", "IBM", "A443", "MSFT", "JNJ", "BMPS.MI"};

        // init the Yahoo Finance API using default constructor
        YFApi yfApi;
        try {
            yfApi = new YFApi();

            // retrieve default quote fields from Yahoo! Finance
            quoteMap = yfApi.retrieveQuotes(tickers);

            System.out.println("\n\nTestYahooFinanceAPI\nprint all quote info from yfApi:");
            System.out.println(yfApi.printQuoteInfo());

            System.out.println("\nIterate through tickers and retrieve the last price");
            for (String symbol : tickers) {
                YFQuote q = quoteMap.get(symbol);
                if (q == null) {
                    System.out.println("Quote information not found for symbol: " + symbol);
                    continue;
                }
                System.out.println(symbol + " last trade price = " + q.getDataItemValue(YFField.LAST_TRADE));
            }

        } catch (Exception ex) {
            System.out.println("ERROR retrieving Yahoo Finance quotes");
            Logger.getLogger(TestYahooFinanceAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Example of specifying which fields to retrieve from Yahoo! Finance
        try {
            tickers = new String[]{"INTL", "CSCO"};
            YFField[] fieldOptions = new YFField[]{
                YFField.SYMBOL,
                YFField.NAME,
                YFField.STOCK_EXCHG};

            yfApi = new YFApi();

            // retrieve specific quote fields from Yahoo! Finance
            quoteMap = yfApi.retrieveQuotes(tickers, fieldOptions);

            System.out.println("\n\nTestYahooFinanceAPI\nprint all quote info from yfApi:");
            System.out.println(yfApi.printQuoteInfo());

            // retrieve a specific field for specific stock
            System.out.println("\nStock Exchange for INTL is: " + quoteMap.get("INTL").getDataItemValue(YFField.STOCK_EXCHG));
            System.out.println("Company name for CSCO is: " + quoteMap.get("CSCO").getDataItemValue(YFField.NAME));

        } catch (Exception ex) {
            System.out.println("ERROR retrieving Yahoo Finance quotes");
            Logger.getLogger(TestYahooFinanceAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
