package com.zrzring.douyin_memo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zrzring.douyin_memo.databinding.ItemMemoBinding
import com.zrzring.douyin_memo.model.Memo

class MemoAdapter(
    private var memos: List<Memo>,
    private val onItemClick: (Memo) -> Unit
) : RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val binding = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val memo = memos[position]
        holder.bind(memo)
        holder.itemView.setOnClickListener {
            onItemClick(memo)
        }
    }

    override fun getItemCount(): Int = memos.size

    fun updateData(newMemos: List<Memo>) {
        memos = newMemos
        notifyDataSetChanged() // 在实际项目中建议使用 DiffUtil
    }

    class MemoViewHolder(private val binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memo: Memo) {
            binding.textViewMemoTitle.text = memo.title
            binding.textViewMemoContent.text = memo.content
        }
    }
}