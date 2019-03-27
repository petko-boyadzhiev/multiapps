package com.sap.cloud.lm.sl.mta.model.v2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;

public class Platform extends com.sap.cloud.lm.sl.mta.model.v1.Platform implements ParametersContainer {

    private Map<String, Object> parameters;
    private List<ModuleType> moduleTypes2;
    private List<ResourceType> resourceTypes2;

    protected Platform() {

    }

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVersion(String version) {
        throw new UnsupportedOperationException();
    }

    public void setResourceTypes2(List<ResourceType> resourceTypes) {
        setResourceTypes(resourceTypes);
    }

    @Override
    protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.ResourceType> resourceTypes) {
        this.resourceTypes2 = ListUtil.cast(resourceTypes);
    }

    public List<ResourceType> getResourceTypes2() {
        return ListUtil.upcastUnmodifiable(getResourceTypes());
    }

    @Override
    protected List<? extends ResourceType> getResourceTypes() {
        return resourceTypes2;
    }

    public void setModuleTypes2(List<ModuleType> moduleTypes) {
        setModuleTypes(moduleTypes);
    }

    @Override
    protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.ModuleType> moduleTypes) {
        this.moduleTypes2 = ListUtil.cast(moduleTypes);
    }

    public List<ModuleType> getModuleTypes2() {
        return ListUtil.upcastUnmodifiable(getModuleTypes());
    }

    @Override
    protected List<? extends ModuleType> getModuleTypes() {
        return moduleTypes2;
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getParameters() {
        return MapUtil.unmodifiable(parameters);
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new LinkedHashMap<>(parameters);
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1.Platform.Builder {

        protected Map<String, Object> parameters;
        protected List<ModuleType> moduleTypes2;
        protected List<ResourceType> resourceTypes2;

        @Override
        public Platform build() {
            Platform result = new Platform();
            result.setName(name);
            result.setDescription(description);
            result.setModuleTypes2(ObjectUtils.defaultIfNull(moduleTypes2, Collections.<ModuleType> emptyList()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setResourceTypes2(ObjectUtils.defaultIfNull(resourceTypes2, Collections.<ResourceType> emptyList()));
            return result;
        }

        public void setModuleTypes2(List<ModuleType> moduleTypes) {
            setModuleTypes(moduleTypes);
        }

        @Override
        protected void setModuleTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.ModuleType> moduleTypes) {
            this.moduleTypes2 = ListUtil.cast(moduleTypes);
        }

        public void setResourceTypes2(List<ResourceType> resourceTypes) {
            setResourceTypes(resourceTypes);
        }

        @Override
        protected void setResourceTypes(List<? extends com.sap.cloud.lm.sl.mta.model.v1.ResourceType> resourceTypes) {
            this.resourceTypes2 = ListUtil.cast(resourceTypes);
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        @Override
        public void setProperties(Map<String, Object> properties) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersion(String version) {
            throw new UnsupportedOperationException();
        }

    }

}
