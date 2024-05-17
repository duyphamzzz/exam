import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    // Khai báo các biến và đối tượng cần sử dụng
    private val questions = listOf(
        "What is the capital of France?" to listOf("Paris", "London", "Berlin", "Madrid"),
        "What is 2 + 2?" to listOf("3", "4", "5", "6"),
        "What is the color of the sky?" to listOf("Blue", "Green", "Red", "Yellow"),
        "Which of the following statements is true about the capital of France? It is a major European city and a global center for art, fashion, gastronomy and culture. Its 19th-century cityscape is crisscrossed by wide boulevards and the River Seine." to listOf(
            "Paris is known for its café culture and landmarks like the Eiffel Tower.",
            "Paris is the largest city in France by area.",
            "Paris has a Mediterranean climate.",
            "Paris is the financial capital of Europe."
        )
    )

    private val correctAnswers = listOf("Paris", "4", "Blue", "Paris is known for its café culture and landmarks like the Eiffel Tower.")

    private var currentQuestionIndex = 0
    private var correctCount = 0

    private lateinit var questionTextView: TextView
    private lateinit var answersRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.questionTextView)
        answersRadioGroup = findViewById(R.id.answersRadioGroup)
        submitButton = findViewById(R.id.submitButton)
        resultTextView = findViewById(R.id.resultTextView)

        // Khôi phục trạng thái câu hỏi và câu trả lời nếu có
        savedInstanceState?.let { savedInstanceState ->
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex", 0)
            val selectedAnswerIndex = savedInstanceState.getInt("selectedAnswerIndex", -1)
            loadQuestion()
            if (selectedAnswerIndex != -1) {
                (answersRadioGroup.getChildAt(selectedAnswerIndex) as RadioButton).isChecked = true
            }
        }

        // Xử lý sự kiện khi nhấn nút Submit
        submitButton.setOnClickListener {
            checkAnswer()
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                loadQuestion()
            } else {
                showResult()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Lưu trạng thái hiện tại của câu hỏi và câu trả lời đã chọn
        val selectedAnswerIndex = answersRadioGroup.indexOfChild(findViewById(answersRadioGroup.checkedRadioButtonId))
        outState.putInt("currentQuestionIndex", currentQuestionIndex)
        outState.putInt("selectedAnswerIndex", selectedAnswerIndex)
    }

    private fun loadQuestion() {
        // Hiển thị câu hỏi và các đáp án lên giao diện
        val (question, answers) = questions[currentQuestionIndex]
        questionTextView.text = question
        answersRadioGroup.clearCheck()

        for (i in 0 until answersRadioGroup.childCount) {
            (answersRadioGroup.getChildAt(i) as RadioButton).text = answers[i]
        }
    }

    private fun checkAnswer() {
        // Kiểm tra câu trả lời và tăng điểm nếu đúng
        val selectedRadioButtonId = answersRadioGroup.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val selectedAnswer = findViewById<RadioButton>(selectedRadioButtonId).text
            if (selectedAnswer == correctAnswers[currentQuestionIndex]) {
                correctCount++
            }
        }
    }

    private fun showResult() {
        // Hiển thị kết quả cuối cùng
        resultTextView.text = "You got $correctCount out of ${questions.size} correct!"
        resultTextView.visibility = TextView.VISIBLE
        submitButton.isEnabled = false
    }
}
