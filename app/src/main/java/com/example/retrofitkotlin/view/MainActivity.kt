package com.example.retrofitkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.retrofitkotlin.model.CryptoModel

import com.example.retrofitkotlin.adapter.CryptoAdapter
import com.example.retrofitkotlin.databinding.ActivityMainBinding
import com.example.retrofitkotlin.service.CryptoApi
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(),CryptoAdapter.Listener {
    private lateinit var cryptoList:ArrayList<CryptoModel>
    private lateinit var cryptoAdapter: CryptoAdapter
    private lateinit var binding: ActivityMainBinding
    private val BASE_URL="https://raw.githubusercontent.com/"
    private lateinit var compositeDisposable:CompositeDisposable
    private lateinit var job:Job
    val exceptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        compositeDisposable = CompositeDisposable()
        binding.recylerView.layoutManager=LinearLayoutManager(this)

        loadData()
        //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json




    }
    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoApi::class.java)

        job = CoroutineScope(Dispatchers.IO ).launch {
            val response = retrofit.getData()

            withContext(Dispatchers.Main + exceptionHandler){
                if(response.isSuccessful) {
                    response.body()?.let {
                        cryptoList = ArrayList(it)
                        cryptoList?.let {
                            cryptoAdapter = CryptoAdapter(it,this@MainActivity)
                            binding.recylerView.adapter = cryptoAdapter
                        }
                    }
                }
            }
        }


        /*
        compositeDisposable.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))*/






       /*
        var service =retrofit.create(CryptoApi::class.java)

        val call=service.getData()

        call.enqueue(object: Callback<List<CryptoModel>>{
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        cryptoList= ArrayList(it)
                        cryptoAdapter =CryptoAdapter(cryptoList)
                        binding.recylerView.adapter=cryptoAdapter

                    }


                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                Toast.makeText(this@MainActivity,t.localizedMessage,Toast.LENGTH_LONG).show()
            }

        })*/

    }
    /*fun handleResponse( cryptoModellist:List<CryptoModel>){
        cryptoList=ArrayList(cryptoModellist)
        cryptoList?.let{
            cryptoAdapter= CryptoAdapter(cryptoList)
            binding.recylerView.adapter=cryptoAdapter
        }
    }
*/
    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        //compositeDisposable.clear()
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(applicationContext,"Clicked on: ${cryptoModel.currency}",Toast.LENGTH_SHORT).show()
    }


}