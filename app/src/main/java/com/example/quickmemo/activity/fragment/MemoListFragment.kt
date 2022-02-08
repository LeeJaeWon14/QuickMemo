package com.example.quickmemo.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickmemo.R
import com.example.quickmemo.activity.adapter.recycler.BasketListAdapter
import com.example.quickmemo.activity.adapter.recycler.MemoListAdapter
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.activity.util.Logger
import com.example.quickmemo.databinding.FragmentMemoListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    0 -> {
                        val memoList = MemoRoomDatabase.getInstance(requireContext()).getMemoDAO().getMemoList()
                        if(checkEmpty(0, memoList.isEmpty())) MemoListAdapter(null)
                        else MemoListAdapter(memoList.toMutableList())
                    }
                    1 -> {
                        val memoList = MemoRoomDatabase.getInstance(requireContext()).getBasketDAO().getBasketList()
                        if(checkEmpty(1, memoList.isEmpty())) BasketListAdapter(null)
                        else BasketListAdapter(memoList.toMutableList())
                    }
                    else -> { null }
                }
                layoutManager = LinearLayoutManager(requireContext())
                adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        if (binding.rvMemoList.adapter?.itemCount!! < 2) {
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
            0 -> {
                val memoList = MemoRoomDatabase.getInstance(requireContext()).getMemoDAO().getMemoList()
                if(checkEmpty(0, memoList.isEmpty())) MemoListAdapter(null)
                else MemoListAdapter(memoList.toMutableList())
            }
            1 -> {
                val memoList = MemoRoomDatabase.getInstance(requireContext()).getBasketDAO().getBasketList()
                if(checkEmpty(1, memoList.isEmpty())) BasketListAdapter(null)
                else BasketListAdapter(memoList.toMutableList())
            }
            else -> { null }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun checkEmpty(memoList: List<*>, isEmpty: Boolean = memoList.isEmpty()) {
//        binding.apply {
//            rvMemoList.isVisible = !isEmpty
//            tvMemoIsEmpty.isVisible = isEmpty
//            if(tvMemoIsEmpty.isVisible) {
//                tvMemoIsEmpty.text = when(memoList[0]) {
//                    is MemoEntity -> {
//
//                        Logger.e("list is MemoEntity")
//                        getString(R.string.str_memo_is_empty)
//                    }
//                    is BasketEntity -> {
//                        BasketListAdapter(null)
//                        Logger.e("list is BasketEntity")
//                        getString(R.string.str_basket_is_empty)
//                    }
//                    else -> ""
//                }
//            }
//            else {
//                when(memoList) {
////                    is MemoActivity -> {}
////                    is BasketEntity -> BasketListAdapter()
//                }
//            }
//        }
//    }

    /**
     * @return true is empty list, false is not empty list
     */
    private fun checkEmpty(page: Int, isEmpty: Boolean = false) : Boolean {
        binding.apply {
            rvMemoList.isVisible = !isEmpty
            tvMemoIsEmpty.isVisible = isEmpty
            if(tvMemoIsEmpty.isVisible) {
                tvMemoIsEmpty.text = when(page) {
                    0 -> {
                        // when page is memo list.
                        Logger.e("list is MemoEntity")
                        getString(R.string.str_memo_is_empty)
                    }
                    1 -> {
                        // when page is basket list.
                        Logger.e("list is BasketEntity")
                        getString(R.string.str_basket_is_empty)
                    }
                    else -> ""
                }
                return true
            }
            else {
                return false
            }
        }
    }
}