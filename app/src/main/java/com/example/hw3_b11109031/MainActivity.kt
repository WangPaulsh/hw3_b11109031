package com.example.hw3_b11109031

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hw3_b11109031.ui.theme.Hw3_b11109031Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hw3_b11109031Theme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val viewModel: MyViewModel = viewModel()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainView(
                onPhotoPickerClick = {
                    navController.navigate("photoPicker")
                },
                onTakePhotoClick = {
                    navController.navigate("takePhoto")
                },
                viewModel = viewModel
            )
        }
        composable("photoPicker") {
            PhotoPickerScreen(
                onPhotoSelected = { uri ->
                    viewModel.setImageUri(uri)
                    navController.navigate("editPhoto")
                }
            )
        }
        composable("takePhoto") {
            TakePhotoScreen(
                onPhotoTaken = { uri ->
                    viewModel.setImageUri(uri)
                    navController.navigate("editPhoto")
                }
            )
        }
        composable("editPhoto") {
            DetailView(
                viewModel = viewModel,
                onProcessCompleted = {
                    viewModel.enqueueImageProcessingWork(context)
                    navController.navigate("main")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Hw3_b11109031Theme {
        MyApp()
    }
}