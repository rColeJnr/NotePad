package com.rick.notepad.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.rick.notepad.R
import com.rick.notepad.databinding.FragmentNewTaskBinding
import com.rick.notepad.model.Task
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.Useful
import com.rick.notepad.util.Useful.task_description
import com.rick.notepad.util.Useful.task_title
import com.rick.notepad.util.popupmenu.ShareOnClick
import com.rick.notepad.util.service.AlarmService
import com.rick.notepad.viewmodel.TaskViewModel
import java.util.*

class TaskViewFragment: Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel
    private val args: TaskViewFragmentArgs by navArgs()
    private lateinit var task: Task
    private var userTime: Long = 0L
    private lateinit var alarmService: AlarmService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmService = AlarmService(requireContext())

        taskViewModel = (activity as MainActivity).taskViewModel
        task = args.task

        task_description = task.task_description
        task_title = task.task_name

        binding.apply {
            etNewTaskName.setText(task.task_name)
            etNewTaskDescription.setText(task.task_description)

            ibNewTaskTime.setOnClickListener {

                setReminder { ( alarmService.setReminder(it)) }
                Useful.dailyTimes = 0
                this.repeatsButton.visibility = View.VISIBLE

            }

            repeatsButton.setOnClickListener {
                PopupMenu(context, it).also {
                    it.inflate(R.menu.repeats_menu)
                    it.setOnMenuItemClickListener { menu ->
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_menu_task, menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.deleteTask -> {
                taskViewModel.deleteTask(task)
                findNavController().navigate(
                    R.id.taskListFragment
                )
                Snackbar.make(binding.root, "Task Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        taskViewModel.addTask(task)
                    }
                    show()
                }
                true
            }
            R.id.shareTask -> { ShareOnClick(task, requireContext()).shareIntent(); true}
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (task.task_name !=  binding.etNewTaskName.text.toString() || task.task_description != binding.etNewTaskDescription.text.toString() ) {
            task.task_name = binding.etNewTaskName.text.toString()
            task.task_description = binding.etNewTaskDescription.text.toString()
            task.task_time = binding.ibNewTaskTime.text.toString()
            taskViewModel.addTask(task)
        }
    }
}