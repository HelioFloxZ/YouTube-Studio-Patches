package app.template.patches.example

import inline.activity.InlineActivity
import inline.bytecode.Instruction
import inline.bytecode.Opcode
import inline.patch.Patch
import inline.patch.annotation.PatchBinding

@PatchBinding(
    name = "Disable forced updates",
    description = "Disables the forced update popup and redirection in YT Studio.",
    dependencies = []
)
object ExamplePatch : Patch() {

    override fun execute() {
        // Method 1: Intercepting the Google Play In-App Update API
        // This targets the AppUpdateInfo check which decides if an update is available
        val appUpdateInfoClass = context.fingerprints.findClass("com.google.android.play.core.appupdate.AppUpdateInfo")
        
        appUpdateInfoClass?.let { clazz ->
            // Locate the method that checks if a specific update flow is allowed
            // Usually: isUpdateTypeAllowed(AppUpdateOptions) or similar returning a Boolean
            val updateAllowedMethod = clazz.methods.firstOrNull { 
                it.returnType == "Z" && it.parameters.size == 1 
            }

            updateAllowedMethod?.patch {
                // Wipe the existing instructions and force it to return false (0)
                instructions.clear()
                instructions.add(Instruction(Opcode.CONST_4, 0, 0)) // v0 = 0 (false)
                instructions.add(Instruction(Opcode.RETURN, 0))     // return v0
            }
        }

        // Method 2: Spoofing the Version Code via PackageInfo intercept
        // If the app checks its own version against a hardcoded timestamp, 
        // we intercept the version check method to return a far-future version code.
        val versionConfigClass = context.fingerprints.findClass("com.google.android.apps.youtube.creator.VersionConfig")
        
        versionConfigClass?.let { clazz ->
            val getVersionCodeMethod = clazz.methods.firstOrNull { 
                it.returnType == "I" && it.parameters.isEmpty() 
            }

            getVersionCodeMethod?.patch {
                instructions.clear()
                // Force return a massive version code (e.g., 20261234)
                instructions.add(Instruction(Opcode.CONST, 0, 126340007)) 
                instructions.add(Instruction(Opcode.RETURN, 0))
            }
        }
    }
}
