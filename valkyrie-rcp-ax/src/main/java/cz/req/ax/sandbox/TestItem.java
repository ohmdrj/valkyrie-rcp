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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class TestItem {
    public static final String[] stringsArr = {"Prvni", "Druhy", "Duhovy", "Treti"};
    public static final List<String> stringsLst = Arrays.asList(stringsArr);
    public static final ArrayList<TestItem> samples = new ArrayList<TestItem>();
    static {
        samples.add(new TestItem("Foo"));
        samples.add(new TestItem("Bar"));
        samples.add(new TestItem("Cenax"));
        samples.add(new TestItem("Cexar"));
        samples.add(new TestItem("Celia"));
    }
    Date date;
    String string;
    TestItem item;
    Integer numberInteger;
    Float numberFloat;
    TestEnum1 enumera;

    public TestItem() {
    }

    public TestItem(String string) {
        this.string = string;
        this.date = new Date();
        this.numberInteger = (int) Math.random() * 1000;
        this.numberFloat = (float) Math.random() * 1000f;
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

    public Float getNumberFloat() {
        return numberFloat;
    }

    public void setNumberFloat(Float numberFloat) {
        this.numberFloat = numberFloat;
    }

    public TestEnum1 getEnumera() {
        return enumera;
    }

    public void setEnumera(TestEnum1 enumera) {
        this.enumera = enumera;
    }
}
