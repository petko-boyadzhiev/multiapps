package com.sap.cloud.lm.sl.mta.mergers.v1;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.handlers.HandlerFactory;
import com.sap.cloud.lm.sl.mta.handlers.v1.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Target;

@RunWith(Parameterized.class)
public class TargetMergerTest {

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) There are properties in every deployment descriptor and target component:
            {
                "mtad-00.yaml", "target-00.json", new Expectation(Expectation.Type.RESOURCE, "result-target-00.json"),
            },
            // (1) Some deployment descriptor components do not have a corresponding deploy target component:
            {
                "mtad-01.yaml", "target-01.json", new Expectation(Expectation.Type.RESOURCE, "result-target-01.json"),
            },
            // (2) Some deploy target properties override properties from the deployment descriptor:
            {
                "mtad-00.yaml", "target-02.json", new Expectation(Expectation.Type.RESOURCE, "result-target-02.json"),
            },
// @formatter:on
        });
    }

    private String deploymentDescriptorYamlLocation;
    private String targetJsonLocation;
    private Expectation expectation;

    public TargetMergerTest(String deploymentDescriptorYamlLocation, String targetJsonLocation, Expectation expectation) {
        this.deploymentDescriptorYamlLocation = deploymentDescriptorYamlLocation;
        this.targetJsonLocation = targetJsonLocation;
        this.expectation = expectation;
    }

    private DeploymentDescriptor deploymentDescriptor;
    private Target target;

    @Before
    public void loadDeploymentDescriptor() throws Exception {
        DescriptorParser parser = getHandlerFactory().getDescriptorParser();
        InputStream deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorYamlLocation);
        this.deploymentDescriptor = parser.parseDeploymentDescriptorYaml(deploymentDescriptorYaml);
    }

    @Before
    public void loadTarget() throws Exception {
        ConfigurationParser parser = getHandlerFactory().getConfigurationParser();
        InputStream targetJson = getClass().getResourceAsStream(targetJsonLocation);
        this.target = parser.parseTargetJson(targetJson);
    }

    @Test
    public void testMergeInto() {
        DescriptorHandler handler = getHandlerFactory().getDescriptorHandler();
        final TargetMerger merger = getTargetMerger(target, handler);
        TestUtil.test(new Callable<DeploymentDescriptor>() {

            @Override
            public DeploymentDescriptor call() throws Exception {
                merger.mergeInto(deploymentDescriptor);
                return deploymentDescriptor;
            }

        }, expectation, getClass());

    }

    protected HandlerFactory getHandlerFactory() {
        return new HandlerFactory(getMajorSchemaVersion());
    }

    protected int getMajorSchemaVersion() {
        return 1;
    }

    protected TargetMerger getTargetMerger(Target target, DescriptorHandler handler) {
        return new TargetMerger(target, handler);
    }
}
