from typing import Any, Iterator


class LegacyInventory:
    def __init__(self):
        self._items = ["GPU", "CPU", "RAM"]

    def getCatalogEnumeration(self):
        return iter(self._items)


class InventoryIteratorAdapter:
    def __init__(self, legacy_inventory: LegacyInventory):
        self._legacy_inventory = legacy_inventory

    def get_inventory_iterator(self) -> Iterator[Any]:
        enumeration = self._legacy_inventory.getCatalogEnumeration()

        class EnumerationIterator:
            def __init__(self, enum_obj):
                self._enum = enum_obj

            def __iter__(self):
                return self

            def __next__(self):
                try:
                    return next(self._enum)
                except StopIteration:
                    raise StopIteration

        return EnumerationIterator(enumeration)