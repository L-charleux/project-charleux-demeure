package com.ismin.csproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_POIS = "pois"

class PoIListFragment : Fragment() {
    private lateinit var pois: ArrayList<PoI>
    private lateinit var adapter : PoIAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val argPoIs = requireArguments().getSerializable(ARG_POIS) as ArrayList<PoI>?
        pois = argPoIs ?: ArrayList()
        adapter = PoIAdapter(pois)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_poi_list, container, false)

        val rcvPoIs = view.findViewById<RecyclerView>(R.id.f_poi_list_rcv_pois)
        rcvPoIs.layoutManager = LinearLayoutManager(context)
        rcvPoIs.adapter = adapter

        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance(pois: ArrayList<PoI>) =
            PoIListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_POIS, pois)
                }
            }
    }
}