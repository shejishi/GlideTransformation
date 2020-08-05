package com.ellison.glide.transformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ellison.glide.transformation.bean.RecyclerBean
import com.ellison.glide.transformation.utils.DensityUtil
import com.ellison.glide.translibrary.ImageUtils
import com.ellison.glide.translibrary.base.LoaderBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val pic = "https://img.lovebuy99.com/uploads/allimg/200731/15-200I1222359.jpg"
    }

    var listData = arrayListOf<RecyclerBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listData.add(RecyclerBean(pic, DensityUtil.dp2px(100f), DensityUtil.dp2px(100f), true))


        listData.add(
            RecyclerBean(
                pic, DensityUtil.dp2px(150f), DensityUtil.dp2px(100f), false,
                leftTopRound = true,
                leftBottomRound = true,
                rightTopRound = true,
                rightBottomRound = true,
                roundCorners = DensityUtil.dp2px(10f)
            )
        )

        listData.add(
            RecyclerBean(
                pic, DensityUtil.dp2px(100f), DensityUtil.dp2px(100f), true,
                borderWidth = DensityUtil.dp2px(2f),
                borderColor = ContextCompat.getColor(this, R.color.colorAccent)
            )
        )

        listData.add(
            RecyclerBean(
                pic, DensityUtil.dp2px(150f), DensityUtil.dp2px(100f), false,
                leftTopRound = true,
                leftBottomRound = true,
                rightTopRound = true,
                rightBottomRound = true,
                roundCorners = DensityUtil.dp2px(10f),
                borderWidth = DensityUtil.dp2px(2f),
                borderColor = ContextCompat.getColor(this, R.color.colorAccent)
            )
        )

        listData.add(
            RecyclerBean(
                pic, DensityUtil.dp2px(50f), DensityUtil.dp2px(80f), false,
                leftTopRound = false,
                leftBottomRound = true,
                rightTopRound = true,
                rightBottomRound = true,
                roundCorners = DensityUtil.dp2px(10f)
            )
        )

        listData.add(
            RecyclerBean(
                pic, DensityUtil.dp2px(70f), DensityUtil.dp2px(100f), false,
                leftTopRound = true,
                leftBottomRound = false,
                rightTopRound = true,
                rightBottomRound = true,
                roundCorners = DensityUtil.dp2px(10f),
                borderWidth = DensityUtil.dp2px(2f),
                borderColor = ContextCompat.getColor(this, R.color.colorAccent)
            )
        )

        listData.add(
            RecyclerBean(
                pic, DensityUtil.dp2px(120f), DensityUtil.dp2px(200f), false,
                leftTopRound = true,
                leftBottomRound = false,
                rightTopRound = false,
                rightBottomRound = true,
                roundCorners = DensityUtil.dp2px(10f),
                borderWidth = DensityUtil.dp2px(2f),
                borderColor = ContextCompat.getColor(this, R.color.colorAccent)
            )
        )

        listData.add(
            RecyclerBean(
                pic, DensityUtil.dp2px(120f), DensityUtil.dp2px(200f), false,
                leftTopRound = false,
                leftBottomRound = false,
                rightTopRound = false,
                rightBottomRound = true,
                roundCorners = DensityUtil.dp2px(10f)
            )
        )


        recycler_view.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val adapter = RecyclerAdapter(listData)
        recycler_view.adapter = adapter
    }
}

class RecyclerAdapter(private var listData: MutableList<RecyclerBean>) :
    RecyclerView.Adapter<RecyclerHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reycycler, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val recyclerBean = listData[position]
        val builderConfig = LoaderBuilder()
            .round(recyclerBean.roundCorners.toFloat())
            .circle(recyclerBean.isRound)
            .width(recyclerBean.picWidth)
            .height(recyclerBean.picHeight)
            .round(
                floatArrayOf(
                    if (recyclerBean.leftTopRound) recyclerBean.roundCorners.toFloat() else 0f,
                    if (recyclerBean.rightTopRound) recyclerBean.roundCorners.toFloat() else 0f,
                    if (recyclerBean.leftBottomRound) recyclerBean.roundCorners.toFloat() else 0f,
                    if (recyclerBean.rightBottomRound) recyclerBean.roundCorners.toFloat() else 0f
                )
            )
            .borderColor(recyclerBean.borderColor)
            .borderWidth(recyclerBean.borderWidth)
            .load(recyclerBean.pic)

        ImageUtils.getInstance().bind(holder.iv, builderConfig)
    }

}

class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val iv: ImageView = itemView.findViewById(R.id.iv)
}