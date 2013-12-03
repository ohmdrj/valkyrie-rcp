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

package cz.req.ax.data;

import org.springframework.binding.convert.converters.Converter;
import org.valkyriercp.binding.value.support.ListListModel;

import javax.swing.*;
import java.util.Arrays;

/**
 * @author Ondrej Burianek
 */
public class ArrayToListModel implements Converter {

    @Override
    public Class getSourceClass() {
        return Object[].class;
    }

    @Override
    public Class getTargetClass() {
        return ListModel.class;
    }

    @Override
    public Object convertSourceToTargetClass(Object source, Class targetClass) throws Exception {
        Object[] objects = (Object[]) source;
        return new ListListModel(Arrays.asList(objects));
    }
}