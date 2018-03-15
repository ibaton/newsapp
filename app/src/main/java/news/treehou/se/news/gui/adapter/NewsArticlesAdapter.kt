package news.treehou.se.news.gui.adapter

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_news_article.view.*
import news.treehou.se.news.R
import news.treehou.se.news.model.NewsArticle
import news.treehou.se.news.model.NewsSource

/**
 * Adapter that is used to show news articles.
 */
class NewsArticlesAdapter:RecyclerView.Adapter<NewsArticlesAdapter.ViewHolder>() {

    private val items: MutableList<NewsArticle> = mutableListOf()
    private val articleSelectedSubject = PublishSubject.create<NewsArticle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_news_article, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addArticles(articles: List<NewsArticle>){
        items.clear()
        items.addAll(articles)
        notifyDataSetChanged()
    }

    /**
     * Get observable that sends events when an items check state is changed.
     */
    fun getArticleSelectedFlowable(): Flowable<NewsArticle>{
        return articleSelectedSubject.toFlowable(BackpressureStrategy.BUFFER)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleView = view.findViewById<TextView>(R.id.title)
        private val descriptionView = view.findViewById<TextView>(R.id.description)
        private val authorView = view.findViewById<TextView>(R.id.author)
        private val dateView = view.findViewById<TextView>(R.id.date)
        private val imageView = view.findViewById<ImageView>(R.id.image)

        init {
            itemView.setOnClickListener {
                articleSelectedSubject.onNext(items[layoutPosition])
            }
        }

        fun bind(article: NewsArticle){
            updateVisibility(article)

            titleView.text = article.title
            descriptionView.text = article.description
            dateView.text = article.publishedAt
            setAuthorText(article)

            loadImage(article)
        }

        private fun loadImage(article: NewsArticle){
            if(article.urlToImage != null) {
                Picasso.get().load(article.urlToImage)
                            .into(imageView)
            } else {
                Picasso.get().cancelRequest(imageView)
            }
        }

        private fun setAuthorText(article: NewsArticle){
            val authorString = if(article.author != null) {
                itemView.context.getString(R.string.source_author_text, article.source.name, article.author)
            } else {
                itemView.context.getString(R.string.source_text, article.source.name)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                authorView.text = Html.fromHtml(authorString, Html.FROM_HTML_MODE_LEGACY)
            } else {
                authorView.text = Html.fromHtml(authorString)
            }
        }

        fun updateVisibility(article: NewsArticle){
            dateView.visibility = if(article.publishedAt != null) View.VISIBLE else View.GONE
            imageView.visibility = if(article.urlToImage != null) View.VISIBLE else View.GONE
        }
    }
}