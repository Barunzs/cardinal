package com.barun.weather.ui.dashboard.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.barun.weather.R
import com.barun.weather.data.response.Resource
import com.barun.weather.data.response.album.Album
import com.barun.weather.databinding.FragmentDashBoardBinding
import com.barun.weather.ui.dashboard.DashBoardViewModel
import com.barun.weather.ui.dashboard.adapter.AlbumAdapter
import com.barun.weather.utils.getQueryTextChangeStateFlow
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dash_board.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.coroutines.CoroutineContext


@ExperimentalSerializationApi
@AndroidEntryPoint
class DashBoardFragment : Fragment(), CoroutineScope {

    private val TAG = DashBoardFragment::class.java.simpleName
    private var binding: FragmentDashBoardBinding? = null
    private val viewModel: DashBoardViewModel by activityViewModels()
    private var onAlbumItemClickListener = object : AlbumAdapter.IAlbumItemClickListener {
        override fun onAlbumItemClicked(album: Album) {
            viewModel.setSharedData(album)
            view?.findNavController()?.navigate(R.id.action_dashBoardFragment_to_albumDetailsFragment)
        }
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job

    var popularAlbumListAdapter: AlbumAdapter = AlbumAdapter(onAlbumItemClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAlbum()
        viewModel.albumResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    progressBarPoster.isVisible = false
                    Log.d(TAG, "Success Data::${it.value}")
                    popularAlbumListAdapter.setAdapterData(it.value)
                    binding?.popularAlbums?.setCategoryAdapter(
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        ), popularAlbumListAdapter
                    )
                }
                is Resource.Failure -> {
                    progressBarPoster.isVisible = false
                    Log.d(TAG, "Failure Data::${it.errorBody.toString()}")
                    binding?.root?.rootView?.let { it1 -> onSNACK(it1,it.errorBody.toString()) }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun onSNACK(view: View, error:String){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, error,
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLUE)
        textView.textSize = 28f
        snackbar.show()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate( R.menu.search, menu)
        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView
        launch {
            searchView.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
                        viewModel.getAlbum()
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emitAll(flowOf(null))
                        }
                }
                .flowOn(Dispatchers.Default)
                .collect { result ->
                    Log.d(TAG, "result::${result}")
                    //textViewResult.text = result
                    result?.let { popularAlbumListAdapter.setAdapterData(it) }
                }
        }
    }


    private fun dataFromNetwork(query: String): Flow<List<Album>?> {
        return flow {
            delay(100)
            val result  = popularAlbumListAdapter.getAdapterData().find {
                it?.title?.startsWith(query,true) == true
            }
            result?.let {
                emit(mutableListOf(it))
            }
        }
    }

}