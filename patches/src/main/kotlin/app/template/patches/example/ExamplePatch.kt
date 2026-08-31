package app.template.patches.example

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.COMPATIBILITY_EXAMPLE

private const val EXTENSION_CLASS = "Lapp/template/extension/extension/ExamplePatch;"

@Suppress("unused")
val disableUpdatesPatch = bytecodePatch(
    name = "Disable Updates",
    description = "Disables in-app update prompts and Play Store update redirects.",
    default = true
) {
    dependsOn(internalPatch)

    execute {
        updateCheckFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """
        )

        playStoreRedirectFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )
    }
}

@Suppress("unused")
val disableTelemetryPatch = bytecodePatch(
    name = "Disable Telemetry",
    description = "Disables analytics and telemetry event logging.",
    default = true
) {
    dependsOn(internalPatch)

    execute {
        telemetryLoggerFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )
    }
}

@Suppress("unused")
val microGSupportPatch = bytecodePatch(
    name = "MicroG Support",
    description = "Redirects Google Play Services calls to GmsCore (MicroG).",
    default = true
) {
    dependsOn(internalPatch)

    extendWith("extensions/extension.mpe")

    execute {
        gmsCoreRedirectionFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, $EXTENSION_CLASS;->getGmsCorePackageName()Ljava/lang/String;
                move-result-object v0
                return-object v0
            """
        )
    }
}