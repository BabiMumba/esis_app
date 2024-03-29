package com.Esisalama.babim.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.Esisalama.babim.R
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.admin.adpters.useradptr
import com.Esisalama.babim.admin.model.modeluser
import com.Esisalama.babim.users.SearchUserActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_allusers.*
import kotlinx.android.synthetic.main.activity_register.*

class AllusersActivity : AppCompatActivity() {

    private var layoutManager:LinearLayoutManager?= null
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var adaps: useradptr
    private companion object{
        private const val TAG = "REWARDED_INTER_TAG_"
    }
    private var mRewardedInterstitialAd : RewardedInterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allusers)

        MobileAds.initialize(this){
            Log.d(TAG,"Oncreate:")
        }
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("e3ecfe91-a277-4650-92e0-4f0cf2ad9c13","1bda7af6-ef75-48d9-a0d7-2ea9121c42e6"))
                .build()
        )
        // loadrewardedInters()
        tresor.setOnClickListener {
            showRewardedInters()
        }

        search_users.setOnClickListener {
            startActivity(Intent(this,SearchUserActivity::class.java))
        }
        firebaseAuth = FirebaseAuth.getInstance()
        layoutManager = LinearLayoutManager(this@AllusersActivity)
        users_recyclerview.layoutManager = layoutManager
        //Rreference des utilisateur
        val ref = FirebaseFirestore.getInstance().collection(Constant.Etudiant)
          //  .whereEqualTo("administrateur", "oui")
        val options = FirestoreRecyclerOptions.Builder<modeluser>()
            .setQuery(
                ref,
                modeluser::class.java
            )
            .build()
        adaps = useradptr(options)

        users_recyclerview.adapter = adaps

    }


    private fun loadrewardedInters() {
        RewardedInterstitialAd.load(
            this,
            resources.getString(R.string.rewarded_ad_reel),
            //AdRequest adRequest = new AdRequest.Builder().addTestDevice("94DF0193F80DB5F14BFF0EA958D02BC9").build();
            AdRequest.Builder().build(),

            object : RewardedInterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.d(TAG,"chargement echouer:${loadAdError.message}")
                    mRewardedInterstitialAd = null
                }

                override fun onAdLoaded(rewardedInterstitialAd: RewardedInterstitialAd) {
                    super.onAdLoaded(rewardedInterstitialAd)
                    Log.d(TAG,"OnLoaded")
                    tresor.visibility = View.VISIBLE
                    Toast.makeText(this@AllusersActivity, "video pret", Toast.LENGTH_SHORT).show()
                    mRewardedInterstitialAd = rewardedInterstitialAd
                }
            }

        )
    }
    private fun showRewardedInters(){

        if (mRewardedInterstitialAd != null){
            Log.d(TAG,"showRewarded")
            mRewardedInterstitialAd!!.fullScreenContentCallback =
                object: FullScreenContentCallback(){
                    override fun onAdClicked() {
                        super.onAdClicked()

                        Log.d(TAG,"onAdclicked")
                        Toast.makeText(this@AllusersActivity, "vous avez cliquez sur la pub", Toast.LENGTH_SHORT).show()

                    }
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        Toast.makeText(this@AllusersActivity, "Vous avez fermer ", Toast.LENGTH_SHORT).show()
                        Log.d(TAG,"onAddismissFullScreen: ")

                        mRewardedInterstitialAd = null
                        loadrewardedInters()

                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        super.onAdFailedToShowFullScreenContent(adError)
                        Log.d(TAG,"onAdfailedtoShowFullscreen: ${adError.message} ")
                        mRewardedInterstitialAd = null
                    }
                    override fun onAdImpression() {
                        super.onAdImpression()
                        Toast.makeText(this@AllusersActivity, "pub valider", Toast.LENGTH_SHORT).show()
                        Log.d(TAG,"onAdImpression: ")
                    }
                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        Log.d(TAG,"onAdShowFullScreencontente: ")
                    }

                }
            mRewardedInterstitialAd!!.show(this){
                //  ajouter()
                tresor.visibility = View.GONE
                Toast.makeText(this, "recompence accorder", Toast.LENGTH_SHORT).show()
                Log.d(TAG,"onUserEarnedrewarded: ")
            }
        }
        else{
            Log.d(TAG,"chargement echouer")
            Toast.makeText(this, "chargement echouer", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        adaps.startListening()
        users_recyclerview.recycledViewPool.clear()
        adaps.notifyDataSetChanged()
        super.onStart()
    }
    override fun onStop() {
        adaps.stopListening()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.search_bar, menu)
        val item = menu.findItem(R.id.search_etudian)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.search_etudian -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
