package app.template.patches.example

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.COMPATIBILITY_EXAMPLE

private const val EXTENSION_CLASS = "Lapp/template/extension/extension/ExamplePatch;"

@Suppress("unused")
val examplePatch = bytecodePatch(
    name = "YouTube Studio Enhancements",
    description = "Disables update checks, redirects, telemetry, and enables MicroG support.",
    default = true
) {
    dependsOn(internalPatch)

    extendWith("extensions/extension.mpe")

    compatibleWith(COMPATIBILITY_EXAMPLE)

    execute {
        exampleFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """
        )

        exampleIntegrationsFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, $EXTENSION_CLASS;->getGmsCorePackageName()Ljava/lang/String;
                move-result-object v0
                return-object v0
            """
        )
    }
}