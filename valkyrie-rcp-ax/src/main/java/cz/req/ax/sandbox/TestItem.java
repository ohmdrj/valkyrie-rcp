/*
 * Copyright 2009-2013 Ondřej Buránek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.req.ax.sandbox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class TestItem implements Comparable<TestItem> {
    public static final String[] stringsArr = {"Prvni", "Druhy", "Duhovy", "Treti"};
    public static final List<String> stringsLst = Arrays.asList(stringsArr);
    public static final ArrayList<TestItem> itemsList = new ArrayList<TestItem>();

    static {
        itemsList.add(new TestItem("Foo"));
        itemsList.add(new TestItem("Bar"));
        itemsList.add(new TestItem("Cenax"));
        itemsList.add(new TestItem("Cexar"));
        itemsList.add(new TestItem("Celia"));
        for (int i = 0; i < 50; i++) {
            itemsList.add(new TestItem("Test" + i));
        }
    }

    Date date;
    String string;
    TestItem item;
    List<TestItem> childs;
    Integer numberInteger;
    BigDecimal numberDecimal;
    TestEnum1 enumera;

    public TestItem() {
    }

    public TestItem(String string) {
        this.string = string;
        this.date = new Date();
        this.numberInteger = (int) Math.random() * 1000;
        this.numberDecimal = new BigDecimal(Math.random() * 1000d);
        this.numberDecimal.setScale(4, RoundingMode.HALF_EVEN);
        this.childs = new ArrayList<TestItem>();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public TestItem getItem() {
        return item;
    }

    public void setItem(TestItem item) {
        this.item = item;
    }

    public Integer getNumberInteger() {
        return numberInteger;
    }

    public void setNumberInteger(Integer numberInteger) {
        this.numberInteger = numberInteger;
    }

    public List<TestItem> getChilds() {
        return childs;
    }

    public void setChilds(List<TestItem> childs) {
        this.childs = childs;
    }

    public BigDecimal getNumberDecimal() {
        return numberDecimal;
    }

    public void setNumberDecimal(BigDecimal numberDecimal) {
        this.numberDecimal = numberDecimal;
    }

    public TestEnum1 getEnumera() {
        return enumera;
    }

    public void setEnumera(TestEnum1 enumera) {
        this.enumera = enumera;
    }

    @Override
    public int compareTo(TestItem o) {
        try {
            return getString().compareTo(o.getString());
        } catch (Exception ex) {
            return 0;
        }
    }
}
