package br.com.market.core.ui.mask

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CEPMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return text.maskCep()
    }
}

private fun AnnotatedString.maskCep(): TransformedText {
    val inputText = text.substring(0 until kotlin.math.min(text.length, 9))
    val mask = "#####-###"
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
                return offset + offset / 5
            }
            return maskedText.length
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset < maskedText.length) {
                return offset - offset / 5
            }
            return inputText.length
        }
    }

    return TransformedText(AnnotatedString(maskedText), offsetTranslator)
}