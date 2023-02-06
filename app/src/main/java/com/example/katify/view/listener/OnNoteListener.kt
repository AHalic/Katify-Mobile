package com.example.katify.view.listener

import com.example.katify.data.model.Note

/**
 * Implements [onClick] receiving [Note]
 */
interface OnNoteListener {
    fun onClick(note: Note)
}