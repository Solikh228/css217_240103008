class RecordNotFoundException(Exception):
    pass

class DatabaseLockedException(Exception):
    pass

class LegacyDatabaseConnection:
    def executeFetch(self, record_id: int, out_buffer: list) -> int:
        if record_id == 404:
            return -1
        if record_id == 500:
            return -2
        out_buffer[0] = "RECORD_DATA"
        return 0

class DatabaseAdapter:
    def __init__(self, legacy_connection: LegacyDatabaseConnection):
        self._legacy_connection = legacy_connection

    def find_by_id(self, record_id: int) -> str:
        out_buffer = [None]
        result_code = self._legacy_connection.executeFetch(record_id, out_buffer)

        if result_code == 0:
            return out_buffer[0]
        elif result_code == -1:
            raise RecordNotFoundException(f"Record with ID {record_id} was not found.")
        elif result_code == -2:
            raise DatabaseLockedException("Database record or system is currently locked.")
        else:
            raise RuntimeError(f"Unexpected error code: {result_code}")