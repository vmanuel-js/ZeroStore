package com.example.zero.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.zero.R
import com.example.zero.ui.theme.Background
import com.example.zero.ui.theme.BlackIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZeroTopBar(
    title: String = "",
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Background,
            navigationIconContentColor = BlackIcon,
            actionIconContentColor = BlackIcon,
            titleContentColor = BlackIcon
        ),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.volver)
                )
            }
        },
        title = {
            if (title.isNotEmpty()) {
                Text(text = title, fontWeight = FontWeight.Bold)
            }
        },
        actions = actions
    )
}