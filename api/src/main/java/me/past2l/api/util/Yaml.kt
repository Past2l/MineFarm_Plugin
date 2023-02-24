package me.past2l.api.util

import org.yaml.snakeyaml.DumperOptions
import java.io.*
import java.nio.charset.StandardCharsets

class Yaml {
    companion object {
        fun write(path: String, map: HashMap<String, *>) {
            val file = File(path)
            try {
                if (!file.exists()) {
                    if (!file.parentFile.exists()) file.parentFile.mkdirs()
                    file.createNewFile()
                }
                val options = DumperOptions()
                options.isAllowUnicode = true
                options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
                options.isPrettyFlow = true
                val yaml = org.yaml.snakeyaml.Yaml(options)
                val writer: Writer = OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8)
                yaml.dump(map, writer)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun read(path: String): HashMap<String, *>? {
            val file = File(path)
            if (!file.exists()) return null
            try {
                return org.yaml.snakeyaml.Yaml().load(InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8))
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return null
        }
    }
}