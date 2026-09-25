from abc import ABC, abstractmethod
from datetime import datetime, timezone, date


class LegacyClock:
    
    def get_epoch_seconds(self) -> int:
        return 1700000000


class IModernCalendar(ABC):
    @abstractmethod
    def get_current_date(self) -> date:
        pass

class ClockAdapter(IModernCalendar):
    def __init__(self, legacy_clock: LegacyClock):
        self._legacy_clock = legacy_clock

    def get_current_date(self) -> date:
        
        epoch_seconds = self._legacy_clock.get_epoch_seconds()
        
        utc_dt = datetime.fromtimestamp(epoch_seconds, tz=timezone.utc)

        return utc_dt.date()
    
