package com.tr1984.smilecompetition.page

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.smilecompetition.data.BePrettyDatabase
import com.tr1984.smilecompetition.data.Smiling
import com.tr1984.smilecompetition.databinding.ActivityCalendarBinding
import com.tr1984.smilecompetition.databinding.ItemCalendarBinding
import java.io.File

class CalendarActivity : AppCompatActivity() {

    private lateinit var viewModel: CalendarViewModel
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            CalendarViewModelFactory(
                BePrettyDatabase.getInstance(this),
                intent.getBooleanExtra("withInsert", false)
            )
        ).get(CalendarViewModel::class.java)

        setContentView(
            ActivityCalendarBinding.inflate(layoutInflater)
                .apply {
                    lifecycleOwner = this@CalendarActivity
                    viewModel = this@CalendarActivity.viewModel

                    btnClose.setOnClickListener { finish() }

                    recyclerview.adapter =
                        CalendarAdapter(this@CalendarActivity, this@CalendarActivity.viewModel)
                }.also {
                    binding = it
                }.root
        )

        viewModel.fileName.observe(this, {
            if (it.isNotEmpty()) {
                startActivity(Intent(this@CalendarActivity, PhotoActivity::class.java).apply {
                    putExtra("fileName", it)
                })
            }
        })
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
            val item = getItem(position)
            with(holder.binding) {
                lifecycleOwner = this@CalendarAdapter.lifecycleOwner
                smiling = item
                root.setOnClickListener { viewModel.loadPicture(item) }
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

    class CalendarViewModelFactory(
        private val db: BePrettyDatabase,
        private val withInsert: Boolean
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
                CalendarViewModel(db, withInsert) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}
