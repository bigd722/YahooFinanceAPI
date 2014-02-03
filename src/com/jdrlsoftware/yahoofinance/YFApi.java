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

package com.jdrlsoftware.yahoofinance;

import java.io.CharArrayReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 *
 * @author Jim Merrell - <www.jmerrell.com>
 */
public class YFApi {

    private final String baseURL = "http://finance.yahoo.com/d/?s=";
    private final String delimChar = ",";
    
    /**
     * Default field options to use if none are specified by calling program.
     */
    private static final YFField[] defaultFieldOptions = new YFField[]{
        YFField.SYMBOL,
        YFField.PREVIOUS_CLOSE,
        YFField.LAST_TRADE,
        YFField.LAST_TRADE_DATE,
        YFField.LAST_TRADE_TIME,
        YFField.CHG_AMT,
        YFField.OPEN,
        YFField.DAYS_HIGH,
        YFField.DAYS_LOW,
        YFField.VOLUME};

    /**
     * The list of ticker symbols formatted for Yahoo! Finance
     */
    private String symbolParamString = "";

    /**
     * The list of field options formatted for Yahoo! Finance
     */
    private String fieldOptionParamString = "";

    /**
     * Holds the quote info returned from Yahoo! Finance in <code>YFQuote</code>
     * objects
     */
    private Map<String, YFQuote> quoteMap;

    /**
     * Class constructor.
     */
    public YFApi() {
    }

    /**
     * Retrieves default fields for the given list of ticker symbols.
     *
     * @param tickerSymbols Ticker symbols as defined by Yahoo! Finance
     * @return The map of <code>YFQuote</code> objects.
     * @throws Exception
     */
    public Map<String, YFQuote> retrieveQuotes(String[] tickerSymbols) throws Exception {
        return retrieveQuotes(tickerSymbols, defaultFieldOptions);
    }

    /**
     * Retrieves the quote information for the given list of ticker symbols.
     *
     * @param tickerSymbols Ticker symbols as defined by Yahoo! Finance
     * @param fieldOptions The fields that will be retrieved from Yahoo! Finance
     * @return The map of <code>YFQuote</code> objects.
     * @throws Exception
     */
    public Map<String, YFQuote> retrieveQuotes(String[] tickerSymbols, YFField[] fieldOptions) throws Exception {

        if (tickerSymbols == null || tickerSymbols.length == 0) {
            throw new IllegalArgumentException("Yahoo Finance ticker symbols cannot be empty/null");
        }

        if (fieldOptions == null || fieldOptions.length == 0) {
            throw new IllegalArgumentException("Yahoo Finance field options cannot be empty/null");
        }

        setSymbolParamString(tickerSymbols);
        setFieldOptionParamString(fieldOptions);

        YFReader yfReader = new YFReader(buildURL());
        String csvDataLine;

        // parse/load the csv data from Yahoo! Finance
        while ((csvDataLine = yfReader.readLine()) != null) {
            loadQuoteDataMap(csvDataLine, fieldOptions);
        }

        yfReader.close();
        return getQuoteMap();
    }

    private void setSymbolParamString(String[] tickerSymbols) {
        StringBuilder sb = new StringBuilder();
        int length = tickerSymbols.length;
        if (length > 0) {
            sb.append(tickerSymbols[0]);
            for (int i = 1; i < length; i++) {
                sb.append("+").append(tickerSymbols[i]);
            }
        }
        symbolParamString = sb.toString();
    }

    private void setFieldOptionParamString(YFField[] fieldOptions) {
        StringBuilder sb = new StringBuilder();
        int length = fieldOptions.length;
        for (int i = 0; i < length; i++) {
            sb.append(fieldOptions[i].getFieldId());
        }
        fieldOptionParamString = sb.toString();
    }

    private void loadQuoteDataMap(String csvData, YFField[] fieldOptions) throws IOException {
        EnumMap<YFField, YFDataItem> dataItemMap = new EnumMap<YFField, YFDataItem>(YFField.class);
        YFQuote quote;
        YFDataItem yfdi;
        String tokenData;

        // parse line into tokens, separated by the delimiter
        ArrayList<String> tokenList = parseTokens(csvData);

        // if number of tokens in csv line does not match the number of field options, 
        // something has gone wrong...
        if (fieldOptions.length != tokenList.size()) {
            throw new IllegalArgumentException("YFApi field option/token mismatch error");
        }
        int fieldIdx = 0;

        // for each token/field, create a YFDataItem and load into a map
        for(String token : tokenList) {
            yfdi = new YFDataItem(fieldOptions[fieldIdx++], token);
            dataItemMap.put(yfdi.getYFField(), yfdi);
        }

        quote = new YFQuote(dataItemMap.get(YFField.SYMBOL).getValue(), dataItemMap);

        getQuoteMap().put(quote.getId(), quote);

    }

    // Unfortunately it is not as simple as using String.split or StringTokenizer
    // to parse the csv data from Yahoo! Finance.  This is because some fields
    // contain embedded commas (name for example)
    private ArrayList<String> parseTokens(String quoteLine) throws IOException {
        if (quoteLine == null) {
            throw new IllegalArgumentException("parseTokens cannot accept null parameter");
        }
        ArrayList<String> tokenList = new ArrayList<String>();
        CharArrayReader charReader = new CharArrayReader(quoteLine.toCharArray());
        int c = charReader.read();
        if (c == -1) {
            //empty quote line??
            return tokenList;
        }
        StringBuffer sb;
        while (c != -1) {
            sb = new StringBuffer();
            if (c == ',') {
                c = charReader.read();
                if (c == ',' || c == -1) {
                    //empty token
                    sb.append("N/A");
                    tokenList.add(sb.toString());
                    c = charReader.read();
                }
                continue;
            }
            if (c == '"') {
                c = charReader.read();
                if (c == '"') {
                    // empty quoted string
                    tokenList.add("N/A");
                    c = charReader.read();
                    continue;
                }
                while (c != '"') { // take anything inside quotes as single field
                    if (c == -1) {
                        throw new IOException("Missing end quote!");
                    }
                    sb.append((char) c);
                    c = charReader.read();
                }
                tokenList.add(sb.toString());
                c = charReader.read();
                continue;
            }
            while (c != ',' && c != -1) { // take anything up to a comma or end
                sb.append((char) c);
                c = charReader.read();
            }
            tokenList.add(sb.toString());
        }
        charReader.close();
        return tokenList;
    }

    private Map<String, YFQuote> getQuoteMap() {
        if (quoteMap == null) {
            quoteMap = new HashMap<String, YFQuote>();
        }
        return quoteMap;
    }

    private String stripQuotes(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }
        final String result;
        result = input.replace('"', ' ');
        return result.trim();
    }

    private URL buildURL() throws MalformedURLException {
        StringBuilder sb = new StringBuilder();
        sb.append(baseURL).append(symbolParamString).append("&f=").append(fieldOptionParamString);
        return new URL(sb.toString());
    }

    public YFQuote getQuote(String id) {
        return getQuoteMap().get(id);
    }

    public String printPropertyInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nYahoo Finance API Properties: ");
        sb.append("\n\tSymbols=[").append(symbolParamString).append("]");
        sb.append("\n\tOptions=[").append(fieldOptionParamString).append("]");
        return sb.toString();
    }

    public String printQuoteInfo() {
        StringBuilder sb = new StringBuilder();
        //System.out.println("size of YFApi quoteMap is: " + getQuoteMap().size());
        Collection<YFQuote> quotes = getQuoteMap().values();
        sb.append("\nYahoo Finance API Quote Data:");
        if (quotes != null && quotes.size() > 0) {
            for (YFQuote q : quotes) {
                sb.append(q.printInfo());
            }
        } else {
            sb.append("\n\t<no quote data found>");
        }
        return sb.toString();
    }
}
