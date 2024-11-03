rootProject.name = "onyxdb"

include("onyxdbApi")
project(":onyxdbApi").projectDir = file("api")

include("onyxdbCommon")
project(":onyxdbCommon").projectDir = file("common")

include("onyxdbCommon:postgres")
