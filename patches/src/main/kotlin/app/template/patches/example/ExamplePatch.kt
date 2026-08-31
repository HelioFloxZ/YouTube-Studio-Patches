package app.template.patches.example

import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.COMPATIBILITY_EXAMPLE
import app.morphe.patcher.extensions.SmaliExtensions.replaceInstruction
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11n
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableStringReference

@Suppress("unused")
val youtubeStudioEnhancementsPatch = bytecodePatch(
    name = "YouTube Studio Enhancements",
    description = "Disables update checks, redirects, telemetry, and enables MicroG (GmsCore) support.",
    default = true
) {
    compatibleWith(COMPATIBILITY_EXAMPLE)

    execute {
        updateDialogFingerprint.result?.let { match ->
            val mutableImpl = match.mutableMethod.implementation ?: return@let
            val instructions = mutableImpl.instructions
            while (instructions.isNotEmpty()) {
                mutableImpl.removeInstruction(0)
            }
            mutableImpl.addInstruction(BuilderInstruction10x(Opcode.RETURN_VOID))
        }

        forceUpdateCheckFingerprint.result?.let { match ->
            val mutableImpl = match.mutableMethod.implementation ?: return@let
            val instructions = mutableImpl.instructions
            while (instructions.isNotEmpty()) {
                mutableImpl.removeInstruction(0)
            }
            mutableImpl.addInstruction(BuilderInstruction11n(Opcode.CONST_4, 0, 0))
            mutableImpl.addInstruction(BuilderInstruction10x(Opcode.RETURN))
        }

        clearcutTelemetryFingerprint.result?.let { match ->
            val mutableImpl = match.mutableMethod.implementation ?: return@let
            val instructions = mutableImpl.instructions
            while (instructions.isNotEmpty()) {
                mutableImpl.removeInstruction(0)
            }
            mutableImpl.addInstruction(BuilderInstruction10x(Opcode.RETURN_VOID))
        }

        classes.forEach { classDef ->
            classDef.methods.forEach { method ->
                val methodImpl = method.implementation ?: return@forEach
                val mutableImpl = methodImpl as? MutableMethodImplementation ?: MutableMethodImplementation(methodImpl)

                val currentInstructions = mutableImpl.instructions.toList()
                currentInstructions.forEachIndexed { index, insn ->
                    if (insn.opcode == Opcode.CONST_STRING || insn.opcode == Opcode.CONST_STRING_JUMBO) {
                        val stringRef = (insn as? BuilderInstruction21c)?.reference as? StringReference
                        val strVal = stringRef?.string ?: ""
                        val reg = (insn as OneRegisterInstruction).registerA

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