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

package cz.req.ax.remote;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.valkyriercp.binding.format.FormatterFactory;
import org.valkyriercp.binding.format.support.StrictNumberFormatterFactory;

/**
 * @author Ondrej Burianek
 */
@Deprecated
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService> {

    private ConversionService conversionService;
    private FormatterFactory formatterFactory = new StrictNumberFormatterFactory();

    protected FormatterFactory getFormatterFactory() {
        return formatterFactory;
    }

    public void setFormatterFactory(FormatterFactory formatterFactory) {
        this.formatterFactory = formatterFactory;
    }

    @Override
    public ConversionService getObject() throws Exception {
        if (conversionService == null) {
            conversionService = createConversionService();
        }
        return conversionService;
    }

    @Override
    public Class getObjectType() {
        return ConversionService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * Creates the conversion service. Subclasses may override to customize creation.
     *
     * @return the configured conversion service
     */
    protected ConversionService createConversionService() {
        DefaultConversionService service = new DefaultConversionService();
//        service.
//        service.addConverter(new TextToDate(getFormatterFactory(), true));
//        service.addConverter(new DateToText(getFormatterFactory(), true));
//        service.addConverter(new TextToNumber(getFormatterFactory(), true));
//        service.addConverter(new NumberToText(getFormatterFactory(), true));
//        service.addConverter(new BooleanToText());
//        service.addConverter(new TextToBoolean());
//        service.addConverter(new CollectionConverter());
//        service.addConverter(new ListModelConverter());
//        service.addConverter(new TimestampToDate(getFormatterFactory()));
//        service.addConverter(new TimestampToString(getFormatterFactory()));
//        service.addConverter(new DateToTimestamp(getFormatterFactory()));
        return service;
    }
//    static final class TextToDate extends AbstractFormattingConverter {
//
//        private final boolean allowEmpty;
//
//        protected TextToDate(FormatterFactory formatterFactory, boolean allowEmpty) {
//            super(formatterFactory);
//            this.allowEmpty = allowEmpty;
//        }
//
//        public Class[] getSourceClasses() {
//            return new Class[]{String.class};
//        }
//
//        public Class[] getTargetClasses() {
//            return new Class[]{Date.class};
//        }
//
//        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
//            return (!allowEmpty || StringUtils.hasText((String) source)) ? getFormatterFactory().getDateTimeFormatter().parseValue((String) source, Date.class) : null;
//        }
//    }
//
//    static final class DateToText extends AbstractFormattingConverter {
//
//        private final boolean allowEmpty;
//
//        protected DateToText(FormatterFactory formatterLocator, boolean allowEmpty) {
//            super(formatterLocator);
//            this.allowEmpty = allowEmpty;
//        }
//
//        public Class[] getSourceClasses() {
//            return new Class[]{Date.class};
//        }
//
//        public Class[] getTargetClasses() {
//            return new Class[]{String.class};
//        }
//
//        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
//            return (!allowEmpty || source != null) ? getFormatterFactory().getDateTimeFormatter().formatValue(source)
//                    : "";
//        }
//    }
//
//    static final class TextToNumber extends AbstractFormattingConverter {
//
//        private final boolean allowEmpty;
//
//        protected TextToNumber(FormatterFactory formatterLocator, boolean allowEmpty) {
//            super(formatterLocator);
//            this.allowEmpty = allowEmpty;
//        }
//
//        public Class[] getSourceClasses() {
//            return new Class[]{String.class};
//        }
//
//        public Class[] getTargetClasses() {
//            return new Class[]{Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
//                        BigInteger.class, BigDecimal.class,};
//        }
//
//        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
//            return (!allowEmpty || StringUtils.hasText((String) source)) ? getFormatterFactory().getNumberFormatter(
//                    targetClass).parseValue((String) source, targetClass) : null;
//        }
//    }
//
//    static final class NumberToText extends AbstractFormattingConverter {
//
//        private final boolean allowEmpty;
//
//        protected NumberToText(FormatterFactory formatterLocator, boolean allowEmpty) {
//            super(formatterLocator);
//            this.allowEmpty = allowEmpty;
//        }
//
//        public Class[] getSourceClasses() {
//            return new Class[]{Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
//                        BigInteger.class, BigDecimal.class,};
//        }
//
//        public Class[] getTargetClasses() {
//            return new Class[]{String.class};
//        }
//
//        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
//            return (!allowEmpty || source != null) ? getFormatterFactory().getNumberFormatter(source.getClass()).formatValue(source) : "";
//        }
//    }
//
//    static final class TextToBoolean extends AbstractConverter {
//
//        public static final String VALUE_TRUE = "true";
//        public static final String VALUE_FALSE = "false";
//        public static final String VALUE_ON = "on";
//        public static final String VALUE_OFF = "off";
//        public static final String VALUE_YES = "yes";
//        public static final String VALUE_NO = "no";
//        public static final String VALUE_1 = "1";
//        public static final String VALUE_0 = "0";
//        private String trueString;
//        private String falseString;
//
//        public TextToBoolean() {
//        }
//
//        public TextToBoolean(String trueString, String falseString) {
//            this.trueString = trueString;
//            this.falseString = falseString;
//        }
//
//        public Class[] getSourceClasses() {
//            return new Class[]{String.class};
//        }
//
//        public Class[] getTargetClasses() {
//            return new Class[]{Boolean.class};
//        }
//
//        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
//            String text = (String) source;
//            if (!StringUtils.hasText(text)) {
//                return null;
//            } else if (this.trueString != null && text.equalsIgnoreCase(this.trueString)) {
//                return Boolean.TRUE;
//            } else if (this.falseString != null && text.equalsIgnoreCase(this.falseString)) {
//                return Boolean.FALSE;
//            } else if (this.trueString == null
//                    && (text.equalsIgnoreCase(VALUE_TRUE) || text.equalsIgnoreCase(VALUE_ON)
//                    || text.equalsIgnoreCase(VALUE_YES) || text.equals(VALUE_1))) {
//                return Boolean.TRUE;
//            } else if (this.falseString == null
//                    && (text.equalsIgnoreCase(VALUE_FALSE) || text.equalsIgnoreCase(VALUE_OFF)
//                    || text.equalsIgnoreCase(VALUE_NO) || text.equals(VALUE_0))) {
//                return Boolean.FALSE;
//            } else {
//                throw new IllegalArgumentException("Invalid boolean value [" + text + "]");
//            }
//        }
//    }
//
//    static final class BooleanToText extends AbstractConverter {
//
//        public static final String VALUE_YES = "yes";
//        public static final String VALUE_NO = "no";
//        private String trueString;
//        private String falseString;
//
//        public BooleanToText() {
//        }
//
//        public BooleanToText(String trueString, String falseString) {
//            this.trueString = trueString;
//            this.falseString = falseString;
//        }
//
//        public Class[] getSourceClasses() {
//            return new Class[]{Boolean.class};
//        }
//
//        public Class[] getTargetClasses() {
//            return new Class[]{String.class};
//        }
//
//        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
//            Boolean bool = (Boolean) source;
//            if (this.trueString != null && bool.booleanValue()) {
//                return trueString;
//            } else if (this.falseString != null && !bool.booleanValue()) {
//                return falseString;
//            } else if (bool.booleanValue()) {
//                return VALUE_YES;
//            } else {
//                return VALUE_NO;
//            }
//        }
//    }
//
//    static final class TimestampToDate extends AbstractFormattingConverter {
//
//        public TimestampToDate(FormatterFactory formatterFactory) {
//            super(formatterFactory);
//        }
//
//        public Class[] getSourceClasses() {
//            return new Class[]{Timestamp.class};
//        }
//
//        public Class[] getTargetClasses() {
//            return new Class[]{Date.class};
//        }
//
//        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
//            if (source == null) {
//                return null;
//            }
//            return new Date(((Timestamp) source).getTime());
//        }
//    }
//
//    static final class TimestampToString extends AbstractFormattingConverter {
//
//        public TimestampToString(FormatterFactory formatterFactory) {
//            super(formatterFactory);
//        }
//
//        public Class[] getSourceClasses() {
//            return new Class[]{Timestamp.class};
//        }
//
//        public Class[] getTargetClasses() {
//            return new Class[]{String.class};
//        }
//
//        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
//            if (source == null) {
//                return null;
//            }
//            return new SimpleDateFormat().format((Timestamp) source);
//        }
//    }
//
//    static final class DateToTimestamp extends AbstractFormattingConverter {
//
//        protected DateToTimestamp(FormatterFactory formatterLocator) {
//            super(formatterLocator);
//        }
//
//        public Class[] getSourceClasses() {
//            return new Class[]{Date.class};
//        }
//
//        public Class[] getTargetClasses() {
//            return new Class[]{Timestamp.class};
//        }
//
//        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
//            if (source == null) {
//                return null;
//            }
//            return new Timestamp(((Date) source).getTime());
//        }
//    }
}
