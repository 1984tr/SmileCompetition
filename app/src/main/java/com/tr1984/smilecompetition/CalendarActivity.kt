package com.tr1984.smilecompetition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import com.tr1984.smilecompetition.data.BePrettyDatabase
import com.tr1984.smilecompetition.data.Smiling
import com.tr1984.smilecompetition.databinding.ActivityCalendarBinding
import com.tr1984.smilecompetition.databinding.ItemCalendarBinding
import com.tr1984.smilecompetition.util.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CalendarActivity : AppCompatActivity() {

    private lateinit var viewModel: CalendarViewModel
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory(BePrettyDatabase.getInstance(this)))
            .get(CalendarViewModel::class.java)

        setContentView(
            ActivityCalendarBinding.inflate(layoutInflater)
                .apply {
                    lifecycleOwner = this@CalendarActivity
                    viewModel = this@CalendarActivity.viewModel

                    btnClose.setOnClickListener { finish() }

                    recyclerview.adapter = CalendarAdapter(this@CalendarActivity, this@CalendarActivity.viewModel)
                }.also {
                   binding = it
                }.root
        )
    }

    class CalendarAdapter(
        private val lifecycleOwner: LifecycleOwner,
        private val viewModel: CalendarViewModel
    ) : ListAdapter<Smiling, CalendarAdapter.Holder>(diffCallback) {

        init {
            viewModel.histories.observe(lifecycleOwner, {
                submitList(it)
            })
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val binding = ItemCalendarBinding.inflate(LayoutInflater.from(parent.context))
            return Holder(binding)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            with(holder.binding) {
                lifecycleOwner = this@CalendarAdapter.lifecycleOwner
                smiling = getItem(position)
            }
        }

        class Holder(val binding: ItemCalendarBinding) : RecyclerView.ViewHolder(binding.root)

        companion object {
            val diffCallback = object :
                DiffUtil.ItemCallback<Smiling>() {

                override fun areItemsTheSame(oldItem: Smiling, newItem: Smiling): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Smiling, newItem: Smiling): Boolean {
                    return oldItem.equals(newItem)
                }
            }

        }
    }
}
