package com.astralai.notepad.feature_note.presentation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.astralai.notepad.feature_note.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    cutCornerSize: Dp = 32.dp,
    onDeleteClick: () -> Unit,
) {
    Box(modifier = modifier, content = {
        Canvas(modifier = Modifier.matchParentSize(), onDraw = {
            val clipPath = Path().apply {
                lineTo(x = size.width - cutCornerSize.toPx(), y = 0f)
                lineTo(x = size.width, y = cutCornerSize.toPx())
                lineTo(x = size.width, y = size.height)
                lineTo(x = 0f, y = size.height)
                close()
            }
            clipPath(clipPath) {
                drawRoundRect(color = Color(note.color),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(ColorUtils.blendARGB(note.color, Color.Black.toArgb(), 0.2f)),
                    topLeft = Offset(x = size.width - cutCornerSize.toPx(), y = -100f),
                    size = Size(
                        width = cutCornerSize.toPx() + 100f,
                        height = cutCornerSize.toPx() + 100f
                    ),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 32.dp
                )
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = onDeleteClick, modifier = Modifier.align(Alignment.BottomEnd)) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete note")
        }
    })
}