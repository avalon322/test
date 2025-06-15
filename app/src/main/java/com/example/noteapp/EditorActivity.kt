package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.consume
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.noteapp.model.Note
import com.example.noteapp.model.NoteRepository
import com.example.noteapp.ui.NoteAppTheme

class EditorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NoteEditor(onSave = { note ->
                        NoteRepository.save(note)
                        finish()
                    })
                }
            }
        }
    }
}

@Composable
fun NoteEditor(onSave: (Note) -> Unit) {
    var text by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Black) }
    var scale by remember { mutableStateOf(1f) }
    var currentPath by remember { mutableStateOf(Path()) }
    val paths = remember { mutableStateListOf<Pair<Path, Color>>() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.background(Color.White)
        )
        Canvas(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        scale *= zoom
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPath.moveTo(offset.x / scale, offset.y / scale)
                        },
                        onDrag = { change, _ ->
                            currentPath.lineTo(change.position.x / scale, change.position.y / scale)
                            change.consume()
                        },
                        onDragEnd = {
                            paths.add(currentPath to color)
                            currentPath = Path()
                        }
                    )
                }
        ) {
            withTransform({ scale(scale, scale) }) {
                for ((p, c) in paths) {
                    drawPath(p, color = c, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
                }
                drawPath(currentPath, color = color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
            }
        }
        Button(onClick = { onSave(Note(text = text, paths = paths.map { it.first to it.second })) }) {
            Text("Сохранить")
        }
    }
}
