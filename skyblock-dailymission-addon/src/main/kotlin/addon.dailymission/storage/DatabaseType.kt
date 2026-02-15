package addon.dailymission.storage

enum class DatabaseType {
    SQLITE,
    MYSQL;

    companion object {
        fun from(raw: String): DatabaseType {
            return if (raw.equals("mysql", ignoreCase = true)) MYSQL else SQLITE
        }
    }
}
