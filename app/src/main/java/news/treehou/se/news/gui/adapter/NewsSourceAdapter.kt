package news.treehou.se.news.gui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import news.treehou.se.news.R
import news.treehou.se.news.model.NewsSource

/**
 * Adapter that is used to select news sources that should be used.
 */
class NewsSourceAdapter : RecyclerView.Adapter<NewsSourceAdapter.ViewHolder>() {

    private val items: MutableList<NewsSourceAdapterItem> = mutableListOf()
    private val checkChangedSubject = PublishSubject.create<Pair<NewsSource, Boolean>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        if (viewType == VIEW_TYPE_LABEL) {
            val view = inflater.inflate(R.layout.item_news_label, parent, false)
            return LabelViewHolder(view)
        }

        val view = inflater.inflate(R.layout.item_news_source, parent, false)
        return SourceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        if (item is LabelAdapterItem) {
            return VIEW_TYPE_LABEL
        }

        return VIEW_TYPE_SOURCE
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        if (holder is LabelViewHolder && item is LabelAdapterItem) {
            holder.bind(item.label)
        } else if (holder is SourceViewHolder && item is SourceAdapterItem) {
            holder.bind(item.source)
        }
    }

    /**
     * TODO add diff util sort data set and make animations nicer.
     * Cleared old data and updates with provided items.
     *
     * @param newItems the items to populate adapter with
     */
    fun updateSources(newItems: List<NewsSourceAdapterItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    /**
     * Get observable that sends events when an items check state is changed.
     */
    fun getCheckChangedFlowable(): Flowable<Pair<NewsSource, Boolean>> {
        return checkChangedSubject.toFlowable(BackpressureStrategy.BUFFER)
    }

    abstract inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    inner class SourceViewHolder(view: View) : ViewHolder(view) {
        private val watchCheckBox = view.findViewById<CheckBox>(R.id.nameCheckBox)

        fun bind(source: NewsSource) {
            watchCheckBox.text = source.name
            watchCheckBox.setOnCheckedChangeListener(null)
            watchCheckBox.isChecked = source.watched
            watchCheckBox.setOnCheckedChangeListener(checkChangeListener)
        }

        private val checkChangeListener = { _: View, isChecked: Boolean ->
            val item = items[layoutPosition]
            if (item is SourceAdapterItem) {
                checkChangedSubject.onNext(Pair(item.source, isChecked))
            }
        }
    }

    inner class LabelViewHolder(view: View) : ViewHolder(view) {
        private val labelView = view.findViewById<TextView>(R.id.label)

        fun bind(label: String) {
            labelView.text = label
        }
    }


    abstract class NewsSourceAdapterItem

    /**
     * A item that renders as a label by the adapter
     * @param the label to show
     */
    class LabelAdapterItem(val label: String) : NewsSourceAdapterItem()

    /**
     * A item that renders a news source by the adapter
     * @param the label to show
     */
    class SourceAdapterItem(val source: NewsSource) : NewsSourceAdapterItem()

    companion object {
        private const val VIEW_TYPE_LABEL = 1
        private const val VIEW_TYPE_SOURCE = 2
    }
}