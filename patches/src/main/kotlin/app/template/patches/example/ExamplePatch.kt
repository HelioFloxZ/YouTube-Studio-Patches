package app.template.patches.example

import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.COMPATIBILITY_EXAMPLE
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11n
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableStringReference

@Suppress("unused")
val examplePatch = bytecodePatch(
    name = "YouTube Studio Enhancements",
    description = "Disables update checks, update redirects, telemetry, and Patch Store update checks.",
    default = true
) {
    compatibleWith(COMPATIBILITY_EXAMPLE)

    execute { context ->
        context.classPool.classes.forEach { classDef ->
            val isMorpheClass = classDef.type.contains("morphe", ignoreCase = true)

            classDef.methods.forEach { method ->
                val methodImpl = method.implementation ?: return@forEach
                val mutableImpl = methodImpl as? MutableMethodImplementation ?: MutableMethodImplementation(methodImpl)

                // 1. Force In-App Update Checks to Return False
                if (method.returnType == "Z" && (
                    method.name.equals("isUpdateAvailable", ignoreCase = true) ||
                    method.name.equals("checkUpdate", ignoreCase = true) ||
                    method.name.equals("requiresUpdate", ignoreCase = true)
                )) {
                    val instructions = mutableImpl.instructions
                    while (instructions.isNotEmpty()) {
                        mutableImpl.removeInstruction(0)
                    }
                    mutableImpl.addInstruction(BuilderInstruction11n(Opcode.CONST_4, 0, 0))
                    mutableImpl.addInstruction(BuilderInstruction10x(Opcode.RETURN))
                    return@forEach
                }

                // 2. Disable Void Telemetry & Update Handlers
                if (method.returnType == "V" && (
                    method.name.contains("logEvent", ignoreCase = true) ||
                    method.name.contains("trackEvent", ignoreCase = true) ||
                    method.name.contains("sendAnalytics", ignoreCase = true) ||
                    method.name.contains("flushEvents", ignoreCase = true) ||
                    (method.name.contains("checkUpdate", ignoreCase = true) && isMorpheClass)
                )) {
                    val instructions = mutableImpl.instructions
                    while (instructions.isNotEmpty()) {
                        mutableImpl.removeInstruction(0)
                    }
                    mutableImpl.addInstruction(BuilderInstruction10x(Opcode.RETURN_VOID))
                    return@forEach
                }

                // 3. Neutralize Play Store Redirects & MicroG Package Remapping
                val currentInstructions = mutableImpl.instructions.toList()
                currentInstructions.forEachIndexed { index, insn ->
                    if (insn.opcode == Opcode.CONST_STRING || insn.opcode == Opcode.CONST_STRING_JUMBO) {
                        val stringRef = (insn as? BuilderInstruction21c)?.reference as? StringReference
                        val strVal = stringRef?.string ?: ""
                        val reg = (insn as OneRegisterInstruction).registerA

                        if (strVal.contains("market://details?id=") || 
                            strVal.contains("play.google.com/store/apps/details")) {
                            mutableImpl.replaceInstruction(
                                index,
                                BuilderInstruction21c(
                                    Opcode.CONST_STRING,
                                    reg,
                                    ImmutableStringReference("")
                                )
                            )
                        }

                        if (strVal == "com.google.android.gms") {
                            mutableImpl.replaceInstruction(
                                index,
                                BuilderInstruction21c(
                                    Opcode.CONST_STRING,
                                    reg,
                                    ImmutableStringReference("app.revanced.android.gms")
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}