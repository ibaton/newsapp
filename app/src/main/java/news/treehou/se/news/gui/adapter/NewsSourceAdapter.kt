package news.treehou.se.news.gui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import news.treehou.se.news.R
import news.treehou.se.news.model.NewsSource

/**
 * Adapter that is used to select news sources that should be used.
 */
class NewsSourceAdapter:RecyclerView.Adapter<NewsSourceAdapter.ViewHolder>() {

    private val items: MutableList<NewsSource> = mutableListOf()
    private val checkChangedSubject = PublishSubject.create<Pair<NewsSource, Boolean>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_news_source, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addSources(sources: List<NewsSource>){
        items.clear()
        items.addAll(sources)
        notifyDataSetChanged()
    }

    /**
     * Get observable that sends events when an items check state is changed.
     */
    fun getCheckChangedFlowable(): Flowable<Pair<NewsSource, Boolean>>{
        return checkChangedSubject.toFlowable(BackpressureStrategy.BUFFER)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val watchCheckBox = view.findViewById<CheckBox>(R.id.nameCheckBox)

        fun bind(source: NewsSource){
            watchCheckBox.text = source.name

            watchCheckBox.setOnCheckedChangeListener(null)
            watchCheckBox.isChecked = source.watched
            watchCheckBox.setOnCheckedChangeListener(checkChangeListener)
        }

        private val checkChangeListener = { _: View , isChecked: Boolean ->
            checkChangedSubject.onNext(Pair(items[layoutPosition], isChecked))
        }
    }
}