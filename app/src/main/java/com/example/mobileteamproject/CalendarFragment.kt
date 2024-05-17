package com.example.mobileteamproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobileteamproject.databinding.FragmentCalendarBinding
import java.io.FileInputStream
import java.io.FileOutputStream

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private var fname: String = ""
    private var str: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            binding.diaryTextView.visibility = View.VISIBLE
            binding.saveBtn.visibility = View.VISIBLE
            binding.contextEditText.visibility = View.VISIBLE
            binding.textView2.visibility = View.INVISIBLE
            binding.chaBtn.visibility = View.INVISIBLE
            binding.delBtn.visibility = View.INVISIBLE

            binding.diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            binding.contextEditText.setText("")

            checkedDay(year, month, dayOfMonth)
        }

        binding.saveBtn.setOnClickListener {
            saveDiary(fname)
            Toast.makeText(requireContext(), "$fname 데이터를 저장했습니다.", Toast.LENGTH_SHORT).show()
            str = binding.contextEditText.text.toString()
            binding.textView2.text = str
            binding.saveBtn.visibility = View.INVISIBLE
            binding.chaBtn.visibility = View.VISIBLE
            binding.delBtn.visibility = View.VISIBLE
            binding.contextEditText.visibility = View.INVISIBLE
            binding.textView2.visibility = View.VISIBLE
        }

        return view
    }

    private fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {
        fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"
        var fis: FileInputStream? = null

        try {
            fis = requireContext().openFileInput(fname)
            val fileData = ByteArray(fis.available())
            fis.read(fileData)
            fis.close()

            str = String(fileData)
            binding.contextEditText.visibility = View.INVISIBLE
            binding.textView2.visibility = View.VISIBLE
            binding.textView2.text = str

            binding.saveBtn.visibility = View.INVISIBLE
            binding.chaBtn.visibility = View.VISIBLE
            binding.delBtn.visibility = View.VISIBLE

            binding.chaBtn.setOnClickListener {
                binding.contextEditText.visibility = View.VISIBLE
                binding.textView2.visibility = View.INVISIBLE
                binding.contextEditText.setText(str)
                binding.saveBtn.visibility = View.VISIBLE
                binding.chaBtn.visibility = View.INVISIBLE
                binding.delBtn.visibility = View.INVISIBLE
                binding.textView2.text = binding.contextEditText.text
            }

            binding.delBtn.setOnClickListener {
                binding.textView2.visibility = View.INVISIBLE
                binding.contextEditText.setText("")
                binding.contextEditText.visibility = View.VISIBLE
                binding.saveBtn.visibility = View.VISIBLE
                binding.chaBtn.visibility = View.INVISIBLE
                binding.delBtn.visibility = View.INVISIBLE
                removeDiary(fname)
                Toast.makeText(requireContext(), "$fname 데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show()
            }

            if (binding.textView2.text.isEmpty()) {
                binding.textView2.visibility = View.INVISIBLE
                binding.diaryTextView.visibility = View.VISIBLE
                binding.saveBtn.visibility = View.VISIBLE
                binding.chaBtn.visibility = View.INVISIBLE
                binding.delBtn.visibility = View.INVISIBLE
                binding.contextEditText.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun saveDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = requireContext().openFileOutput(readyDay, 0)
            val content: String = binding.contextEditText.text.toString()
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun removeDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = requireContext().openFileOutput(readyDay, 0)
            val content: String = ""
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
