package com.rick.notepad.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.rick.notepad.R
import com.rick.notepad.databinding.FragmentNewTaskBinding
import com.rick.notepad.model.Task
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.Useful.dailyTimes
import com.rick.notepad.util.Useful.task_description
import com.rick.notepad.util.Useful.task_title
import com.rick.notepad.util.service.AlarmService
import com.rick.notepad.viewmodel.TaskViewModel
import java.util.*

class NewTaskFragment: Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel
    private val task = Task()
    private var userTime: Long = 0L
    private lateinit var alarmService: AlarmService
    
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmService = AlarmService(requireContext())

        taskViewModel = (activity as MainActivity).taskViewModel
        
        binding.apply {
            ibNewTaskTime.setOnClickListener {
                task_description = binding.etNewTaskDescription.text.toString()
                task_title = binding.etNewTaskName.text.toString()

                setReminder { ( alarmService.setReminder(it)) }
                dailyTimes = 0
                this.repeatsButton.visibility = View.VISIBLE
            }
            repeatsButton.setOnClickListener {
                PopupMenu(context, it).apply {
                    inflate(R.menu.repeats_menu)
                    setOnMenuItemClickListener { menu ->
                        when(menu.itemId){
                            R.id.doesNotRepeat -> {  }
                            R.id.everyDay -> { alarmService.setDailyReminder(userTime) }
                            R.id.everyWeek -> { alarmService.setWeeklyReminder(userTime) }
                            R.id.everyWeekday -> { alarmService.setWeekdayReminder(userTime) }
                            R.id.everyMonth-> { alarmService.setMonthlyReminder(userTime) }
                            R.id.everyYear -> { alarmService.setYearlyReminder(userTime) }
                        }
                        true
                    }
                }.show()
            }
        }

    }

    private fun setReminder(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                requireContext(),
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        requireContext(),
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            // update views here
                            // holds value of the time set by the user to be used in the menu option
                            userTime = this.timeInMillis
                            callback(userTime)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.ibNewTaskTime.text = DateFormat.format("hh:mm", userTime).toString()
        binding.ibNewTaskDate.text = DateFormat.format("MMM/dd", userTime).toString()
        task.task_time = DateFormat.format("MMM/dd, HH:mm", userTime).toString()

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