package automation.analysis

import java.time.ZonedDateTime

class Exam(
    val itemId: Long,
    val inspections: List<Inspection>
) {
    val createdAt: ZonedDateTime = ZonedDateTime.now()
    val updatedAt: ZonedDateTime = ZonedDateTime.now()
}

abstract class Inspection(
    val name: String
) {
    abstract fun isValid(value: Any?): Boolean
}

class NumericInspection(
    name: String,
    private val minimumValue: Float?,
    private val maximumValue: Float?
) : Inspection(name) {
    override fun isValid(value: Any?): Boolean {
        if (value == null) return false

        value as Float

        val results = mutableListOf<Boolean>()

        minimumValue?.run { results.add(this <= value) }
        maximumValue?.run { results.add(this >= value) }

        return results.isNotEmpty() && results.all { true }
    }
}

class OptionInspection(
    name: String,
    val values: List<Option>
) : Inspection(name) {
    override fun isValid(value: Any?): Boolean {
        if (value == null) return false
        value as Option
        return value.correct
    }
}

class Option(
    var value: String,
    var correct: Boolean
)

class FormulaInspection(
    name: String
) : Inspection(name) {
    override fun isValid(value: Any?): Boolean {
        return false
    }
}

class Analysis(
    val exam: Exam,
    val results: Map<Inspection, Any>
)

val exam = Exam(
    1L,
    listOf(
        NumericInspection("Peso", 200f, 600f)
    )
)

val itemAnalysis = Analysis(
    exam, exam.inspections.associateWith { inspection -> inspection.isValid("") }
)

//fun main() {
//    println(itemAnalysis)
//}
