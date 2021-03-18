package com.rick.notepad.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.rick.notepad.R
import com.rick.notepad.databinding.FragmentNewTaskBinding
import com.rick.notepad.databinding.SetTimeDialogBinding
import com.rick.notepad.model.Task
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class NewTaskFragment: Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel
    private var task = Task()
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var datePickerDialog: DatePickerDialog
    
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = (activity as MainActivity).taskViewModel
        
        binding.apply {
            ibNewTaskTime.setOnClickListener {
                setTime()
            }
            ibNewTaskDate.setOnClickListener {
                setDate()
            }
            repeatsButton.setOnClickListener {
                val popupMenu = PopupMenu(context, it).also {
                    it.inflate(R.menu.repeats_menu)
                    it.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.doesNotRepeat -> true
                            R.id.everyDay -> {}
                            R.id.everyWeek -> {}
                            R.id.everyWeekday -> {}
                            R.id.everyMonth-> {}
                            R.id.everyYear -> {}
                        }
                        true
                    }
                }
            }
        }
        
        
    }
    
    private fun setTime(){
        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            binding.ibNewTaskTime.text = SimpleDateFormat("HH:mm").format(calendar.time)
        }
        timePickerDialog = TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true).also {
                it.show()
            }
    }
    
    private fun setDate(){
        val calendar = Calendar.getInstance()
        val bindingDialog = SetTimeDialogBinding.inflate(layoutInflater)
        val dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            binding.ibNewTaskDate.text = SimpleDateFormat("dd/MM").format(calendar.time)
        }
        datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).also {
            it.show()
        }
        
    }
    
    private fun addToTask(){
        if (binding.etNewTaskName.text.isNotEmpty() || binding.etNewTaskDescription.text!!.isNotEmpty()){
            binding.apply {
                task.task_name = this.etNewTaskName.text.toString()
                task.task_description = this.etNewTaskDescription.text.toString()
                task.task_time = this.ibNewTaskTime.text.toString()
            }
            
            taskViewModel.addTask(task)
        }
    }
    
    override fun onPause() {
        addToTask()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}