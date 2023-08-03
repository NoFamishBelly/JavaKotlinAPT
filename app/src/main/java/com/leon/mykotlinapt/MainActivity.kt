package com.leon.mykotlinapt

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leon.ann.LeoKotlinClass
import com.leon.leon_annotation.LeonKotlinAnn
import com.leon.mykotlinapt.ui.theme.MyKotlinAptTheme

@LeonKotlinAnn(name = "Leo", data = " Kotlin apt 信息")
//@LeonJavaAnn(name = "Leo", data = " Java apt 信息")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyKotlinAptTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val msg = testKotlinApt()
//                    val msg = testJavaApt()
                    Log.i("TAG_ZLZ", msg)
                    Greeting(msg)
                }
            }
        }
    }
}


fun testKotlinApt(): String {
    val leo = LeoKotlinClass()
    return leo.getMessage()
}


//fun testJavaApt():String{
//    val leo = LeoClass()
//    return leo.message
//}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello: $name",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyKotlinAptTheme {
        Greeting("Android")
    }
}