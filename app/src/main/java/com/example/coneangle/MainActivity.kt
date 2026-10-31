package com.example.coneangle

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.coneangle.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.atan

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculate.setOnClickListener { calculateAngle() }
    }

    private fun calculateAngle() {
        val big = readNumber(binding.bigDiameter, binding.bigDiameterLayout)
        val small = readNumber(binding.smallDiameter, binding.smallDiameterLayout)
        val distance = readNumber(binding.distance, binding.distanceLayout)

        if (big == null || small == null || distance == null) return

        if (distance == 0.0) {
            binding.distanceLayout.error = getString(R.string.error_distance_positive)
            return
        }

        val tangent = (big - small) / (2.0 * distance)
        val degrees = Math.toDegrees(atan(tangent))

        binding.result.text = getString(R.string.result, formatDegrees(degrees))
        binding.result.visibility = View.VISIBLE
    }

    private fun readNumber(field: TextInputEditText, layout: TextInputLayout): Double? {
        val text = toLatinNumber(field.text)
        if (text.isEmpty()) {
            layout.error = getString(R.string.error_required)
            return null
        }
        val value = text.toDoubleOrNull()
        if (value == null) {
            layout.error = getString(R.string.error_not_number)
            return null
        }
        layout.error = null
        return value
    }

    // Numeric keyboards in Persian accept both Latin and Persian/Arabic digits.
    private fun toLatinNumber(raw: CharSequence?): String {
        val input = raw?.toString()?.trim() ?: return ""
        val digits = "۰۱۲۳۴۵۶۷۸۹٠١٢٣٤٥٦٧٨٩"
        return buildString(input.length) {
            for (ch in input) {
                val index = digits.indexOf(ch)
                when {
                    index >= 0 -> append('0' + index % 10)
                    ch == '٫' || ch == '،' -> append('.')
                    else -> append(ch)
                }
            }
        }
    }

    private fun formatDegrees(value: Double): String {
        val format = DecimalFormat("0.0000", DecimalFormatSymbols(Locale("fa")))
        return format.format(value)
    }
}
