package com.onyxdb.platform;

import java.util.UUID;

import com.onyxdb.platform.idm.common.jwt.AdminInitializer;
import com.onyxdb.platform.idm.models.Product;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePreset;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetType;
import com.onyxdb.platform.mdb.utils.TimeUtils;

public class TestUtils {
    public static final UUID SANDBOX_PROJECT_ID = UUID.fromString("a57842b9-4fe4-433b-9e27-7d0188d2c33b");
    public static final UUID NOT_EXISTING_PROJECT_ID = UUID.fromString("23788255-8a34-4558-8dca-a4e7e63e0727");

    public static final UUID PARENT_PRODUCT_ID = UUID.fromString("22eb7009-ab07-4c1f-a3b4-9c861d5b697f");
    public static final UUID CHILD_PRODUCT_ID = UUID.fromString("8369fd6e-f7c5-4f8d-844f-10ab39ad1eaa");

    public static final UUID ADMIN_ID = AdminInitializer.ADMIN_ID;

    public static final String TEST_1_RESOURCE_PRESET_ID = "s-c2-r8";
    public static final String NOT_EXISTING_RESOURCE_PRESET_ID = "not-existing-preset";

    public static final String DEFAULT_NAMESPACE = "onyxdb";

    public static final Product PRODUCT = new Product(
            PARENT_PRODUCT_ID,
            "Connectify",
            "Социальная сеть Connectify",
            null,
            TestUtils.ADMIN_ID,
            null,
            TimeUtils.now(),
            TimeUtils.now()
    );

    public static ResourcePreset RESOURCE_PRESET = new ResourcePreset(
            TEST_1_RESOURCE_PRESET_ID,
            ResourcePresetType.STANDARD,
            1000,
            1073741824
    );

    public static Project SANDBOX_PROJECT = Project.create(
            TestUtils.SANDBOX_PROJECT_ID,
            "sandbox",
            "sandbox project",
            TestUtils.CHILD_PRODUCT_ID,
            DEFAULT_NAMESPACE,
            TestUtils.ADMIN_ID
    );
}
