package com.unitec.agrohack.ui.menus

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainMenu {
    @Composable
    fun Ejemplo() {
                TextSample("Android")
    }
}


@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    MainMenu()
}

@Composable
fun TextSample(software: String) {
    Text(text = software)
}