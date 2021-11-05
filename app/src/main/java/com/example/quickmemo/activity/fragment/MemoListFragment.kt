package com.example.quickmemo.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickmemo.activity.adapter.MemoListAdapter
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.databinding.FragmentMemoListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

        CoroutineScope(Dispatchers.IO).launch {
            val memoList = MemoRoomDatabase.getInstance(requireContext()).getMemoDAO()
                .getMemoList()
            withContext(Dispatchers.Main) {
                binding.rvMemoList.adapter = MemoListAdapter(memoList)
                binding.rvMemoList.layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}