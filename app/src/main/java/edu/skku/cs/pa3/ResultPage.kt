package edu.skku.cs.pa3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class ResultPage : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_page)

        val tv_isgweight = findViewById<TextView>(R.id.textView_goalweightis)
        val tv_hellouser = findViewById<TextView>(R.id.textView_hellouser)
        val tv_gweight = findViewById<TextView>(R.id.textView_goalweight)
        val tv_gcalory = findViewById<TextView>(R.id.textView_goalcalory)
        val tv_bmr = findViewById<TextView>(R.id.textView_BMR)

        val isweightloss = intent.getBooleanExtra(MainPage.isweightloss, false)
        val ismild = intent.getBooleanExtra(MainPage.ismild, false)
        val isnormal = intent.getBooleanExtra(MainPage.isnormal, false)

        val items = ArrayList<Meals>()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userid = currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().reference


        val userRef = database.child("users").child(userid)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue(User::class.java)
                if (userData != null) {
                    val username = userData.username
                    val usergender = userData.gender
                    val userage = userData.age.toString()
                    val userheight = userData.height.toString()
                    val userweight = userData.weight.toString()

                    val urllink = "https://fitness-calculator.p.rapidapi.com/dailycalorie?age=$userage&gender=$usergender&height=$userheight&weight=$userweight&activitylevel=level_1"
                    Log.v("tag", "url link is ${urllink}")
                    val req = Request.Builder()
                        .url(urllink)
                        .get()
                        .addHeader("X-RapidAPI-Key", "7b4f3b0e8dmshdc637e8cdd29985p1c3caejsn2a8134c0e6b3")
                        .addHeader("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
                        .build()

                    val client = OkHttpClient()

                    client.newCall(req).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            response.use {
                                if(!response.isSuccessful) throw IOException("Unexpected code $response")
                                val str = response.body!!.string()
                                val data = Gson().fromJson(str, CalorieResponse::class.java)
                                CoroutineScope(Dispatchers.Main).launch {

                                    tv_hellouser.text = "Hello, $username!!\nYour fitness info are shown below"
                                    tv_bmr.text = data.data.BMR.toString()

                                    if(isweightloss) {
                                        if(ismild) {
                                            tv_gweight.text = data.data.goals.Mild_weight_loss.loss_weight
                                            tv_gcalory.text = data.data.goals.Mild_weight_loss.calory.toString()
                                            items.add(Meals(1, "Breakfast", "Scrambled Egg Whites with Vegetables" +
                                                    "\n1 slice of Whole Grain Toast\nGreek Yogurt\n1 cup of Berries"))
                                            items.add(Meals(2, "Lunch", "Grilled Chicken Salad" +
                                                    "\n1 cup of Quinoa\n1 cup of Steamed Broccoli"))
                                            items.add(Meals(3, "Dinner", "Baked Salmon" +
                                                    "\n1 cup of Roasted Vegetables\n1 cup of Brown Rice"))
                                        }
                                        else if(isnormal) {
                                            tv_gweight.text = data.data.goals.Weight_loss.loss_weight
                                            tv_gcalory.text = data.data.goals.Weight_loss.calory.toString()
                                            items.add(Meals(1, "Breakfast", "1 boiled egg" +
                                                    "\n1 slice of whole wheat toast\n1 small apple\n1 cup of black coffee or unsweetened tea"))
                                            items.add(Meals(2, "Lunch", "Grilled chicken breast" +
                                                    "\n1 cup of mixed green salad with vegetables\n1 small orange\n1 cup of water"))
                                            items.add(Meals(3, "Dinner", "Baked salmon fillet" +
                                                    "\n1 cup of steamed broccoli\n1 cup of roasted sweet potatoes\n1 cup of water"))
                                        }
                                        else {
                                            tv_gweight.text = data.data.goals.Extreme_weight_loss.loss_weight
                                            tv_gcalory.text = data.data.goals.Extreme_weight_loss.calory.toString()
                                            items.add(Meals(1, "Breakfast", "1 hard-boiled egg" +
                                                    "\n1 slice of whole wheat bread\n1/2 avocado\n1 cup of black coffee or unsweetened tea"))
                                            items.add(Meals(2, "Lunch", "Grilled chicken breast" +
                                                    "\n1 cup of mixed greens with lemon juice\n1/4 cup of cherry tomatoes\n1/4 cup of cucumber slices" +
                                                    "\n1 tablespoon of balsamic vinegar dressing\n1 cup of water"))
                                            items.add(Meals(3, "Dinner", "Baked white fish fillet" +
                                                    "\n1 cup of steamed broccoli\n1/2 cup of steamed carrots\n1/2 cup of cauliflower rice\n1 cup of water"))
                                        }
                                    }
                                    else {
                                        if(ismild) {
                                            tv_gweight.text = data.data.goals.Mild_weight_gain.gain_weight
                                            tv_gcalory.text = data.data.goals.Mild_weight_gain.calory.toString()
                                            items.add(Meals(1, "Breakfast", "2 scrambled eggs" +
                                                    "\n2 slices of whole wheat toast\n1 tablespoon of peanut butter\n1 medium-sized banana"))
                                            items.add(Meals(2, "Lunch", "Grilled chicken breast" +
                                                    "\n1 cup of cooked quinoa\nSteamed vegetables\n1 tablespoon of olive oil dressing"))
                                            items.add(Meals(3, "Dinner", "Salmon fillet" +
                                                    "\n1 cup of brown rice\nRoasted sweet potatoes\nMixed green salad with vinaigrette dressing"))
                                        }
                                        else if(isnormal) {
                                            tv_gweight.text = data.data.goals.Weight_gain.gain_weight
                                            tv_gcalory.text = data.data.goals.Weight_gain.calory.toString()
                                            items.add(Meals(1, "Breakfast", "Vegetable omelette" +
                                                    "\n1 slice of whole wheat toast\n1 small apple"))
                                            items.add(Meals(2, "Lunch", "Grilled shrimp skewers with zucchini and cherry tomatoes" +
                                                    "\nblack bean salad with diced tomatoes, red onions, corn, and a squeeze of lime juice" +
                                                    "\n"))
                                            items.add(Meals(3, "Dinner", "Baked salmon with lemon and dill" +
                                                    "\n1 cup of steamed asparagus\n1/2 cup of quinoa"))
                                        }
                                        else {
                                            tv_gweight.text = data.data.goals.Extreme_weight_gain.gain_weight
                                            tv_gcalory.text = data.data.goals.Extreme_weight_gain.calory.toString()
                                            items.add(Meals(1, "Breakfast", "2 large whole eggs scrambled with cheese" +
                                                    "\n2 slices of whole wheat toast with peanut butter\n1 banana\n1 cup of whole milk"))
                                            items.add(Meals(2, "Lunch", "Grilled chicken breast sandwich on whole wheat bread with avocado, lettuce, and tomato" +
                                                    "\n1 serving of sweet potato fries\n1 cup of mixed vegetables\n1 glass of fruit juice"))
                                            items.add(Meals(3, "Dinner", "8oz steak cooked with users preference" +
                                                    "\n1 cup of cooked white rice\n1 serving of roasted vegetables\n1 glass of whole milk"))
                                        }
                                    }
                                    val listview = findViewById<ListView>(R.id.listview)
                                    val customAdapter = MealsAdapter(items, this@ResultPage)
                                    listview.adapter=customAdapter
                                }
                            }
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur during data retrieval
            }
        })







    }
}