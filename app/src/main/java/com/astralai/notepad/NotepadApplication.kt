package com.astralai.notepad

import android.app.Application
import timber.log.Timber

class NotepadApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(object : Timber.DebugTree() {

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                super.log(priority, "global_tag_$tag", message, t)
            }

            override fun createStackElementTag(element: StackTraceElement): String? {
                return String.format(
                    "(%s:%s)",
                    element.fileName,
                    element.lineNumber,
                    element.methodName,
                    super.createStackElementTag(element)
                )
            }
        })
        Timber.d("Inside app!")
    }
}