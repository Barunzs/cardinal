package com.barun.weather.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barun.weather.R
import com.barun.weather.data.response.album.Album
import com.barun.weather.databinding.ItemAlbumInfoBinding
import com.barun.weather.utils.loadUrl

class AlbumAdapter(val listener: IAlbumItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mTAG = AlbumAdapter::class.java.simpleName
    private var mAlbumList = mutableListOf<Album?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_album_info, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mAlbumList[position]?.let { (holder as ItemViewHolder).bindItemInfo(it) }
    }

    override fun getItemCount(): Int {
        return mAlbumList.size
    }

    fun setAdapterData(album: List<Album?>) {
        mAlbumList.clear()
        mAlbumList.addAll(album)
        notifyDataSetChanged()
    }

    fun getAdapterData():List<Album?>{
        return mAlbumList
    }

    interface IAlbumItemClickListener {
        fun onAlbumItemClicked(album: Album)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemAlbumInfoBinding.bind(view)

        fun bindItemInfo(album: Album) {
            binding.tvMovieTitle.text = album.title ?: "No Title"
            album.url.run {
                binding.ivAlbumPoster.loadUrl(this)
            }
            binding.root.setOnClickListener {
                listener.onAlbumItemClicked(album)
            }
        }
    }

}
