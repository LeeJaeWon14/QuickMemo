package com.example.quickmemo.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickmemo.activity.adapter.recycler.BasketListAdapter
import com.example.quickmemo.activity.adapter.recycler.MemoListAdapter
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.activity.room.entity.BasketEntity
import com.example.quickmemo.activity.room.entity.MemoEntity
import com.example.quickmemo.activity.util.Logger
import com.example.quickmemo.databinding.FragmentMemoListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoListFragment : Fragment() {
    private var _binding : FragmentMemoListBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance(page : Int) : MemoListFragment {
            val fragment = MemoListFragment()
            val args = Bundle()
            args.putInt("page", page)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMemoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            binding.rvMemoList.apply {
                adapter = when(arguments?.getInt("page")) {
                    0 -> MemoListAdapter(requireContext())
                    1 -> BasketListAdapter(requireContext())
                    else -> { null }
                }
                layoutManager = LinearLayoutManager(requireContext())
                adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        if (binding.rvMemoList.adapter?.itemCount == 0) {
                            Toast.makeText(requireContext(), "목록이 비었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // replace말고 다른 방법 찾아볼것
        binding.rvMemoList.adapter = when(arguments?.getInt("page")) {
            0 -> MemoListAdapter(requireContext())
            1 -> BasketListAdapter(requireContext())
            else -> { null }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}