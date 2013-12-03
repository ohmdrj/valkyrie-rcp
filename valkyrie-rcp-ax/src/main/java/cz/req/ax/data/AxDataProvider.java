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

package cz.req.ax.data;

import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.widget.editor.provider.AbstractDataProvider;
import org.valkyriercp.widget.editor.provider.DataProviderEvent;

import java.util.List;

/**
 * AxDataProvider
 *
 * @author Ondrej Burianek
 */
@Configurable
public abstract class AxDataProvider extends AbstractDataProvider implements DataLoader {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private DataIdentity dataIdentity;
    private DataFactory dataFactory;
    private boolean supportsUpdate = false;
    private boolean supportsCreate = false;
    private boolean supportsDelete = false;
    private boolean supportsFiltering = false;
    private boolean supportsClone = false;
    private boolean supportsDetail = false;

    public AxDataProvider() {
    }

    public AxDataProvider(DataFactory dataFactory) {
        this(dataFactory, dataFactory);
    }

    public AxDataProvider(DataIdentity dataIdentity, DataFactory dataFactory) {
        this.dataIdentity = dataIdentity;
        this.dataFactory = dataFactory;
        setSupport(true, true, true);
    }

    public AxDataProvider(Class dataIdentityClass) {
        this(dataIdentityClass, dataIdentityClass);
    }

    public AxDataProvider(Class dataIdentityClass, Class dataFactoryClass) {
        this(new AxDataIdentity(dataIdentityClass), new AxDataFactory(dataFactoryClass));
        setSupport(true, true, true);
    }

    public AxDataProvider(String dataIdentityId, Class dataIdentityClass) {
        this(dataIdentityId, dataIdentityClass, dataIdentityClass);
    }

    public AxDataProvider(String dataIdentityId, Class dataIdentityClass, Class dataFactoryClass) {
        this(new AxDataIdentity(dataIdentityId, dataIdentityClass), new AxDataFactory(dataIdentityId, dataFactoryClass));
        setSupport(true, true, true);
    }

    @Override
    public abstract List getList(Object criteria);

    @Override
    public List getList() {
        return getList(null);
    }

    @Override
    protected Object loadDetailObject(Object baseObject) {
        return baseObject;
    }

    //TODO remove?
    public Object checkDetailObject(Object baseObject) {
        return loadDetailObject(baseObject);
    }

    @Override
    public void notifyObservers(Object arg) {
        if (arg instanceof DataProviderEvent) {
            DataProviderEvent event = (DataProviderEvent) arg;
            if (event.getNewEntity() != null) {
                if (!event.getNewEntity().getClass().isAssignableFrom(dataFactory.getDataClass())) {
                    throw new ClassCastException("DataProvider mixed class to " + event.getNewEntity().getClass());
                }
            }
        }
        super.notifyObservers(arg);
    }

    //    @Deprecated
//    public PropertyColumnTableDescription getTableDescription(String... cols) {
//        Assert.notNull(dataFactory, "DataFactory is null");
//        return new AxTableDescription(dataFactory).add(cols);
//    }
    public DataFactory getDataFactory() {
        return dataFactory;
    }

    public void setDataFactory(DataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    public DataIdentity getDataIdentity() {
        return dataIdentity;
    }

    public void setDataIdentity(DataIdentity dataIdentity) {
        this.dataIdentity = dataIdentity;
    }

    @Override
    public boolean supportsFiltering() {
        return supportsFiltering;
    }

    @Override
    public boolean supportsUpdate() {
        return supportsUpdate;
    }

    @Override
    public boolean supportsCreate() {
        return supportsCreate;
    }

    @Override
    public boolean supportsDelete() {
        return supportsDelete;
    }

    @Override
    public boolean supportsClone() {
        return supportsClone;
    }

    public boolean supportsDetail() {
        return supportsDetail;
    }

    @Override
    public Object doUpdate(Object updatedData) {
        return updatedData;
    }

    public void setSupport(boolean supportsCreate, boolean supportsUpdate, boolean supportsDelete) {
        setSupportsCreate(supportsCreate);
        setSupportsUpdate(supportsUpdate);
        setSupportsDelete(supportsDelete);
    }

    public void setSupport(boolean supportsCreate, boolean supportsUpdate, boolean supportsDelete, boolean supportsDetail) {
        setSupportsCreate(supportsCreate);
        setSupportsUpdate(supportsUpdate);
        setSupportsDelete(supportsDelete);
        setSupportsDetail(supportsDetail);
    }

    public void setSupportsClone(boolean supportsClone) {
        this.supportsClone = supportsClone;
    }

    public void setSupportsCreate(boolean supportsCreate) {
        this.supportsCreate = supportsCreate;
    }

    public void setSupportsDelete(boolean supportsDelete) {
        this.supportsDelete = supportsDelete;
    }

    public void setSupportsFiltering(boolean supportsFiltering) {
        this.supportsFiltering = supportsFiltering;
    }

    public void setSupportsUpdate(boolean supportsUpdate) {
        this.supportsUpdate = supportsUpdate;
    }

    public void setSupportsDetail(boolean supportsDetail) {
        this.supportsDetail = supportsDetail;
    }
}
