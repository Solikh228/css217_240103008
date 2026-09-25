class EnterpriseTelemetryLogger:
    def writeLog(self, level: int, appName: str, message: str):
        print(f"[{appName}][Level {level}] {message}")


class TelemetryLoggerAdapter:
    def __init__(self, logger: EnterpriseTelemetryLogger, appName: str):
        self._logger = logger
        self._appName = appName

    def info(self, message: str) -> None:
        self._logger.writeLog(1, self._appName, message)

    def warn(self, message: str) -> None:
        self._logger.writeLog(2, self._appName, message)

    def error(self, message: str) -> None:
        self._logger.writeLog(3, self._appName, message)