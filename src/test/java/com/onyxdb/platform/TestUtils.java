package com.onyxdb.platform;

import java.util.UUID;

import com.onyxdb.platform.idm.common.jwt.AdminInitializer;

public class TestUtils {
    public static final UUID PROJECT_ID_1 = UUID.fromString("a57842b9-4fe4-433b-9e27-7d0188d2c33b");
    public static final UUID PROJECT_ID_2 = UUID.fromString("23788255-8a34-4558-8dca-a4e7e63e0727");
    public static final UUID PROJECT_ID_3 = UUID.fromString("7f673db3-b272-4f89-bdbc-74006d37a17a");

    public static final UUID PRODUCT_ID_1 = UUID.fromString("22eb7009-ab07-4c1f-a3b4-9c861d5b697f");
    public static final UUID PRODUCT_ID_2 = UUID.fromString("8369fd6e-f7c5-4f8d-844f-10ab39ad1eaa");
    public static final UUID PRODUCT_ID_3 = UUID.fromString("99d2a60a-fe72-4203-8c01-de9b602cb758");

    public static final UUID ADMIN_ID = AdminInitializer.ADMIN_ID;
}
