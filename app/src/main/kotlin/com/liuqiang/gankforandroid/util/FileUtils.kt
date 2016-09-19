package com.liuqiang.gankforandroid.util

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

object FileUtils {

    fun getStringFromFile(filePath: String, charsetName: String): String? {
        return noThrow{
            BufferedReader(InputStreamReader(FileInputStream(File(filePath)), charsetName)).readText()
        }
    }

    fun getStringFromResource(cls: Class<*>, name: String, charsetName: String): String? {
        return noThrow{
            BufferedReader(InputStreamReader(cls.getResourceAsStream(name), charsetName)).readText()
        }
    }
}
