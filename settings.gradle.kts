rootProject.name = "simpletron"

include("core:common")
include("core:cpu")
include("core:display")
include("core:input")
include("core:memory")

include(":translator:infix-postfix")
include(":translator:compiler")
