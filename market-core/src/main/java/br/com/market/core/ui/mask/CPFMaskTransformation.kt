package br.com.market.core.ui.mask

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.min

class CPFMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return text.maskCpf()
    }
}

private fun AnnotatedString.maskCpf(): TransformedText {
    val inputText = text.substring(0 until min(text.length, 14))
    val mask = "###.###.###-##"
    val maskedText = buildString {
        var inputIndex = 0
        for (maskChar in mask) {
            if (inputIndex >= inputText.length) break
            if (maskChar == '#') {
                append(inputText[inputIndex])
                inputIndex++
            } else {
                append(maskChar)
            }
        }
    }

    val offsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset < maskedText.length) {
                return offset + offset / 4
            }
            return maskedText.length
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset < maskedText.length) {
                return offset - offset / 4
            }
            return inputText.length
        }
    }

    return TransformedText(AnnotatedString(maskedText), offsetTranslator)
}