package edu.skku.cs.pa3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class MealsAdapter(val data : ArrayList<Meals>, val context : Context) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val generatedView = inflater.inflate(R.layout.meals, null)

        val thumbnail = generatedView.findViewById<ImageView>(R.id.thumbnail)
        val mealname = generatedView.findViewById<TextView>(R.id.mealname)
        val mealmessage = generatedView.findViewById<TextView>(R.id.mealmessage)

        mealname.text = data[p0].mealname
        mealmessage.text = data[p0].mealmessage

        if(data[p0].thumbnail==1) {
            thumbnail.setImageResource(R.drawable.morning)
        }
        else if(data[p0].thumbnail==2) {
            thumbnail.setImageResource(R.drawable.noon)
        }
        else {
            thumbnail.setImageResource(R.drawable.night)
        }


        return generatedView
    }
}