rootProject.name = "onyxdb"

include(":common")
include(":common:postgres")
include(":mdb")

include(":mongodb-operator")
//include(":onyxdb-mongodb-operator")
//project(":onyxdb-mongodb-operator").projectDir = File(settingsDir, "mongodb-operator")
