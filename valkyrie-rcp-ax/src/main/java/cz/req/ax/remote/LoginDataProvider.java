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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import cz.req.ax.AxUtils;
import cz.req.ax.data.AxDataFactory;
import cz.req.ax.data.AxDataIdentity;
import cz.req.ax.data.AxDataProvider;
import cz.thickset.utils.branding.ThicksetProject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.application.ApplicationDescriptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * LoginDataProvider
 *
 * @author Ondrej Burianek
 */
@Configurable
public class LoginDataProvider extends AxDataProvider {

    @Autowired
    ApplicationDescriptor applicationDescriptor;
    @Autowired
    ConnectionRegistry connectionRegistry;
    List<ConnectionItem> list = Collections.synchronizedList(new ArrayList());
    File file;

    public LoginDataProvider() {
        super(new AxDataIdentity(ConnectionItem.class), new LoginDataFactory(ConnectionItem.class));
        setSupport(true, true, true, false);
    }

    public LoginDataProvider(ConnectionItem... servers) {
        this();
        list.addAll(Arrays.asList(servers));
    }

    public ConnectionRegistry getConnectionRegistry() {
        return connectionRegistry;
    }

    @Autowired
    public void setConnectionRegistry(ConnectionRegistry registry) {
        this.connectionRegistry = registry;
    }

    @Override
    public synchronized List<ConnectionItem> getList() {
        return new ArrayList<ConnectionItem>(list);
    }

    @Override
    public List getList(Object criteria) {
        xmlLoad(getFile());
        checkDefaults();
        return getList();
    }

    @Override
    public Object doCreate(Object newData) {
        if (newData instanceof ConnectionItem) {
            list.add((ConnectionItem) newData);
        }
        xmlSave();
        return newData;
    }

    @Override
    public Object doUpdate(Object updatedData) {
        xmlSave();
        return updatedData;
    }

    @Override
    public void doDelete(Object dataToRemove) {
        if (dataToRemove instanceof ConnectionItem) {
            list.remove((ConnectionItem) dataToRemove);
        }
        xmlSave();
    }

    public ConnectionItem findOne(String name) {
        xmlLoad();
        for (ConnectionItem item : getList()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public Collection<ConnectionItem> findAuto() {
        return Collections2.filter(getList(), new Predicate<ConnectionItem>() {

            @Override
            public boolean apply(ConnectionItem input) {
                return input.isAutoconnect();
            }
        });
    }

    public void setFile(File file) {
        this.file = file;
    }

    protected File getFile() {
        if (file == null) {
            file = AxUtils.applicationUserFile(getApplicationName(), "logins.xml");
            File older = new File(System.getProperty("user.home") + "/.AxLogin-" + getApplicationName() + ".xml");
            if (older.exists()) {
                try {
                    if (file.exists()) {
                        FileUtils.deleteQuietly(older);
                        ;
                    } else {
                        FileUtils.moveFile(older, file);
                    }
                } catch (IOException ex) {
                    log.error("Error manipulating old config " + older, ex);
                }
            }
        }
        return file;
    }

    protected String getApplicationName() {
        String name = ThicksetProject.instance().getName();
        if (name == null) {
            name = applicationDescriptor.getDisplayName();
        }
        return name;
    }

    protected void resetDefaults() {
        if (connectionRegistry != null && connectionRegistry.getDefaults() != null) {
            List<ConnectionItem> tmp = getList();
            for (ConnectionItem itemNew : connectionRegistry.getDefaults()) {
                for (ConnectionItem itemOld : tmp) {
                    if (itemOld.getName().equals(itemNew.getName())) {
                        list.remove(itemOld);
                    }
                }
                list.add(itemNew);
            }
            xmlSave();
        }
    }

    protected void checkDefaults() {
        if (list.isEmpty()) {
            resetDefaults();
        }
    }

    protected synchronized void xmlLoad(File file) {
        log.info("Loading XML");
        list.clear();
        try {
            if (!file.exists()) {
                return;
            }
            Document document = new SAXReader().read(file);
            Element root = document.getRootElement();
            String vers = root.attributeValue("version");
            if (vers == null) {
                return;
            }
            for (Element e1 : (List<Element>) root.elements()) {
                try {
                    String name = e1.attributeValue("name");
                    String clazz = e1.attributeValue("class");
                    String url = e1.attributeValue("url");
                    String autoc = e1.attributeValue("auto");
                    String user = e1.attributeValue("user");
                    String pass = e1.attributeValue("pass");

                    //BUGFIX
                    url = url == null ? null : url.replace("/remoting", "");

                    ConnectionItem item = new ConnectionItem();
                    item.setName(name);
                    item.setUrl(url);
                    item.setConnectionClass(connectionRegistry.findClass(clazz));
                    item.setAutoconnect(autoc != null && Boolean.valueOf(autoc));


                    if (user != null && user.length() > 0) {
                        item.getCredentials().setUsername(user);
                        if (pass != null) {
                            item.getCredentials().setRemember(true);
                            byte[] decoded = Base64.decodeBase64(pass.getBytes());
                            item.getCredentials().setPassword(new String(decoded));
                        }
                    }
                    list.add(item);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("Error parsing server data " + e1);
                }
            }

        } catch (Throwable ex) {
            ex.printStackTrace();
            log.error("Error loading login data from " + file, ex);
        }
    }

    protected void xmlLoad() {
        xmlLoad(getFile());
    }

    protected void xmlSave(File file) {
        XMLWriter writer = null;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            writer = new XMLWriter(new FileOutputStream(file), format);
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("servers");
            root.addAttribute("version", "1");
            for (ConnectionItem item : getList()) {
                Element elm = root.addElement("server");
                try {
                    elm.addAttribute("name", item.getName());
                    elm.addAttribute("class", item.getConnectionClass().getName());
                    elm.addAttribute("url", item.getUrl());
                    elm.addAttribute("auto", Boolean.toString(item.isAutoconnect()));
                    if (item.getCredentials().isRemember()) {
                        elm.addAttribute("user", item.getCredentials().getUsername());
                        if (item.getCredentials().getPassword() != null) {
                            byte[] encoded = Base64.encodeBase64(item.getCredentials().getPassword().getBytes());
                            elm.addAttribute("pass", new String(encoded));
                        }
//                        elm.addAttribute("auto", Boolean.valueOf(server.getConnectionCredentials().isAutologin()).toString());
                    }
                } catch (Exception ex) {
                    log.error("Error server data " + item, ex);
                    root.remove(elm);
                }
//                elm.addCDATA(nhvo.getDokument());
            }
            writer.write(document);
        } catch (Throwable ex) {
            log.error("Error saving login data to " + file, ex);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void xmlSave() {
        xmlSave(getFile());
    }

    public static class LoginDataFactory extends AxDataFactory {

        public LoginDataFactory(Class dataClass) {
            super(dataClass);
//            setRecursive(true);
        }

        @Override
        public Object newInstance(Object criteria) {
            ConnectionItem s = (ConnectionItem) super.newInstance(criteria);
            //TODO set default class
            return s;
        }
    }
}
