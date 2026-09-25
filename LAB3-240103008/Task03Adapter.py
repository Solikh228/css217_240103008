from abc import ABC, abstractmethod

class LegacyStudentDirectory:
    def __init__(self):
        self._students = ["Alice", "Bob", "Charlie", "David"]

    def total_entries(self) -> int:
        return len(self._students)


    def get_student_at(self, one_based_index: int) -> str:
        if one_based_index < 1 or one_based_index > len(self._students):
            raise IndexError(f"Legacy index out of bounds: {one_based_index}")
        return self._students[one_based_index - 1]

class IModernDirectory(ABC):
    @abstractmethod
    def size(self) -> int:
        pass

    @abstractmethod
    def get(self, zero_based_index: int) -> str:
        pass

class StudentDirectoryAdapter(IModernDirectory):
    def __init__(self, legacy_directory: LegacyStudentDirectory):
        self._legacy_directory = legacy_directory

    def size(self) -> int:
        
        return self._legacy_directory.total_entries()

    def get(self, zero_based_index: int) -> str:
        
        if zero_based_index < 0 or zero_based_index >= self.size():
            raise IndexError(f"Index out of bounds: {zero_based_index}")

        return self._legacy_directory.get_student_at(zero_based_index + 1)