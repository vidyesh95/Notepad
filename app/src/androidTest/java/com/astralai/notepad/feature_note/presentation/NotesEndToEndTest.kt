package com.astralai.notepad.feature_note.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.astralai.notepad.core.util.TestTags
import com.astralai.notepad.di.AppModule
import com.astralai.notepad.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.astralai.notepad.feature_note.presentation.notes.NotesScreen
import com.astralai.notepad.feature_note.presentation.util.Screen
import com.astralai.notepad.ui.theme.NotepadTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            NotepadTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(name = "noteId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(name = "noteColor") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(navController = navController, noteColor = color)
                        }
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_editAfterwards() {
        /**
         * Click on FloatingActionButton to add note screen
         * */
        //NotesScreen.kt
        composeRule.onNodeWithContentDescription("Add note").performClick()

        /**
         * Enter texts in title and content text fields
         * */
        //AddEditNoteScreen.kt & TransparentHintTextField.kt
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("test Title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).performTextInput("test Content")
        /**
         * Save the note
         * */
        composeRule.onNodeWithContentDescription("Save note").performClick()

        /**
         * Check whether note with our title and content is present
         * */
        composeRule.onNodeWithText("test Title").assertIsDisplayed()
        composeRule.onNodeWithText("test Content").assertIsDisplayed()
        /**
         * Click on note to edit it
         * */
        composeRule.onNodeWithText("test Title").performClick()

        /**
         * Check whether note and content text fields contain note title and content
         * */
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).assertTextEquals("test Title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).assertTextEquals("test Content")
        /**
         * Add the text "1000" to title text field
         * */
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("1000")
        /**
         * Save the note to update
         * */
        composeRule.onNodeWithContentDescription("Save note").performClick()

        /**
         * Check whether update was applied to the list
         * */
        composeRule.onNodeWithText("test Title1000").assertIsDisplayed()
    }

    @Test
    fun saveNewNotes_orderByTitleDescending() {
        for (i in 1..3) {
            /**
             * Click on FloatingActionButton to add note screen
             * */
            //NotesScreen.kt
            composeRule.onNodeWithContentDescription("Add note").performClick()

            /**
             * Enter texts in title and content text fields
             * */
            //AddEditNoteScreen.kt & TransparentHintTextField.kt
            composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput(i.toString())
            composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).performTextInput(i.toString())
            /**
             * Save the note
             * */
            composeRule.onNodeWithContentDescription("Save note").performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithContentDescription("Title").performClick()
        composeRule.onNodeWithContentDescription("Descending").performClick()
        composeRule.onNodeWithContentDescription("Sort").performClick()

        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0].assertTextContains("3")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1].assertTextContains("2")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2].assertTextContains("1")
    }
}