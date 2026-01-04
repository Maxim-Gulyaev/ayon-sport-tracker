package com.maxim.settings.screen.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maxim.ui.component.AyonHorizontalSpacer
import com.maxim.ui.theme.AyonTypography
import com.maxim.ui.util.NoRippleInteractionSource

@Composable
internal fun SettingsCheckableItem(
    @StringRes displayNameRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .clickable(
                indication = null,
                interactionSource = NoRippleInteractionSource,
                onClick = onClick,
            )
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
        )

        AyonHorizontalSpacer(12.dp)

        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(displayNameRes),
            style = AyonTypography.bodyLarge,
        )
    }
}