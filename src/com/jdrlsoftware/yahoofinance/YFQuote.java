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


import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Jim Merrell - <www.jmerrell.com>
 */
public class YFQuote {
    private final String id;
    private Map<YFField,YFDataItem> fieldDataMap;
    
    public YFQuote(String id, EnumMap<YFField, YFDataItem> fieldDataMap) {
        this.id = id;
        this.fieldDataMap = fieldDataMap;
    }
    
    public String getId() {
        return id;
    }
    
    private Map<YFField,YFDataItem> getFieldDataMap() {
        if (fieldDataMap == null) {
            fieldDataMap = new EnumMap<YFField,YFDataItem>(YFField.class);
        }
        return fieldDataMap;
    }

    private Collection<YFField> getFields() {
        return getFieldDataMap().keySet();
    }
    
    public boolean containsField(YFField yff) {
        return getFieldDataMap().containsKey(yff);
    }
    
    private YFDataItem getDataItem(YFField yffKey) {
        return getFieldDataMap().get(yffKey);
    }
    
    public String getDataItemValue(YFField yffKey) {
        YFDataItem item = getDataItem(yffKey);
        if (item != null) {
            return item.getValue();
        } else {
            return "";
        }
    }
    
    public void addQuoteData(YFDataItem qd) {
        getFieldDataMap().put(qd.getYFField(), qd);
    }
    
    public String printInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tQuoteId: ").append(getId());
        Collection<YFField> fieldList = getFields();
        for (YFField yff : fieldList) {
            sb.append("\n\t\t").append(yff).append("=[").append(getDataItem(yff).getValue()).append("]");
        }
        return sb.toString();
    }
    
}
