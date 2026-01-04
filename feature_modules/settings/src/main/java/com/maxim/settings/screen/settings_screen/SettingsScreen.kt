package com.maxim.settings.screen.settings_screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxim.settings.R
import com.maxim.settings.screen.component.SettingsTopAppBar
import com.maxim.ui.component.AyonHorizontalSpacer
import com.maxim.ui.theme.AyonTheme
import com.maxim.ui.theme.AyonTypography
import com.maxim.ui.util.AdaptivePreviewDark
import com.maxim.ui.util.AdaptivePreviewLight
import com.maxim.ui.util.NoRippleInteractionSource

@Composable
fun SettingsScreen(
    onLanguageClick: () -> Unit,
    onDarkThemeClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsScreenContent(
        onLanguageClick = onLanguageClick,
        onDarkThemeClick = onDarkThemeClick,
        onBackClick = onBackClick,
    )
}

@Composable
private fun SettingsScreenContent(
    onLanguageClick: () -> Unit,
    onDarkThemeClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            SettingsTopAppBar(
                titleRes = R.string.top_app_bar_settings,
                onBackClick = onBackClick,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                item {
                    SettingsItem(
                        titleRes = R.string.language,
                        iconRes = R.drawable.ic_language,
                        onClick = onLanguageClick
                    )
                }

                item {
                    SettingsItem(
                        titleRes = R.string.dark_mode_settings,
                        iconRes = R.drawable.ic_dark_mode,
                        onClick = onDarkThemeClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    @StringRes titleRes: Int,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = NoRippleInteractionSource,
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = "Language icon",
        )
        AyonHorizontalSpacer(12.dp)
        Text(
            modifier = modifier
                .padding(horizontal = 12.dp, vertical = 12.dp),
            text = stringResource(titleRes),
            style = AyonTypography.titleMedium
        )
    }
}

@AdaptivePreviewDark
@Preview
@Composable
private fun PreviewSettingsScreenDark() {
    AyonTheme() {
        SettingsScreenContent(
            onLanguageClick = {},
            onDarkThemeClick = {},
            onBackClick = {}
        )
    }
}

@AdaptivePreviewLight
@Preview
@Composable
private fun PreviewSettingsScreenLight() {
    AyonTheme {
        SettingsScreenContent(
            onLanguageClick = {},
            onDarkThemeClick = {},
            onBackClick = {}
        )
    }
}
