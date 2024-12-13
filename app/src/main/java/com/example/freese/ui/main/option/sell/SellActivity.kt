package com.example.freese.ui.main.option.sell

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.freese.R
import com.example.freese.data.ProductEntity
import com.example.freese.databinding.ActivitySellBinding
import com.example.freese.ui.ViewModelFactory

class SellActivity : AppCompatActivity() {

   companion object {
      const val EXTRA_PRODUCT = "EXTRA_PRODUCT"
      const val ALERT_DIALOG_CLOSE = 10
      const val ALERT_DIALOG_DELETE = 20
   }
   private var isEdit = false
   private var product: ProductEntity? = null

   private lateinit var binding: ActivitySellBinding
   private lateinit var sellViewModel: SellViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivitySellBinding.inflate(layoutInflater)
      setContentView(binding.root)

      sellViewModel = obtainViewModel(this@SellActivity)

      product = intent.getParcelableExtra(EXTRA_PRODUCT)
      binding.btnJual.setOnClickListener{
         val imageResId = "https://cdn.rri.co.id/berita/Meulaboh/o/1715939134907-47FFC1F9-C157-4FB3-A126-FFC3D6997C76/bd48p4ouqqa45dd.jpeg"
         val title = binding.etFoodName.text.toString()
         val description = binding.etDescription.text.toString()
         val price = binding.etPrice.text.toString()
         val seller = "admin"
         val cityName = "Bandung"
         val stock = binding.etStock.text.toString()
         val category = "buah"


         when {
            title.isEmpty() -> {
               binding.etFoodName.error = "Field can not be blank"
            }
            description.isEmpty() -> {
               binding.etDescription.error = "Field can not be blank"
            }
            price.isEmpty() -> {
               binding.etPrice.error = "Field can not be blank"
            }
            stock.isEmpty() -> {
               binding.etStock.error = "Field can not be blank"
            }
         else ->{
            product.let { product ->
               product?.id = 1
               product?.imageResId = imageResId
               product?.title = title
               product?.description = description
               product?.price = price
               product?.stok = stock
               product?.seller = seller
               product?.cityName = cityName
               product?.category = category
            }

            Log.d("Product", "onCreate: $product")
            sellViewModel.insert(product as ProductEntity)
            finish()
            }
         }
      }
   }

   private fun obtainViewModel(activity: AppCompatActivity): SellViewModel {
      val factory = ViewModelFactory.getInstance(activity.application)
      return ViewModelProvider(activity, factory)[SellViewModel::class.java]
   }

}