package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.noteapp.model.Note
import com.example.noteapp.model.NoteRepository
import com.example.noteapp.ui.NoteAppTheme

class NoteListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NoteList(notes = NoteRepository.all())
                }
            }
        }
    }
}

@Composable
fun NoteList(notes: List<Note>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(notes.sortedByDescending { it.created }) { note ->
            Text(
                text = note.text,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clickable { /* open note */ },
                maxLines = 1
            )
        }
    }
}
