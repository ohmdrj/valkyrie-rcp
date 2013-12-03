/*
 * Copyright 2009-2013 Ondrej Burianek.
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

package cz.req.ax;

import cz.thickset.utils.ExtensionFileFilter;
import cz.thickset.utils.reflect.ClassUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.ValidatingFormModel;
import org.valkyriercp.binding.form.support.DefaultFormModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * AxUtis
 *
 * @author Ondrej Burianek
 */
public class AxUtils {

    static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AxUtils.class);
    protected static File lastDir = new File(System.getProperty("user.home"));

    public static String lowerCaseFirst(String string) {
        if (string == null) {
            return null;
        }
        if (string.length() < 2) {
            return string.toLowerCase();
        }
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    public static String upperCaseFirst(String string) {
        if (string == null) {
            return null;
        }
        if (string.length() < 2) {
            return string.toUpperCase();
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static File applicationUserFile(String appname, String file) {
        return new File(applicationUserDir(appname), file);
    }

    public static File applicationUserDir(String appname) {
        Assert.notNull(appname, "Application name is required!");
        String path = System.getProperty("user.home") + "/." + appname.replaceAll(" ", "");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static GridBagConstraints gc(int gridx, int gridy) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gridx;
        gc.gridy = gridy;
        return gc;
    }

    public static GridBagConstraints gc(int gridx, int gridy, int inset) {
        GridBagConstraints gc = gc(gridx, gridy);
        gc.insets = new Insets(inset, inset, inset, inset);
        return gc;
    }

    public static GridBagConstraints gc(int gridx, int gridy, int weightx, int weighty, boolean spanx) {
        GridBagConstraints gc = gc(gridx, gridy);
        gc.weightx = weightx;
        gc.weighty = weighty;
        if (spanx) {
            gc.fill = GridBagConstraints.HORIZONTAL;
        }
        return gc;
    }

    public static GridBagConstraints gc(int gridx, int gridy, int weightx, int weighty, boolean spanx, boolean spany) {
        GridBagConstraints gc = gc(gridx, gridy, weightx, weighty, spanx);
        if (spanx && spany) {
            gc.fill = GridBagConstraints.BOTH;
        } else if (spanx) {
            gc.fill = GridBagConstraints.HORIZONTAL;
        } else if (spany) {
            gc.fill = GridBagConstraints.VERTICAL;
        }
        return gc;
    }

    public static GridBagConstraints gc(int gridx, int gridy, int weightx, int weighty, boolean spanx, boolean spany, int insetx, int insety) {
        GridBagConstraints gc = gc(gridx, gridy, weightx, weighty, spanx, spany);
        gc.insets = new Insets(insety, insetx, insety, insetx);
        return gc;
    }

    @Deprecated
    public static ApplicationContext getPageContext() {
        //MIG
        throw new UnsupportedOperationException();
//        ApplicationPage page = Application.instance().getActiveWindow().getPage();
//        if (page instanceof AxApplicationPage) {
//            AxApplicationPage axpage = (AxApplicationPage) page;
//            return axpage.getApplicationContext();
//        } else {
//            return null;
//        }
    }

    @Deprecated
    public static void showMessageDialog(String message) {
        //MIG
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static boolean showConfirmDialog(String message) {
        //MIG
        throw new UnsupportedOperationException();
    }

    public static ValidatingFormModel createStructuredFormModel(Class valueClass) {
        return createStructuredFormModel(valueClass, valueClass.getSimpleName().toLowerCase());
    }

    public static ValidatingFormModel createStructuredFormModel(Class valueClass, String formId) {
        Object formObject = new RecursiveInstanceFacotry(valueClass, "cz.thickset").getInstance();
        DefaultFormModel formModel = new DefaultFormModel(formObject, true);
        formModel.setId(formId);
        return formModel;
    }

    public static Object newRecursiveClassInstance(Class valueClass) {
        return new RecursiveInstanceFacotry(valueClass, "cz.thickset").getInstance();
    }

    public static File showSaveDialog(String suggestName, String suggestExt, String suggestDesc) {
        JFileChooser chooser = new JFileChooser(lastDir);
        chooser.setSelectedFile(new File(lastDir, suggestName + "." + suggestExt));
        chooser.setFileFilter(new ExtensionFileFilter(true, suggestExt, suggestDesc));
        chooser.setMultiSelectionEnabled(false);
        //MIG
        int selec = chooser.showSaveDialog(null);//Application.instance().getActiveWindow().getControl());
        if (JFileChooser.APPROVE_OPTION != selec) {
            return null;
        }
        File file = chooser.getSelectedFile();
        if (file.isDirectory()) {
            lastDir = file;
        } else {
            lastDir = file.getParentFile();
        }
        return file;
    }

    static class RecursiveFieldFinder {

        List<Field> fields;
        Class clazz;

        RecursiveFieldFinder(Class clazz) {
            this.clazz = clazz;
        }

        private void addField(Field field) {
            field.setAccessible(true);
            fields.add(field);
        }

        public void searchClass(Class search) {
            for (Field field : search.getDeclaredFields()) {
                addField(field);
            }
            if (search.getSuperclass() == null) {
                return;
            }
            if (search.getSuperclass().equals(Object.class)) {
                return;
            }
            searchClass(search.getSuperclass());
        }

        public List<Field> getFields() {
            if (fields == null) {
                fields = new ArrayList<Field>();
                searchClass(clazz);
            }
            return fields;
        }
    }

    static class RecursiveInstanceFacotry {

        Class clazz;
        String packz;

        RecursiveInstanceFacotry(Class clazz, String packz) {
            this.clazz = clazz;
            this.packz = packz;
        }

        public Object getInstance(Class instClass) throws Exception {
//            try {
            Constructor constructor = instClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object object = constructor.newInstance();
            RecursiveFieldFinder fieldFinder = new RecursiveFieldFinder(instClass);
            for (Field field : fieldFinder.getFields()) {
                try {
                    Assert.notNull(field, "field");
                    Class fieldClass = field.getType();

                    Assert.notNull(fieldClass, "fieldClass");
                    if (fieldClass.equals(instClass)) {
                        continue;
                    }
                    if (fieldClass.isAssignableFrom(List.class)) {
                        write(object, field, new ArrayList());
                    }
                    if (fieldClass.getCanonicalName().startsWith(packz)) {
                        write(object, field, getInstance(field.getType()));
                    }
                } catch (Exception ex) {
                    //commonly fails
                    log.warn("Common fail on " + instClass.getCanonicalName());
                }
            }
            return object;
//            } catch (Exception ex) {
//                return null;
//            }
        }

        protected void write(Object object, Field field, Object value) throws Exception {
            try {
                Assert.notNull(value, "fieldValue");
                Method writeMethod = ClassUtils.getWriteMethod(object.getClass(), field.getName(), field.getType());
                if (writeMethod != null) {
                    writeMethod.invoke(object, value);
                }
            } catch (Exception ex) {
                log.warn("Recursive field instance exception class=" + object.getClass() + " field=" + field.getName() + " type=" + field.getType() + ": " + ex.getMessage());
            }
        }

        public Object getInstance() {
            try {
                return getInstance(clazz);
            } catch (Exception ex) {
                return new RuntimeException(ex);
            }
        }
    }
}
