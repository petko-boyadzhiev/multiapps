package com.sap.cloud.lm.sl.mta.handlers.v1;

import java.io.InputStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor;

@RunWith(value = Parameterized.class)
public class DescriptorParserTest {

    private final String extensionDescriptorsLocation;
    private final String deploymentDescriptorLocation;
    private final Expectation[] expectations;

    private InputStream extensionDescriptorsYaml;
    private InputStream deploymentDescriptorYaml;

    private DescriptorParser parser;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptors:
            {
                "/mta/sample/v1/mtad-01.yaml", "/mta/sample/v1/config-01.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "mtad-01.yaml.json"),
                    new Expectation(Expectation.Type.RESOURCE, "config-01.mtaext.json"),
                },
            },
            // (1) Multiple modules with the same name:
            {
                "mtad-03.yaml", "config-01.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"foo\" for key \"name\" not unique for object \"MTA module\""),
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"foo\" for key \"name\" not unique for object \"MTA extension module\""),
                },
            },
            // (2) Multiple provided dependencies with the same name:
            {
                "mtad-04.yaml", "config-02.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"bar\" for key \"name\" not unique for object \"MTA provided dependency\""),
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"bar\" for key \"name\" not unique for object \"MTA extension provided dependency\""),
                },
            },
            // (3) Multiple resources with the same name:
            {
                "mtad-05.yaml", "config-03.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"foo\" for key \"name\" not unique for object \"MTA resource\""),
                    new Expectation(Expectation.Type.EXCEPTION, "Value \"foo\" for key \"name\" not unique for object \"MTA extension resource\""),
                },
            },
            // (4) No deploy targets element in extension descriptor:
            {
                null, "config-04.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.RESOURCE, "config-04.mtaext.json"),
                },
            },
            // (5) Partial schema version support test (int):
            {
                "mtad-with-partial-schema-version-major.yaml", "config-with-partial-schema-version-major.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "parsed-mtad-with-partial-schema-version.json"),
                    new Expectation(Expectation.Type.RESOURCE, "parsed-config-with-partial-schema-version.json"),
                },
            },
            // (6) Partial schema version support test (double):
            {
                "mtad-with-partial-schema-version-major.minor.yaml", "config-with-partial-schema-version-major.minor.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "parsed-mtad-with-partial-schema-version.json"),
                    new Expectation(Expectation.Type.RESOURCE, "parsed-config-with-partial-schema-version.json"),
                },
            },
            // (7) Partial schema version support test (string):
            {
                "mtad-with-partial-schema-version-major-quoted.yaml", "config-with-partial-schema-version-major-quoted.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "parsed-mtad-with-partial-schema-version.json"),
                    new Expectation(Expectation.Type.RESOURCE, "parsed-config-with-partial-schema-version.json"),
                },
            },
            // (8) Partial schema version support test (string):
            {
                "mtad-with-partial-schema-version-major.minor-quoted.yaml", "config-with-partial-schema-version-major.minor-quoted.mtaext",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "parsed-mtad-with-partial-schema-version.json"),
                    new Expectation(Expectation.Type.RESOURCE, "parsed-config-with-partial-schema-version.json"),
                },
            },
// @formatter:on
        });
    }

    public DescriptorParserTest(String deploymentDescriptorLocation, String extensionDescriptorsLocation, Expectation[] expectations) {
        this.extensionDescriptorsLocation = extensionDescriptorsLocation;
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.expectations = expectations;
    }

    @Before
    public void setUp() throws Exception {
        if (extensionDescriptorsLocation != null) {
            extensionDescriptorsYaml = getClass().getResourceAsStream(extensionDescriptorsLocation);
        }
        if (deploymentDescriptorLocation != null) {
            deploymentDescriptorYaml = getClass().getResourceAsStream(deploymentDescriptorLocation);
        }
        parser = createDescriptorParser();
    }

    protected DescriptorParser createDescriptorParser() {
        return new DescriptorParser();
    }

    @Test
    public void testParseDeploymentDescriptorYaml() throws Exception {
        TestUtil.test(new Callable<DeploymentDescriptor>() {
            @Override
            public DeploymentDescriptor call() throws Exception {
                return parser.parseDeploymentDescriptorYaml(deploymentDescriptorYaml);
            }
        }, expectations[0], getClass());
    }

    @Test
    public void testParseExtensionDescriptorYaml() throws Exception {
        TestUtil.test(new Callable<ExtensionDescriptor>() {
            @Override
            public ExtensionDescriptor call() throws Exception {
                return parser.parseExtensionDescriptorYaml(extensionDescriptorsYaml);
            }
        }, expectations[1], getClass());
    }

}
