"""Database manager module."""

import sqlite3


class DatabaseManager():
    """Manage database connection."""

    def __init__(self, db):
        self.conn = sqlite3.connect(db)
        self.conn.execute('PRAGMA foreign_keys = on')
        self.conn.commit()
        self.cur = self.conn.cursor()

    def query(self, query, values=tuple()):
        """Execute given query.

        Args:
            query (str): SQL query.

        """
        self.cur.execute(query, values)
        self.conn.commit()
        return self.cur

    def __del__(self):
        self.conn.close()
