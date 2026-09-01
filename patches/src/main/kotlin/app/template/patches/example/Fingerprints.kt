package app.template.patches.example

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string

object StartUpdateFingerprint : Fingerprint(
    definingClass = "Lttt;",
    name = "h",
    returnType = "V",
    parameters = listOf("Landroid/view/ViewGroup;"),
    filters = listOf(
        string(":startUpdate")
    )
)

object PlayStoreRedirectFingerprint : Fingerprint(
    definingClass = "Lesy;",
    name = "e",
    returnType = "V",
    filters = listOf(
        string("market://details?id=")
    )
)