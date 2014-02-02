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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jim Merrell - <www.jmerrell.com>
 */
public enum YFField {

    ASK("a"),
    AVG_DAILY_VOLUME("a2"),
    ASK_SIZE("a5"),
    BID("b"),
    ASK_REAL_TIME("b2"),
    BID_REAL_TIME("b3"),
    BOOK_VALUE("b4"),
    BID_SIZE("b6"),
    CHG_AND_PERCENT_CHG("c"),
    CHG_AMT("c1"),
    COMMISION("c3"),
    CHG_AMT_REAL_TIME("c6"),
    AFTER_HOURS_CHG_REAL_TIME("c8"),
    DIV_PER_SHARE("d"),
    LAST_TRADE_DATE("d1"),
    TRADE_DATE("d2"),
    EARNINGS_PER_SHARE("e"),
    ERROR_INDICATION("e1"),
    EPS_EST_CURRENT_YEAR("e7"),
    EPS_EST_NEXT_YEAR("e8"),
    EPS_EST_NEXT_QUARTER("e9"),
    FLOAT_SHARES("f6"),
    DAYS_LOW("g"),
    DAYS_HIGH("h"),
    FIFTY_TWO_WEEK_LOW("j"),
    FIFTY_TWO_WEEK_HIGH("k"),
    //Note: This API is currently not designed to work with portfolios
    //HOLDINGS_GAIN_PERCENT("g1"),
    //ANNUALIZED_GAIN("g3"),
    //HOLDINGS_GAIN("g4"),
    //HOLDINGS_GAIN_PERCENT_REAL_TIME("g5"),
    //HOLDINGS_GAIN_REAL_TIME("g6"),
    // unclear how to decode/use "more info" value returned so ignore for now
    //MORE_INFO("i"),  
    ORDER_BOOK_REAL_TIME("i5"),
    MKT_CAPITALIZATION("j1"),
    MKT_CAPITALIZATION_REAL_TIME("j3"),
    EBITDA("j4"),
    CHG_FROM_FIFTY_TWO_WEEK_LOW("j5"),
    PERCENT_CHG_FROM_FIFTY_TWO_WEEK_LOW("j6"),
    LAST_TRADE_WITH_REAL_TIME("k1"),
    CHG_PERCENT_REAL_TIME("k2"),
    LAST_TRADE_SIZE("k3"),
    CHG_FROM_FIFTY_TWO_WEEK_HIGH("k4"),
    PERCENT_CHG_FROM_FIFTY_TWO_WEEK_HIGH("k5"),
    LAST_TRADE_WITH_TIME("l"),
    LAST_TRADE("l1"),
    HIGH_LIMIT("l2"),
    LOW_LIMIT("l3"),
    DAYS_RANGE("m"),
    DAYS_RANGE_REAL_TIME("m2"),
    FIFTY_DAY_MOVING_AVG("m3"),
    CHG_FROM_FIFTY_DAY_MOVING_AVG("m4"),
    PERCENT_CHG_FROM_FIFTY_DAY_MOVING_AVG("m5"),
    TWO_HUNDRED_DAY_MOVING_AVG("m6"),
    CHG_FROM_TWO_HUNDRED_DAY_MOVING_AVG("m7"),
    PERCENT_CHG_FROM_TWO_HUNDRED_DAY_MOVING_AVG("m8"),
    NAME("n"),
    //Note: This API is currently not designed to work with portfolios
    //NOTES("n4"),
    OPEN("o"),
    PREVIOUS_CLOSE("p"),
    //Note: This API is currently not designed to work with portfolios
    //PRICE_PAID("p1"),
    //Note: This API is currently not designed to work with portfolios
    //CHG_IN_PERCENT("c2"),
    PRICE_PER_SALES("p5"),
    PRICE_PER_BOOK("p6"),
    EX_DIV_DATE("q"),
    PRICE_EARNINGS_RATIO("r"),
    DIV_PAY_DATE("r1"),
    PRICE_EARNINGS_RATIO_REAL_TIME("r2"),
    PEG_RATIO("r5"),
    PRICE_PER_EPS_EST_CURRENT_YEAR("r6"),
    PRICE_PER_EPS_EST_NEXT_YEAR("r7"),
    SYMBOL("s"),
    //Note: This API is currently not designed to work with portfolios
    //SHARES_OWNED("s1"),
    SHORT_RATIO("s7"),
    LAST_TRADE_TIME("t1"),
    TRADE_LINKS("t6"),
    TICKER_TREND("t7"),
    ONE_YEAR_TARGET_PRICE("t8"),
    VOLUME("v"),
    //Note: This API is currently not designed to work with portfolios
    //HOLDINGS_VALUE("v1"),
    //HOLDINGS_VALUE_REAL_TIME("v7"),
    FIFTY_TWO_WEEK_RANGE("w"),
    //Note: This API is currently not designed to work with portfolios
    //DAYS_VALUE_CHG("w1"),
    //DAYS_VALUE_CHG_REAL_TIME("w4"),
    STOCK_EXCHG("x"),
    DIV_YIELD("y");

    private static final Map<String, YFField> codeMap = new HashMap<String, YFField>();

    static {
        for (YFField s : EnumSet.allOf(YFField.class)) {
            codeMap.put(s.getFieldId(), s);
        }
    }

    private final String fieldId;

    private YFField(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public static YFField getField(String fieldId) {
        return codeMap.get(fieldId);
    }

}
