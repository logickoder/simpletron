rootProject.name = "simpletron"

include("core:common")
include("core:cpu")
include("core:display")
include("core:input")
include("core:memory")

include(":compiler:infix-postfix")
include(":compiler:translator")
include(":compiler:compiler")
