package com.example.freese.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.freese.DependencyProvider
import com.example.freese.GenericViewModelFactory
import com.example.freese.R
import com.example.freese.databinding.ActivityMainBinding
import com.example.freese.ui.main.favorite.FavoriteFragment
import com.example.freese.ui.main.home.HomeFragment
import com.example.freese.ui.main.option.OptionFragment
import com.example.freese.ui.main.scan.ScanActivity
import com.example.freese.ui.auth.AuthViewModel
import com.example.freese.ui.main.scan.ScanActivity.Companion.CAMERAX_RESULT

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: AuthViewModel
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        mainViewModel = ViewModelProvider(
            this,
            GenericViewModelFactory {
                AuthViewModel(
                    DependencyProvider.provideUserRepository(this)
                )
            }
        )[AuthViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.navigation_favorite -> {
                    replaceFragment(FavoriteFragment())
                    true
                }

                R.id.navigation_scan -> {
                    startCameraX()
                    true
                }

                R.id.navigation_option -> {
                    replaceFragment(OptionFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun startCameraX() {
        val intent = Intent(this, ScanActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(ScanActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
        }
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}