from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Optional

@dataclass(frozen=True)
class UserProfile:
    id: int
    full_name: str
    role: str
    

class LegacyCsvUserStore:
    
    def fetch_next_row(self) -> Optional[str]:
        return "101,John Doe,ADMIN"

class IUserSource(ABC):
    @abstractmethod
    def get_next_user(self) -> UserProfile:
        pass


class CsvUserAdapter(IUserSource):
    def __init__(self, legacy_store: LegacyCsvUserStore):
        self._legacy_store = legacy_store

    def get_next_user(self) -> UserProfile:
        
        row = self._legacy_store.fetch_next_row()

    
        if row is None:
            raise RuntimeError("Invalid or missing CSV row from legacy store")

        
        tokens = [token.strip() for token in row.split(",")]

        
        if len(tokens) != 3 or not tokens[0]:
            raise RuntimeError(f"Invalid CSV line structure: {row}")

        
        try:
            user_id = int(tokens[0])
        except ValueError as e:
            raise RuntimeError(f"Invalid integer ID in CSV: {tokens[0]}") from e

        full_name = tokens[1]
        role = tokens[2]

        return UserProfile(id=user_id, full_name=full_name, role=role)